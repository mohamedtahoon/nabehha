package userservice;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.jersey.api.Responses;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Aya
 */
public class RegisterationFailedException extends WebApplicationException {

    public RegisterationFailedException() {
        super(Responses.notFound().build());
    }

    /**
     * Create a HTTP 409 (CONFLICT) exception.
     *
     * @param message the String that is the entity of the 404 response.
     */
    public RegisterationFailedException(String message) {
        super(Response.status(Responses.CONFLICT).
                entity(message).type("text/plain").build());
    }
}
