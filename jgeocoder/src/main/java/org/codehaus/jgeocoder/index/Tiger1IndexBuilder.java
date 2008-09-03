package org.codehaus.jgeocoder.index;

import static org.codehaus.jgeocoder.index.FieldNames.FEDIRP;
import static org.codehaus.jgeocoder.index.FieldNames.FEDIRS;
import static org.codehaus.jgeocoder.index.FieldNames.FETYPE;
import static org.codehaus.jgeocoder.index.FieldNames.FRADDL;
import static org.codehaus.jgeocoder.index.FieldNames.FRADDR;
import static org.codehaus.jgeocoder.index.FieldNames.FRLAT;
import static org.codehaus.jgeocoder.index.FieldNames.FRLONG;
import static org.codehaus.jgeocoder.index.FieldNames.STREET;
import static org.codehaus.jgeocoder.index.FieldNames.TLID;
import static org.codehaus.jgeocoder.index.FieldNames.TOADDL;
import static org.codehaus.jgeocoder.index.FieldNames.TOADDR;
import static org.codehaus.jgeocoder.index.FieldNames.TOLAT;
import static org.codehaus.jgeocoder.index.FieldNames.TOLONG;
import static org.codehaus.jgeocoder.index.FieldNames.ZIPCODE;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;

/**
 * 
 * 
 * @author liangj01
 *
 */
class Tiger1IndexBuilder{
	
	public static void main(String[] args) throws Exception{
		IndexWriter w = null;
		BufferedReader r = null;
//		if(true) throw new RuntimeException("dont run");
		try {
			w = new IndexWriter("/usr/local/jgeocoder/index/tiger1", new KeywordAnalyzer() );
			
		    r = new BufferedReader(new FileReader(Thread.currentThread().getContextClassLoader().getResource("org/codehaus/jgeocoder/index/tiger1").getFile()));
			String line = null;
			int count = 0;
			while((line = r.readLine()) != null){
				if(count++ > 1000){
					System.out.println(count);
				}
				String[] items = line.split("[|]");
				int i =0;
				String tlid = items[i++].trim();
				String fraddl = StringUtils.leftPad(items[i++].trim(), 4, '0');
				String fraddr = StringUtils.leftPad(items[i++].trim(), 4, '0');
				String toaddl = StringUtils.leftPad(items[i++].trim(), 4, '0');
				String toaddr = StringUtils.leftPad(items[i++].trim(), 4, '0');
				
				String fedirp = items[i++].trim();
				String street = items[i++].trim();
				String fedirs = items[i++].trim();
				String fetype = items[i++].trim();
				String zipcode = items[i++].trim();
				String frlat = items[i++].trim();
				String tolat = items[i++].trim();
				String frlong = items[i++].trim();
				String tolong = items[i++].trim();
				
				Document doc = new Document();
				doc.add(new Field(TLID, tlid, Store.YES, Index.NO));
				doc.add(new Field(FRADDL, fraddl, Store.YES, Index.UN_TOKENIZED));
				doc.add(new Field(FRADDR, fraddr, Store.YES, Index.UN_TOKENIZED));
				doc.add(new Field(TOADDL, toaddl, Store.YES, Index.UN_TOKENIZED));
				doc.add(new Field(TOADDR, toaddr, Store.YES, Index.UN_TOKENIZED));
				
				
				doc.add(new Field(FEDIRP, fedirp, Store.YES, Index.NO));
				doc.add(new Field(FEDIRS, fedirs, Store.YES, Index.NO));
				
				doc.add(new Field(FETYPE, fetype, Store.YES, Index.UN_TOKENIZED));
				
				doc.add(new Field(ZIPCODE, zipcode, Store.YES, Index.UN_TOKENIZED));
				
				doc.add(new Field(FRLAT, frlat, Store.YES, Index.NO));
				doc.add(new Field(FRLONG, frlong, Store.YES, Index.NO));
				
				doc.add(new Field(TOLAT, tolat, Store.YES, Index.NO));
				doc.add(new Field(TOLONG, tolong, Store.YES, Index.NO));
				
				doc.add(new Field(STREET, street, Store.YES, Index.TOKENIZED));
				
				w.addDocument(doc);
			}
			
		} finally{
			IOUtils.closeQuietly(r);
			w.close();
		}
		
	}
}