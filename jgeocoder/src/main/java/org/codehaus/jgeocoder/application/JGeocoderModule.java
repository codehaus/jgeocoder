package org.codehaus.jgeocoder.application;

import static org.codehaus.jgeocoder.index.FieldNames.FULLNAME;
import static org.codehaus.jgeocoder.index.FieldNames.STREET;
import static org.codehaus.jgeocoder.index.FieldNames.ZIPCODE;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.RAMDirectory;
import org.codehaus.jgeocoder.DataUtils;
import org.codehaus.jgeocoder.JGeocoderException;
import org.codehaus.jgeocoder.NGramUtils;
import org.codehaus.jgeocoder.SearchInputContext;
import org.codehaus.jgeocoder.SearchTerm;
import org.codehaus.jgeocoder.SearchTerm.Tag;
import org.codehaus.jgeocoder.SearchTerm.TaggedField;
import org.codehaus.jgeocoder.analyzer.CityWordAnalyzer;
import org.codehaus.jgeocoder.analyzer.SearchTermAnalyzer;
import org.codehaus.jgeocoder.index.FieldNames;
import org.codehaus.jgeocoder.index.Tiger1Bdb;
import org.codehaus.jgeocoder.index.Tiger1DAO;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
public class JGeocoderModule extends AbstractModule{
	
	private static String idxTiger = 
	    System.getProperty("jgeocoder.index.tiger") == null? 
	    		"/usr/local/jgeocoder/index/tiger" : System.getProperty("jgeocoder.index.tiger");
	private static String idxCityDict = 
	    System.getProperty("jgeocoder.index.city_dict") == null? 
	    		"/usr/local/jgeocoder/index/city_dict" : System.getProperty("jgeocoder.index.city_dict");
	public String getIdxCityDict() {
		return idxCityDict;
	}
	
	public void setIdxCityDict(String idxCityDict) {
		this.idxCityDict = idxCityDict;
	}
	public String getIdxTiger() {
		return idxTiger;
	}
	
	public void setIdxTiger(String idxTiger) {
		this.idxTiger = idxTiger;
	}
	private static final int MAX_CANDIDATES = 200;
	private static final float DISTANCE_THRESHOLD = .75f;
	private static final float MIN_NGRAM_PERCENTAGE = .6f;
	
	public static void main(String[] args) throws Exception{
		Injector injector = Guice.createInjector(new JGeocoderModule()); 
		
		SearchTermAnalyzer cityAnalyzer = injector.getInstance(SearchTermAnalyzer.class);
		String token = "mcclellans street philadelphia pennslyvlnia".toUpperCase();
		
		Map<String, IndexSearcher> searchers = new HashMap<String, IndexSearcher>();
		for(Map.Entry<String, String> e : DataUtils.getSTATE_MAP().entrySet()){
			searchers.put(e.getKey(), 
					new IndexSearcher("/usr/local/jgeocoder/index/states/"+e.getKey()));
		}
		
		IndexSearcher s = searchers.get("PA");
		
		BooleanQuery bq = new BooleanQuery();
		
		List<SearchTerm> sts = new SearchInputContext(token).getSearchTerms();
		
		cityAnalyzer.analyzeSearchTerms(sts);
		
		Set<String> zips = new HashSet<String>();
		for(SearchTerm st : sts){
			for(TaggedField tg : st.getTagFields()){
				if(tg.getTag() == SearchTerm.Tag.CITY){
					String[] values = tg.getValue().split(",");
					zips.addAll(Arrays.asList(values));
				}
			}
		}
//		BooleanQuery zipbq = new BooleanQuery();
//		for(String zip : zips){
//			zipbq.add(new TermQuery(new Term(ZIPCODE, zip)), Occur.SHOULD);
//		}
//		zipbq.setMinimumNumberShouldMatch(1);
//		BooleanClause bc = new BooleanClause(zipbq, Occur.MUST);
//		
//		
//		bq.add(bc);
		bq.add(new TermQuery(new Term(ZIPCODE, "19148")), Occur.MUST);
		List<String> ngrams = NGramUtils.nGramTokenize(" "+token+" ", 2, 2);
		for(String ngram : ngrams){
			TermQuery tq = new TermQuery(new Term(FieldNames.NGRAMS[ngram.length()], ngram));
            if(ngram.charAt(0) == ' '){
                tq.setBoost(2.5f);
            }
			bq.add(tq, Occur.SHOULD);
		}
		
//
		Tiger1Bdb db = new Tiger1Bdb();
		db.init(new File("/usr/local/jgeocoder/data/tiger1"), true, false);
		Tiger1DAO dao = new Tiger1DAO(db.getStore());
		System.out.println(bq);
        try {
        	Hits hits = s.search(bq);
        	long start = System.currentTimeMillis();
        	
			hits = s.search(bq);
			System.out.println(System.currentTimeMillis()-start);
			for(int i = 0; i<hits.length() && i<MAX_CANDIDATES; i++){
				Document doc = hits.doc(i);
				String id = doc.get(FULLNAME);
				String fullname = dao.getById(id).getValue();
				System.out.println(fullname);
//				float score = EditDistanceUtils.getNormalizedSimilarity(fullname, token);
//				if(score>=DISTANCE_THRESHOLD){
//					System.out.println(fullname);
//				}
			}
			
		} catch (IOException e) {
			throw new JGeocoderException("Unable to query tiger index", e);
		}
	}
	
	@Override
	protected void configure() {
		try {
			bind(IndexSearcher.class)
			.annotatedWith(Names.named(NameConstants.INDEX_CITY_DICT))
			.toInstance(new IndexSearcher(getIdxCityDict()));
			
			bind(SearchTermAnalyzer.class)
			.toInstance(new CityWordAnalyzer());
						
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure JGeocoder module", e);
		}
	}

}
