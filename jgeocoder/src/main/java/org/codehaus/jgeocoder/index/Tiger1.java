package org.codehaus.jgeocoder.index;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;


@Entity
public class Tiger1{
  @PrimaryKey
  private String id;
  private String value;
  public String getId() {
	return id;
  }
  public String getValue() {
	return value;
  }
  public void setId(String id) {
	this.id = id;
  }
  public void setValue(String value) {
	this.value = value;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}
