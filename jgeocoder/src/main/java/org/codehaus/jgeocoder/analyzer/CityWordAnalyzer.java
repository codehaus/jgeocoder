package org.codehaus.jgeocoder.analyzer;


import static org.codehaus.jgeocoder.index.FieldNames.*;
import static org.codehaus.jgeocoder.index.FieldNames.WORD;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.codehaus.jgeocoder.EditDistanceUtils;
import org.codehaus.jgeocoder.JGeocoderException;
import org.codehaus.jgeocoder.NGramUtils;
import org.codehaus.jgeocoder.SearchTerm;
import org.codehaus.jgeocoder.SearchTerm.Tag;
import org.codehaus.jgeocoder.SearchTerm.TaggedField;
import org.codehaus.jgeocoder.application.NameConstants;
import org.codehaus.jgeocoder.index.FieldNames;

import com.google.inject.Inject;
import com.google.inject.name.Named;



public class CityWordAnalyzer implements SearchTermAnalyzer{

	@Inject @Named(NameConstants.INDEX_CITY_DICT)
	private IndexSearcher cityDictSearcher;
	
	private static final int MAX_CANDIDATES = 50;
	private static final float DISTANCE_THRESHOLD = .75f;
	private static final float MIN_NGRAM_PERCENTAGE = .6f;
	
	@Override
	public void analyzeSearchTerms(List<SearchTerm> searchTerms) throws JGeocoderException {
		for(SearchTerm st : searchTerms){
			String token = st.getToken();
			if(token.length() < 3){
				continue;
			}
			BooleanQuery bq = new BooleanQuery();
			List<String> ngrams = NGramUtils.nGramTokenize(" "+token+" ", 2, 3);
			for(String ngram : ngrams){
				TermQuery tq = new TermQuery(new Term(FieldNames.NGRAMS[ngram.length()], ngram));
                if(ngram.charAt(0) == ' '){
                    tq.setBoost(2.5f);
                }
				bq.add(tq, Occur.SHOULD);
			}
			bq.setMinimumNumberShouldMatch((int)(ngrams.size()*MIN_NGRAM_PERCENTAGE));
			
			int buffer = Math.max((int).25f * token.length(), 2);
			
            String lower = StringUtils.leftPad(String.valueOf(Math.max(token.length() - buffer, 2)), 2, '0') ;
            String upper = StringUtils.leftPad(String.valueOf(token.length() + buffer), 2, '0') ;
            RangeQuery range = new RangeQuery(new Term(LENGTH, lower),
                    new Term(LENGTH, upper), true);

            CachingWrapperFilter filter = new CachingWrapperFilter(new QueryWrapperFilter(range));
            try {
				Hits hits = cityDictSearcher.search(bq, filter);

				for(int i = 0; i<hits.length() && i<MAX_CANDIDATES; i++){
					Document doc = hits.doc(i);
					String word = doc.get(WORD);
					
					float score = EditDistanceUtils.getNormalizedSimilarity(word, token);
					if(score>=DISTANCE_THRESHOLD){
						TaggedField taggedField = new TaggedField(score, Tag.CITY, doc.get(ZIPS), 1);
						st.addTaggedField(taggedField);
					}
				}
				
			} catch (IOException e) {
				throw new JGeocoderException("Unable to query city_dict index", e);
			}
		}
	}
	
}
