package org.codehaus.jgeocoder;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.codehaus.jgeocoder.SearchTerm.Tag;
import org.codehaus.jgeocoder.SearchTerm.TaggedField;
import org.codehaus.jgeocoder.analyzer.SearchTermAnalyzer;
import org.codehaus.jgeocoder.application.NameConstants;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import static org.codehaus.jgeocoder.index.FieldNames.*;
public class JGeocoder{
	@Inject @Named(NameConstants.ANALYZER_STREET)
	private SearchTermAnalyzer streetAnalyzer;
	
	@Inject @Named(NameConstants.ANALYZER_CITY)
	private SearchTermAnalyzer cityAnalyzer;

	@Inject @Named(NameConstants.INDEX_TIGER)
	private IndexSearcher tiger1Searcher;
	
	public void test(String test) throws Exception{
		test = "university ave philadelpiha pa";
		List<SearchTerm> sts = new SearchInputContext(test).getSearchTerms();
		streetAnalyzer.analyzeSearchTerms(sts);
		cityAnalyzer.analyzeSearchTerms(sts);
		BooleanQuery bq = new BooleanQuery();
		for(SearchTerm st : sts){
			System.out.println(st.getToken());
			
			for(TaggedField tg : st.getTagFields()){
				if(tg.getTag() == Tag.STREET){
					TermQuery tq = new TermQuery(new Term(STREET, tg.getValue()));
					tq.setBoost(tg.getScore());
					bq.add(tq, Occur.SHOULD);
				}else if(tg.getTag() == Tag.CITY){
					String[] zips = tg.getValue().split(",");
					for(String zip : zips){
						bq.add(new TermQuery(new Term(ZIPCODE, zip)), Occur.SHOULD);
					}
				}
			}
		}

		System.out.println(bq);
//		
//		BooleanQuery bq = new BooleanQuery();
//		
//		bq.add(new TermQuery(new Term(STREET, "CASINO")), Occur.SHOULD);
//		bq.add(new TermQuery(new Term(STREET, "CENTER")), Occur.SHOULD);
//		
//		System.out.println(bq);
		
//		bq.setMinimumNumberShouldMatch(2);
		Hits hits =  tiger1Searcher.search(bq);
		for(int i =0; i< hits.length() && i < 500; i++){
			Document doc = hits.doc(i);
			System.out.println(
					Arrays.asList(
							new String[]{doc.get(FEDIRP), Arrays.toString(doc.getValues(STREET)), doc.get(FEDIRS), doc.get(FETYPE), doc.get(ZIPCODE)}));
//			System.out.println(hits.doc(i).getFields());
		}
		
//		System.out.println(sts);
	}
}