package org.irmacard.web.restapi;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.irmacard.credentials.idemix.util.CredentialInformation;
import org.irmacard.credentials.info.DescriptionStore;
/*
import org.irmacard.web.restapi.resources.NYTimesVerificationResource;
import org.irmacard.web.restapi.resources.SpuitenEnSlikkenVerificationResource;
import org.irmacard.web.restapi.resources.StudentCardIssueResource;
import org.irmacard.web.restapi.resources.StudentCardVerificationResource;
import org.irmacard.web.restapi.resources.irmaTube.IRMATubeRegistrationIssueResource;
import org.irmacard.web.restapi.resources.irmaTube.IRMATubeRegistrationVerificationResource;
import org.irmacard.web.restapi.resources.irmaTube.IRMATubeVerificationResource;
import org.irmacard.web.restapi.resources.irmaWiki.IRMAWikiRegistrationIssueResource;
import org.irmacard.web.restapi.resources.irmaWiki.IRMAWikiRegistrationVerificationResource;
import org.irmacard.web.restapi.resources.irmaWiki.IRMAWikiVerificationResource;
import org.irmacard.web.restapi.resources.surfnetCoupons.SurfnetVoucherVerificationResource;
*/
import org.irmacard.web.restapi.resources.thalia.ThaliaLoggedInResource;
import org.irmacard.web.restapi.resources.thalia.ThaliaVerificationResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class IRMAProtocolApplication extends Application {
	
	public static Context context = null;
	
	/**
	 * Starting point for the IRMA REST interface for smartcard protocols. Contains
	 * the URL router.
	 * 
	 * @author Maarten Everts
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		if (IRMAProtocolApplication.context == null) {
			IRMAProtocolApplication.context = getContext().createChildContext();
		}
		Router router = new Router(context);
		assert router.getContext() != null;
		
		URI CORE_LOCATION;
		try {
			ClassLoader loader = IRMAProtocolApplication.class.getClassLoader();

			URL resource = loader.getResource("irma_configuration/");
			CORE_LOCATION = resource.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
		CredentialInformation.setCoreLocation(CORE_LOCATION);
		DescriptionStore.setCoreLocation(CORE_LOCATION);

		/*
		router.attach("/verification/SpuitenEnSlikken", SpuitenEnSlikkenVerificationResource.class);
		router.attach("/verification/SpuitenEnSlikken/{id}/{step}", SpuitenEnSlikkenVerificationResource.class);

		router.attach("/verification/NYTimes", NYTimesVerificationResource.class);
		router.attach("/verification/NYTimes/{id}/{step}", NYTimesVerificationResource.class);
		*/
		
		router.attach("/verification/Thalia", ThaliaVerificationResource.class);
		router.attach("/verification/Thalia/{id}/getUserName", ThaliaLoggedInResource.class);
		router.attach("/verification/Thalia/{id}/{step}", ThaliaVerificationResource.class);
		
		/*
		router.attach("/verification/IRMATube/{age}", IRMATubeVerificationResource.class);
		router.attach("/verification/IRMATube/{age}/{id}/{step}", IRMATubeVerificationResource.class);

		router.attach("/verification/SurfnetVoucher", SurfnetVoucherVerificationResource.class);
		router.attach("/verification/SurfnetVoucher/{id}/{step}", SurfnetVoucherVerificationResource.class);

		router.attach("/verification/IRMAWiki", IRMAWikiVerificationResource.class);
		router.attach("/verification/IRMAWiki/{id}/{step}", IRMAWikiVerificationResource.class);
		router.attach("/verification/IRMAWikiRegistration", IRMAWikiRegistrationVerificationResource.class);
		router.attach("/verification/IRMAWikiRegistration/{id}/{step}", IRMAWikiRegistrationVerificationResource.class);
		router.attach("/issue/IRMAWikiRegistration/{id}", IRMAWikiRegistrationIssueResource.class);
		router.attach("/issue/IRMAWikiRegistration/{id}/{cred}/{step}", IRMAWikiRegistrationIssueResource.class);

		router.attach("/verification/StudentCard", StudentCardVerificationResource.class);
		router.attach("/verification/StudentCard/{id}/{step}", StudentCardVerificationResource.class);
		router.attach("/issue/StudentCard/{id}", StudentCardIssueResource.class);
		router.attach("/issue/StudentCard/{id}/{cred}/{step}", StudentCardIssueResource.class);

		router.attach("/verification/IRMATubeRegistration", IRMATubeRegistrationVerificationResource.class);
		router.attach("/verification/IRMATubeRegistration/{id}/{step}", IRMATubeRegistrationVerificationResource.class);
		router.attach("/issue/IRMATubeRegistration/{id}", IRMATubeRegistrationIssueResource.class);
		router.attach("/issue/IRMATubeRegistration/{id}/{cred}/{step}", IRMATubeRegistrationIssueResource.class);
		*/

		assert router.getContext() != null;
		assert router.getContext().getServerDispatcher().getContext() != null;
		return router;
	}   
}
