package org.codehaus.jgeocoder;

public class JGeocoderException extends Exception{

	private static final long serialVersionUID = 20080822L;

	public JGeocoderException() {
		super();
	}

	public JGeocoderException(String message, Throwable cause) {
		super(message, cause);
	}

	public JGeocoderException(String message) {
		super(message);
	}

	public JGeocoderException(Throwable cause) {
		super(cause);
	}
	
}