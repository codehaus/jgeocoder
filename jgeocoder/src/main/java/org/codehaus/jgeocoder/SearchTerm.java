package org.codehaus.jgeocoder;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * Search term value object
 * @author liangj01
 *
 */
public class SearchTerm{
	
	public static enum Tag{
		NUMBER, STREET, CITY, STATE, ZIP, STOP
	}
	
	public static class TaggedField{
		private Tag tag;
		private String value;
		private float score;
		private int freq;
		
		public TaggedField(float score, Tag tag, String value, int freq) {
			super();
			this.score = score;
			this.tag = tag;
			this.value = value;
			this.freq = freq;
		}
		
		public int getFreq() {
			return freq;
		}
		
		public void setFreq(int freq) {
			this.freq = freq;
		}
		
		public void setScore(float score) {
			this.score = score;
		}
		public float getScore() {
			return score;
		}
		public Tag getTag() {
			return tag;
		}
		public void setTag(Tag tag) {
			this.tag = tag;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}
	
	public SearchTerm(String token, String separator){
		this.token = token;
		this.separator = separator;
	}
	
	private String separator;
	private String token;
	private Collection<TaggedField> tagFields = new HashSet<TaggedField>();
	
	public Collection<TaggedField> getTagFields() {
		return tagFields;
	}
	
	public void addTaggedField(TaggedField tagField){
		tagFields.add(tagField);
	}
	
	public void setTagFields(Collection<TaggedField> tagFields) {
		this.tagFields = tagFields;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}