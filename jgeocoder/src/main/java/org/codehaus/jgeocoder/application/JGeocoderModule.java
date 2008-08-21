package org.codehaus.jgeocoder.application;

import org.apache.lucene.search.IndexSearcher;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class JGeocoderModule extends AbstractModule{

	private String idxStreetDict = 
		    System.getProperty("jgeocoder.data.home") == null? 
		    		"/usr/local/jgeocoder/data" : System.getProperty("jgeocoder.data.home");
	
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
			
			
			
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure JGeocoder module", e);
		}
	}

}
