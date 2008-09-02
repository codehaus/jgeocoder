package org.codehaus.jgeocoder.application;

import org.apache.lucene.search.IndexSearcher;
import org.codehaus.jgeocoder.JGeocoder;
import org.codehaus.jgeocoder.analyzer.CityWordAnalyzer;
import org.codehaus.jgeocoder.analyzer.SearchTermAnalyzer;
import org.codehaus.jgeocoder.analyzer.StreetWordAnalyzer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
public class JGeocoderModule extends AbstractModule{

	private String idxStreetDict = 
		    System.getProperty("jgeocoder.index.street_dict") == null? 
		    		"/usr/local/jgeocoder/index/street_dict" : System.getProperty("jgeocoder.index.street_dict");
	private String idxCityDict = 
	    System.getProperty("jgeocoder.index.city_dict") == null? 
	    		"/usr/local/jgeocoder/index/city_dict" : System.getProperty("jgeocoder.index.city_dict");
	
	public String getIdxStreetDict() {
		return idxStreetDict;
	}
	
	public void setIdxStreetDict(String idxStreetDict) {
		this.idxStreetDict = idxStreetDict;
	}
	
	public String getIdxCityDict() {
		return idxCityDict;
	}
	
	public void setIdxCityDict(String idxCityDict) {
		this.idxCityDict = idxCityDict;
	}
	
	public static void main(String[] args) throws Exception{
		Injector injector = Guice.createInjector(new JGeocoderModule()); 
		JGeocoder jg = injector.getInstance(JGeocoder.class);
		jg.test("south st philadelphia");
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
			
			bind(IndexSearcher.class)
			.annotatedWith(Names.named(NameConstants.INDEX_CITY_DICT))
			.toInstance(new IndexSearcher(getIdxCityDict()));
			
			bind(SearchTermAnalyzer.class)
			.annotatedWith(Names.named(NameConstants.ANALYZER_CITY))
			.toInstance(new CityWordAnalyzer());
			
			bind(JGeocoder.class).toInstance(new JGeocoder());
			
			
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure JGeocoder module", e);
		}
	}

}
