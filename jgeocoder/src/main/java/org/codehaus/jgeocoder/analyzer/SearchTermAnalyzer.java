package org.codehaus.jgeocoder.analyzer;

import java.util.List;

import org.codehaus.jgeocoder.JGeocoderException;
import org.codehaus.jgeocoder.SearchTerm;

public interface SearchTermAnalyzer {
	public void analyzeSearchTerms(List<SearchTerm> searchTerms) throws JGeocoderException;
}
