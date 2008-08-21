package org.codehaus.jgeocoder.index;

import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.COUNTY;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.NGRAMS;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.POST;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.PRE;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.STATE;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.WORD;
import static org.codehaus.jgeocoder.index.StreetDictDocumentNames.ZIP;

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
		try {
			w = new IndexWriter("/usr/local/jgeocoder/index/street_dict", new KeywordAnalyzer() );
			
		    r = new BufferedReader(new FileReader(Thread.currentThread().getContextClassLoader().getResource("org/codehaus/jgeocoder/index/street_dict.txt").getFile()));
			String line = null;
			int count = 0;
			while((line = r.readLine()) != null){
				if(count++ > 1000){
					System.out.println(count);
				}
				String[] values = (" "+line+" ").split("[|]");
				Document doc = new Document();
				if(StringUtils.isBlank(values[1])){
					continue;
				}
				if(StringUtils.isNotBlank(values[0])){
					doc.add(new Field(PRE, values[0], Store.YES, Index.NO));
				}
				doc.add(new Field(WORD, values[1], Store.YES, Index.UN_TOKENIZED));
				
				if(StringUtils.isNotBlank(values[2])){
					doc.add(new Field(POST, values[2], Store.YES, Index.NO));
				}
				if(StringUtils.isNotBlank(values[3])){
					doc.add(new Field(COUNTY, values[3], Store.YES, Index.NO));
				}
				if(StringUtils.isNotBlank(values[4])){
					doc.add(new Field(STATE, values[4], Store.YES, Index.UN_TOKENIZED));
				}
				if(StringUtils.isNotBlank(values[5])){
					doc.add(new Field(ZIP, values[5], Store.YES, Index.UN_TOKENIZED));
				}
				for(String ngram : NGramUtils.nGramTokenize(" "+values[1]+" ", 2, 3)){
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