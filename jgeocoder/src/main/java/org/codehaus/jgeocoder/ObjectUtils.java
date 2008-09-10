package org.codehaus.jgeocoder;

/**
 * Utilities on generic objects
 * @author liangj01
 *
 */
public class ObjectUtils{
	private ObjectUtils(){}

  /**
   * Replace an object with a replacement object if it's <code>null</code>
   *
   * @param <T>
   * @param value the object to test for a null value.
   * @param replacement the value returned if input <code>value</code> is null.
   * @return <code>replacement</code> if input <code>value</code> is <code>null<code>, 
   * <code>value</code> otherwise.
   */
  public static <T> T nvl(T value, T replacement){
    return value==null? replacement : value;
  }
}