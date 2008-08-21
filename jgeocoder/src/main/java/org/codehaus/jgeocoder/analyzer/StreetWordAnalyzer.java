package org.codehaus.jgeocoder.analyzer;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.codehaus.jgeocoder.NGramUtils;
import org.codehaus.jgeocoder.SearchTerm;
import org.codehaus.jgeocoder.application.NameConstants;
import org.codehaus.jgeocoder.index.StreetDictDocumentNames;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class StreetWordAnalyzer implements SearchTermAnalyzer{

	@Inject @Named(NameConstants.INDEX_STREET_DICT)
	private IndexSearcher streetDictSearcher;
	
	@Override
	public void analyzeSearchTerms(List<SearchTerm> searchTerms) {
		for(SearchTerm st : searchTerms){
			
			BooleanQuery bq = new BooleanQuery();
			List<String> ngrams = NGramUtils.nGramTokenize(st.getToken(), 2, 3);
			for(String ngram : ngrams){
				bq.add(new TermQuery(new Term(StreetDictDocumentNames.NGRAMS[ngram.length()], ngram)), Occur.SHOULD);
			}
		}
	}

}
