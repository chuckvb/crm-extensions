package com.massaptitude.crm.svc;

import com.touchmedia.crm.svc.MobileDetailServiceResponse;
import com.touchmedia.crm.svc.MobileServiceRequest;
import com.touchmedia.crm.svc.ServiceRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;

import sun.net.www.protocol.http.HttpURLConnection;


public class YouDaoServiceRequest implements MobileServiceRequest {

	public static final String REQUEST_METHOD = "GET";

	HttpURLConnection httpConn = null;
	
	/**
	 * @return the requestMethod
	 */
	@Override
	public String getRequestMethod() {
		return REQUEST_METHOD;
	}

	public YouDaoServiceRequest( HttpURLConnection newConn, String requestParameter )  
	throws ServiceRequestException {
		httpConn = newConn;
		try {
			httpConn.setRequestMethod( getRequestMethod() );
			httpConn.setDoOutput( true );		
			httpConn.getOutputStream().write( requestParameter.getBytes() );
		} 
		catch ( ProtocolException ex ) {
			throw new ServiceRequestException( "Failed to set the http request method in setting up the YouDao request.", ex );
		}  
		catch (IOException ex) {
			throw new ServiceRequestException( "Failed to write to the http output stream", ex );
		}
	}

	@Override
	public MobileDetailServiceResponse execute() 
	throws ServiceRequestException {
		// execute the request 
		InputStream inStream = null;
		try {
			httpConn.getOutputStream().flush();
			httpConn.getOutputStream().close();
			inStream = httpConn.getInputStream();
		} catch (IOException ex) {
			throw new ServiceRequestException("Failed to in handling of http output or input streams in executing the YouDao request.", ex);
		}  
		return new YouDaoServiceResponse( inStream );
	}

}
