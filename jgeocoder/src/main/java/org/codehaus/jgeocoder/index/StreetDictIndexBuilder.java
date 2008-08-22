package org.codehaus.jgeocoder.index;

import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.LENGTH;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.NGRAMS;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.*;

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
import org.codehaus.jgeocoder.NGramUtils;

/**
 * Builds a lucene index of single token street name words
 * 
 * @author liangj01
 *
 */
class StreetDictIndexBuilder{
	
	public static void main(String[] args) throws Exception{
		IndexWriter w = null;
		BufferedReader r = null;
		if(true) throw new RuntimeException("dont run");
		try {
			w = new IndexWriter("/usr/local/jgeocoder/index/street_dict", new KeywordAnalyzer() );
			
		    r = new BufferedReader(new FileReader(Thread.currentThread().getContextClassLoader().getResource("org/codehaus/jgeocoder/index/street_dict").getFile()));
			String line = null;
			int count = 0;
			while((line = r.readLine()) != null){
				if(count++ > 1000){
					System.out.println(count);
				}
				String v1 = line.substring(0, 7).trim();
				String v2 = line.substring(7).trim();
				Document doc = new Document();
				doc.add(new Field(WORD, v2, Store.YES, Index.NO));
				doc.add(new Field(FREQ, String.valueOf(v1), Store.YES, Index.NO));
				doc.add(new Field(LENGTH, StringUtils.leftPad(String.valueOf(v2.length()), 2, '0'), Store.NO, Index.UN_TOKENIZED));
				String token = " "+v2+" ";
				for(String ngram : NGramUtils.nGramTokenize(token, 2, 3)){
					String name = NGRAMS[ngram.length()];
					doc.add(new Field(name, ngram, Store.NO, Index.UN_TOKENIZED));
				}
				w.addDocument(doc);
			}
			
		} finally{
			IOUtils.closeQuietly(r);
			w.close();
		}
		
	}
}