package com.massaptitude.crm.svc;

import com.touchmedia.crm.svc.BaseMobileDetailService;
import com.touchmedia.crm.svc.MobileServiceRequest;
import com.touchmedia.crm.svc.ServiceRequestException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import sun.net.www.protocol.http.HttpURLConnection;

public class YouDaoService extends BaseMobileDetailService {

	// phone number lookup service
	public static String REQUEST_QUERY="http://www.youdao.com/smartresult-xml/search.s";  

	// Request parameters
	public static final String REQUEST_PARAMETER = "type=mobile&q=";
	
	public String getRequestQuery(){
		return REQUEST_QUERY;
	}

	public String getRequestParameter( String mobileNumber ) {
		return REQUEST_PARAMETER + mobileNumber;
	}
	
	
	/**
	 * Private constructor forces use of the getService method
	 */
	private YouDaoService() {
		super();
	}
	
	/**
	 * Factory method 
	 * @return Web163Service - new instance
	 */
	public static YouDaoService getService() {
		return new YouDaoService();
	}

	@Override
	public MobileServiceRequest getRequest( String mobileDetailNumber ) 
	throws ServiceRequestException {

		HttpURLConnection httpConn = null;
		MobileServiceRequest req = null;
		try {
			httpConn = (HttpURLConnection)( new URL( getRequestQuery() ).openConnection() );
			req = new YouDaoServiceRequest( httpConn, getRequestParameter( mobileDetailNumber ) );
		} 
		catch (MalformedURLException ex) {
			throw new ServiceRequestException("Incorrectly formatted URL discovered while intitializing the http connection.", ex);
		} 
		catch (IOException ex) {
			throw new ServiceRequestException("Issue connecting to the specified http resource.", ex);
		}  

		return req;
	}	
}
