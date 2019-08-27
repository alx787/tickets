package ru.ath.asu.rest;

import com.atlassian.plugins.rest.common.multipart.FilePart;
import com.atlassian.plugins.rest.common.multipart.MultipartFormParam;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A resource of message.
 */
@Path("/service")
public class TicketsRest {

    private static final Logger log = LoggerFactory.getLogger(TicketsRest.class);


    @POST
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Path("/createticket")
    public Response createIssueFromTicket(@MultipartFormParam("ticket-theme") FilePart ticketTheme,
                                          @MultipartFormParam("ticket-text") FilePart ticketText,
                                          @MultipartFormParam("ticket-file-upload") List<FilePart> filePartList)


    {
        log.warn("============= in ws ");
        log.warn(ticketTheme.getValue());
        log.warn(ticketText.getValue());


        for (FilePart filePart : filePartList) {
            String fileName = filePart.getName();
            try {
                InputStream is = filePart.getInputStream();

                File file = new File("/tmp/" + fileName);

                try (FileOutputStream outputStream = new FileOutputStream(file)) {

                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                }



//                try (InputStream inputStream = u.toURL().openStream()) {
//
//                    File file = new File(FILE_TO);
//
//                    // commons-io
//                    FileUtils.copyInputStreamToFile(inputStream, file);
//                }





            } catch (IOException e) {
                e.printStackTrace();
            }

            log.warn(fileName);


        }

        return Response.ok("[]").build();
    }
}