package org.irmacard.web.restapi.resources.thalia;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


/**
 * Fetch /protocols/verification/Thalia/<ID>/getUserName and return the User Name
 * 
 * @author Thom Wiggers <thom@thomwiggers.nl>
 *
 */
public class ThaliaLoggedInResource extends ServerResource {
	
	@SuppressWarnings("unchecked")
	@Get
    public Representation handleGet() {
        String id = (String) getRequestAttributes().get("id");
        
        if (id == null) {
            return null;
        }
        
        Map<String, Object> data = (Map<String, Object>) ThaliaVerificationResource.getThaliaUserStore(getContext()).get(id);
        
        if (data == null) {        	
        	return new StringRepresentation("{\"status\":\"error\", \"message\":\"Unknown or already used id\"}");
        }
        
        ThaliaVerificationResource.getThaliaUserStore(getContext()).remove(id);
        
        ZonedDateTime verifyDate = (ZonedDateTime) data.get("date");
        
        if (verifyDate.isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1))) {        	
        	return new StringRepresentation("{\"status\":\"error\", \"message\":\"Too long ago\"}");
        }
        
        String username = (String) data.get("username");
        
        return new StringRepresentation("{\"status\":\"ok\", \"username\": \""+ username +"\"}");
    }
}