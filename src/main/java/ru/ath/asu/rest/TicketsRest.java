package ru.ath.asu.rest;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugins.rest.common.multipart.FilePart;
import com.atlassian.plugins.rest.common.multipart.MultipartFormParam;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.jira.project.Project;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


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
                                          @MultipartFormParam("ticket-username") FilePart ticketUserName,
                                          @MultipartFormParam("ticket-useremail") FilePart ticketUserEmail,
                                          @MultipartFormParam("ticket-userdepart") FilePart ticketUserDepart,
                                          @MultipartFormParam("ticket-file-upload") List<FilePart> filePartList)

    {





        log.warn("============= in ws ");
        log.warn(ticketTheme.getValue());
        log.warn(ticketText.getValue());


        // -- план --
        // + 1 получить проект
        // + 2 получить тему и текст
        // 3 получить вложения (будем получать после создания задачи и прикреплять по одному)
        // + определится с именем автора
        // + определится с именем исполнителя
        // + 4 создать задачу
        // 5 вернуть идентификатор задачи


        // 1 получить проект
        Project project = ComponentAccessor.getProjectManager().getProjectObjByKey("ZVK");
//        Project project = projectManager.getProjectObjByKey("ZVK");

        // 2 получить тему и текст
        String tTheme = ticketTheme.getValue();
        String tText = ticketText.getValue();

        // определится с именем автора
        // имя автора будет пока не использовано, в качестве автора будет имя админской учетки
        String authorEmail = "admin@admin.com";
        String authorUserName = "admin";
        ApplicationUser authorUser = ComponentAccessor.getUserManager().getUserByName(authorUserName);
//        ApplicationUser authorUser = userManager.getUserByName("authorUserName");

        // определится с именем исполнителя
        // имя исполнителя всегда админ или ответственный сотрудник - его надо будет указать при настройке
        ApplicationUser assignUser = ComponentAccessor.getUserManager().getUserByName(authorUserName);


//        Issue issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey("sdfsdf");
//        issue.getReporter(). get getReporterId();


//        ApplicationUser assignUser = userManager.getUserByName("authorUserName");

        // 4 создать задачу
//        MutableIssue newIssue = ComponentAccessor.getIssueFactory().getIssue();
////        MutableIssue newIssue = issueFactory.getIssue();
//        newIssue.setProjectId(project.getId());
//        newIssue.setIssueTypeId("10000");
//        newIssue.setSummary(tTheme);
//        newIssue.setDescription(tText);
//        newIssue.setReporter(authorUser);
//        newIssue.setAssignee(authorUser);
//        newIssue.setPriorityId("3");
//        newIssue.setStatusId("10000");
//
//        Issue createdIssue = null;
//        try {
//            createdIssue = ComponentAccessor.getIssueManager().createIssueObject(assignUser, newIssue);
////            createdIssue = issueManager.createIssueObject(assignUser, newIssue);
//        } catch (CreateException e) {
//            e.printStackTrace();
//        }


//        JsonObject jsonObject = new JsonObject();





        IssueService issueService = ComponentAccessor.getIssueService();
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.setSkipScreenCheck(true);

        issueInputParameters.setProjectId(project.getId());
        issueInputParameters.setIssueTypeId("10000");
        issueInputParameters.setStatusId("10000");

        issueInputParameters.setSummary(tTheme);

//    issueInputParameters.setDescription(issue.getDescription())
        issueInputParameters.setDescription(tText);

        issueInputParameters.setPriorityId("3");
        issueInputParameters.setReporterId(authorUser.getKey());
        issueInputParameters.setAssigneeId(assignUser.getKey());


        JiraAuthenticationContext jAC = ComponentAccessor.getJiraAuthenticationContext();
        jAC.setLoggedInUser(authorUser);


        IssueService.CreateValidationResult createValidationResult = issueService.validateCreate(jAC.getLoggedInUser(), issueInputParameters);

        MutableIssue createdIssue = null;

//        if (createValidationResult.isValid()) {
//            log.error("entrou no createValidationResult");
//
//
//            IssueService.IssueResult createResult = issueService.create(jAC.getLoggedInUser(), createValidationResult);
//            if (!createResult.isValid()) {
//                log.error("Error while creating the issue.");
//            } else {
//                createdIssue = createResult.getIssue();
//                log.warn(" ==================== new issue  ");
//                log.warn(createdIssue.toString());
//
//            }
//        } else {
//            log.warn(" ==================== create result is not valid  ");
//
//            Map<String, String> errorCollection = createValidationResult.getErrorCollection().getErrors();
//            log.warn("ERROR: Validation errors:");
//            for (String errorKey : errorCollection.keySet()) {
//                log.warn(errorKey);
//                log.warn(errorCollection.get(errorKey));
//            }
//
//        }


        JsonObject jsonObject = new JsonObject();
        if (createdIssue != null) {
            jsonObject.addProperty("issueId", String.valueOf(createdIssue.getId()));
            jsonObject.addProperty("message", "-");
        } else {
            jsonObject.addProperty("issueId", "");
            jsonObject.addProperty("message", "ошибка создания задачи");
        }




//        for (FilePart filePart : filePartList) {
//            String fileName = filePart.getName();
//            try {
//                InputStream is = filePart.getInputStream();
//
//                File file = new File("/tmp/" + fileName);
//
//                try (FileOutputStream outputStream = new FileOutputStream(file)) {
//
//                    int read;
//                    byte[] bytes = new byte[1024];
//
//                    while ((read = is.read(bytes)) != -1) {
//                        outputStream.write(bytes, 0, read);
//                    }
//
//                }
//
//
////                try (InputStream inputStream = u.toURL().openStream()) {
////
////                    File file = new File(FILE_TO);
////
////                    // commons-io
////                    FileUtils.copyInputStreamToFile(inputStream, file);
////                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            log.warn(fileName);
//
//        }

        Gson gsonToStr = new Gson();
        return Response.ok(gsonToStr.toJson(jsonObject)).build();

//        return Response.ok("[]").build();
    }
}