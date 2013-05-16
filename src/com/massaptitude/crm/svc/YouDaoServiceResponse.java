package com.massaptitude.crm.svc;

import com.touchmedia.crm.svc.MobileDetailServiceResponse;
import com.touchmedia.crm.svc.ServiceResponseProcessException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.touchmedia.crm.entity.MobileDetail;

public class YouDaoServiceResponse implements MobileDetailServiceResponse {

	protected InputStream inputStream = null;
	
	public YouDaoServiceResponse( InputStream inStream ) {
		inputStream = inStream;
	}
	
	@Override
	/**
	 * Processes a single response from the service to populate mobile details.
	 * @param inStream InputStream - the response from streamed from the service 
	 * @param mobileDetail MobileDetail - the given mobile detail information 
	 * @return MobileDetail - the complete mobile detail information included that retrieved by the service
	 */
	public MobileDetail process( MobileDetail mobileDetail ) 
	throws ServiceResponseProcessException {
				
		// prepare the return value with the number and id
		MobileDetail mobileDetailReturn = new MobileDetail();
		mobileDetailReturn.setMobileId(mobileDetail.getMobileId());
		mobileDetailReturn.setMobileNumber(mobileDetail.getMobileNumber());

		processMobileServiceData( inputStream, mobileDetailReturn );
		return mobileDetailReturn;
	}

	
	/**
	 * 
	 * @param inStream
	 * @param mobileDetailReturn
	 * @throws IOException
	 * @throws ServiceResponseProcessException
	 */
	public void processMobileServiceData(InputStream inStream, MobileDetail mobileDetailReturn)
	throws ServiceResponseProcessException	{
		
		DocumentBuilderFactory docBuildFactory = null; 
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			docBuildFactory = DocumentBuilderFactory.newInstance(); 
			docBuilder = docBuildFactory.newDocumentBuilder();
			doc = docBuilder.parse(inStream);
			
			Element locElement = doc.getElementById("location"); 
			String location = locElement.getNodeValue();
			if ( location == null ) {
				throw new ServiceResponseProcessException("Location information in the source xml document is null.");
			}
			
			String[] locationDetails = location.split(" ");
			if ( locationDetails.length == 0 ) {
				throw new ServiceResponseProcessException("No information in the location xml node was found; expected at least one item -- city.");
			}
			if ( locationDetails.length > 2 ){
				throw new ServiceResponseProcessException("Unexpected information in the location xml node; i.e. found more spaces than expected.");
			}
			
			// Of primary importance, the city name.
			mobileDetailReturn.setCityName( locationDetails[0] );

			// Capture the province information if the service provided it.
			if ( locationDetails.length == 2 ) {
				mobileDetailReturn.setProvinceName( locationDetails[1] );
			}

		}
		catch ( ParserConfigurationException ex ) {
			throw new ServiceResponseProcessException("Reader failded to initialize the parser that would read the source input xml document.",  ex);
		}
		catch ( IOException ex ) {
			throw new ServiceResponseProcessException("Reader failded to initialize with or read the source input stream.",  ex);
		} 
		catch (SAXException ex) {
			throw new ServiceResponseProcessException("Reader failed to parse the input source xml document.", ex);
		}
	}
}
