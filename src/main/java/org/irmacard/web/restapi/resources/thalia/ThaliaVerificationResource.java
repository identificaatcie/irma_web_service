package org.irmacard.web.restapi.resources.thalia;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.irmacard.credentials.Attributes;
import org.irmacard.credentials.info.DescriptionStore;
import org.irmacard.credentials.info.InfoException;
import org.irmacard.credentials.info.VerificationDescription;
import org.irmacard.web.restapi.resources.VerificationBaseResource;
import org.irmacard.web.restapi.util.ProtocolStep;
import org.restlet.Context;

/**
 * Verify the Thalia root credential
 * 
 * Based on IRMATube code
 * 
 * @author Thom Wiggers
 *
 */
public class ThaliaVerificationResource extends
		VerificationBaseResource {
	final static String VERIFIER = "Thalia";
	final static String VERIFICATIONID = "rootAll";

	public static final String ATTRIBUTE_STORE_NAME = "ThaliaStore";
	VerificationDescription memberDescription;
	
	public ThaliaVerificationResource() {
		try {
			memberDescription = DescriptionStore.getInstance()
					.getVerificationDescriptionByName(VERIFIER, VERIFICATIONID);
		} catch (InfoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProtocolStep onSuccess(String id, Map<String, Attributes> attrMap) {
		ProtocolStep ps = new ProtocolStep();

		Attributes memberType = attrMap.get(VERIFICATIONID);

		if (memberType == null) {
			return ProtocolStep.newError(memberDescription.getName()
					+ " credential is invalid/expired");
		}

		if ((new String(memberType.get("userID"))).isEmpty()) {
			throw new RuntimeException("no UserID? WTF?");
		}
		
		String uid = new String(memberType.get("userID"));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("username", uid);
		data.put("date", ZonedDateTime.now(ZoneOffset.UTC));
		
		getThaliaUserStore(getContext()).put(id, data);		
		
		
		ps.protocolDone = true;
		ps.status = "success";
		ps.result = id;
		return ps;
	}

	@Override
	public List<VerificationDescription> getVerifications(String id) {
		List<VerificationDescription> result = new ArrayList<VerificationDescription>();
		result.add(memberDescription);
		return result;
	}
	
	/**
	 * The userid found in the credential is stored here.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getThaliaUserStore(Context context) {
		Map<String, Object> store = null;
		ServletContext ctxt = null;

		try {
			ctxt = (ServletContext) context.getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		// Try first context
		if(ctxt != null) {
			store = (Map<String, Object>) ctxt.getAttribute(ATTRIBUTE_STORE_NAME);
		}

		// Still no store found, create it and put it in first context.
		if( store == null) {
			System.out.println("Store not found, generating new store");
			store = new HashMap<String, Object>();
			ctxt.setAttribute(ATTRIBUTE_STORE_NAME, store);
		}

		return store;		
	}
}
