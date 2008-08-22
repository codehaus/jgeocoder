package org.codehaus.jgeocoder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SearchInputContext{
	private final String originalInput;
	private final String cleanedInput;
	
	private final List<SearchTerm> searchTerms = new ArrayList<SearchTerm>();
	
	private static final Pattern TERM = Pattern.compile("(.+?)(\\W+)?");
	
	
	public SearchInputContext(String input){
		originalInput = input;
		cleanedInput = getCleanSttring(input);
		StringTokenizer tk = new StringTokenizer(cleanedInput.replaceAll(",", ", "));
		while(tk.hasMoreTokens()){
			Matcher m = TERM.matcher(tk.nextToken());
			m.matches();
			searchTerms.add(new SearchTerm(m.group(1), m.group(2)));
		}
		
	}
	
	public List<SearchTerm> getSearchTerms() {
		return searchTerms;
	}
	
	public String getOriginalInput() {
		return originalInput;
	}
	
	public String getCleanedInput() {
		return cleanedInput;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
    private static final Pattern CLEANUP = Pattern.compile("^\\W+|\\W+$|[\\p{Punct}&&[^#,:;-]]");
    private static final Pattern SPACE = Pattern.compile("\\s+");
    /**
     * Remove all semantically insignificant punctuation characters; condense white spaces.
     * In addition, converts everything to lower case
     * @param where
     * @return
     */
    private static String getCleanSttring(String where){
      return SPACE.matcher(CLEANUP.matcher(where).replaceAll(" ")).replaceAll(" ").trim().toUpperCase();
    }   
}