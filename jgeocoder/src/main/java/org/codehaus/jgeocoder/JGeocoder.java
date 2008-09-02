package org.codehaus.jgeocoder;

import java.util.List;

import org.codehaus.jgeocoder.SearchTerm.TaggedField;
import org.codehaus.jgeocoder.analyzer.SearchTermAnalyzer;
import org.codehaus.jgeocoder.application.NameConstants;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class JGeocoder{
	@Inject @Named(NameConstants.ANALYZER_STREET)
	private SearchTermAnalyzer streetAnalyzer;
	
	@Inject @Named(NameConstants.ANALYZER_CITY)
	private SearchTermAnalyzer cityAnalyzer;
	
	public void test(String test) throws Exception{
		List<SearchTerm> sts = new SearchInputContext(test).getSearchTerms();
		streetAnalyzer.analyzeSearchTerms(sts);
		cityAnalyzer.analyzeSearchTerms(sts);
		for(SearchTerm st : sts){
			System.out.println(st.getToken());
			for(TaggedField tg : st.getTagFields()){
				System.out.println(tg);
			}
		}
//		System.out.println(sts);
	}
}