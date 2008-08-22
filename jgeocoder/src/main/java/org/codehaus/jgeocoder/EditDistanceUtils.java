package org.codehaus.jgeocoder;

import org.apache.commons.lang.StringUtils;

public class EditDistanceUtils{
	
	/**
	 * @param s1
	 * @param s2
	 * @return a normalized edit distance similarity between s1 and s2
	 * normalizedSimilarity = 1 - levenshteinDistance / max(|s1|, |s2|)
	 */
	public static float getNormalizedSimilarity(String s1, String s2){
		int length = s1.length() > s2.length()? s1.length() : s2.length();
		return 1- StringUtils.getLevenshteinDistance(s1, s2) / (float)length;
	}
}