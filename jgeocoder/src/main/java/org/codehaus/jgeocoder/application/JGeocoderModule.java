package org.codehaus.jgeocoder.application;

import org.apache.lucene.search.IndexSearcher;
import org.codehaus.jgeocoder.JGeocoder;
import org.codehaus.jgeocoder.analyzer.StreetWordAnalyzer;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.codehaus.jgeocoder.analyzer.SearchTermAnalyzer;
public class JGeocoderModule extends AbstractModule{

	private String idxStreetDict = 
		    System.getProperty("jgeocoder.index.street_dict") == null? 
		    		"/usr/local/jgeocoder/index/street_dict" : System.getProperty("jgeocoder.index.street_dict");
	
	public String getIdxStreetDict() {
		return idxStreetDict;
	}
	
	public void setIdxStreetDict(String idxStreetDict) {
		this.idxStreetDict = idxStreetDict;
	}
	
	@Override
	protected void configure() {
		try {
			bind(IndexSearcher.class)
			.annotatedWith(Names.named(NameConstants.INDEX_STREET_DICT))
			.toInstance(new IndexSearcher(getIdxStreetDict()));
			
			bind(SearchTermAnalyzer.class)
			.annotatedWith(Names.named(NameConstants.ANALYZER_STREET))
			.toInstance(new StreetWordAnalyzer());
			
			bind(JGeocoder.class).toInstance(new JGeocoder());
			
			
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure JGeocoder module", e);
		}
	}

}
