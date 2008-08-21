package org.codehaus.jgeocoder;

import java.util.LinkedList;
import java.util.List;

/**
 * NGram utils
 * @author liangj01
 *
 */
public class NGramUtils{

	/**
	 * Tokenize input string with ngrma from minNGram to maxNGram
	 * @param s
	 * @param minNGram
	 * @param maxNGram
	 * @return a list of ngrams  (minNGram to maxNGram inclusive) 
	 */
    public static List<String> nGramTokenize(String s, int minNGram, int maxNGram){
    	if(s == null || minNGram < 1)  throw new IllegalArgumentException("s is null or minNGram less than 1");
        List<String> ret = new LinkedList<String>();
        for(int i =minNGram; i<= maxNGram; i++){
        	if (s.length() > i){
                for (int j = 0; j <= s.length() - i; j++){
                    ret.add(s.substring(j, j + i));
                }
            }else if(s.length() == i){
                ret.add(s);
            }
        }
        
        return ret;
    }
}
