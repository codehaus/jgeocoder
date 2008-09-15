package org.codehaus.jgeocoder.index;

import static org.codehaus.jgeocoder.DataUtils.expandLocalityAbbrv;
import static org.codehaus.jgeocoder.ObjectUtils.nvl;
import static org.codehaus.jgeocoder.index.FieldNames.FEDIRP;
import static org.codehaus.jgeocoder.index.FieldNames.FEDIRS;
import static org.codehaus.jgeocoder.index.FieldNames.FULLNAME;
import static org.codehaus.jgeocoder.index.FieldNames.NGRAMS;
import static org.codehaus.jgeocoder.index.FieldNames.TLID;
import static org.codehaus.jgeocoder.index.FieldNames.ZIPCODE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.codehaus.jgeocoder.DataUtils;
import org.codehaus.jgeocoder.NGramUtils;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;
/**
 * 
 * 
 * @author liangj01
 *
 */
class Tiger1IndexBuilder{
	
    public static class Street{
    	String street; String type; String county; String city; String state;

		
		public Street(
				String street, String type, String city, String county, String state) {
			super();
			this.city = city;
			this.county = county;
			this.state = state;
			this.street = street;
			this.type = type;
		}


		@Override
		public String toString() {
			return StringUtils.join(new String[]{
					street,  
					nvl(type, ""), nvl(city, ""), 
					nvl(county, ""), nvl(state, "")}, ' ').replaceAll("\\s+", " ").trim();
		}
    	
    }
    
	public static void main(String[] args) throws Exception{
		IndexWriter w = null;
		Map<String, IndexWriter> writers = new HashMap<String, IndexWriter>();
		BufferedReader r = null;
		BufferedReader r2 = null;
//		if(true) throw new RuntimeException("dont run");
		try {
			
			Map<String, String> zip2City = new HashMap<String, String>();
			r2 = new BufferedReader(new FileReader(Thread.currentThread().getContextClassLoader().getResource("org/codehaus/jgeocoder/index/ZIP_CODES.txt").getFile()));
			
			String line = null;
			int count = 0;
			while((line = r2.readLine()) != null){
				String[] all = line.split(",");
				zip2City.put(all[0].substring(1, 6), all[3].replaceAll("\"", ""));
			}
			
			
			
			for(Map.Entry<String, String> e : DataUtils.getSTATE_MAP().entrySet()){
				writers.put(e.getKey(), 
						new IndexWriter("/usr/local/jgeocoder/index/states/"+e.getKey(), 
								new KeywordAnalyzer()));
			}
			
			w = new IndexWriter("/usr/local/jgeocoder/index/full", new KeywordAnalyzer());
			
			
		    r = new BufferedReader(new FileReader("/home/liangj01/Desktop/street_all.txt"));
			line = null;
			count = 0;
			while((line = r.readLine()) != null){
				if(count++ > 1000){
					System.out.println(count);
				}
				
				
				
				String[] items = line.split("[|]");
				int i =0;
				
				String fedirp = StringUtils.trimToNull(items[i++]);
				String street = StringUtils.trimToNull(items[i++]);
				String fetype = StringUtils.trimToNull(items[i++]);
				String fedirs = StringUtils.trimToNull(items[i++]);
				String county = StringUtils.trimToNull(items[i++]);
				String state = StringUtils.trimToNull(items[i++]);
				String zipcode = StringUtils.trimToNull(items[i++]);
				String tlid = items[i++];
		
				Set<String> ret = new HashSet<String>();

		    	String type = expandLocalityAbbrv(fetype, false);
		    	
		    	street = expandLocalityAbbrv(street, true);
		    	
		    	String city = zip2City.get(zipcode);
		    	String stateabbrv = state;
		    	state = DataUtils.expandState(state);
		    	
		    	ret.add(new Street(street, type, county, city, null).toString());
		    	
		    	for(String s : ret){
					Document doc = new Document();
//					doc.add(new Field(TLID, tlid, Store.YES, Index.NO));
//					if(fedirp != null){
//						doc.add(new Field(FEDIRP, fedirp, Store.YES, Index.NO));
//					}
//					if(fedirs != null){
//						doc.add(new Field(FEDIRS, fedirs, Store.YES, Index.NO));
//					}
					
					doc.add(new Field(ZIPCODE, zipcode, Store.YES, Index.UN_TOKENIZED));
					doc.add(new Field(FULLNAME, String.valueOf(count), Store.YES, Index.NO));
					String token = " "+s+" ";
					for(String ngram : NGramUtils.nGramTokenize(token, 2, 2)){
						String name = NGRAMS[ngram.length()];
						doc.add(new Field(name, ngram, Store.NO, Index.UN_TOKENIZED));
					}
					writers.get(stateabbrv).addDocument(doc);		    		
		    	}
		    	

			}
			
		} finally{
			IOUtils.closeQuietly(r);
			IOUtils.closeQuietly(r2);
			for(Map.Entry<String, IndexWriter> e : writers.entrySet()){
				e.getValue().close();
			}
		}
		
	}
}