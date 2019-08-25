package ru.ath.asu.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * A resource of message.
 */
@Path("/service")
public class TicketsRest {

    private static final Logger log = LoggerFactory.getLogger(TicketsRest.class);


    @POST
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/createticket")
    public Response createIssueFromTicket(String inputJson)
    {
        log.warn("============= in ws ");
        log.warn(inputJson);

        return Response.ok("[]").build();
    }
}