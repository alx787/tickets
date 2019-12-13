package ru.ath.asu.tickets.rest;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.attachment.CreateAttachmentParamsBean;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;

//import com.atlassian.jira.jql.builder.DefaultJqlClauseBuilder;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.jira.web.util.AttachmentException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.multipart.FilePart;
import com.atlassian.plugins.rest.common.multipart.MultipartFormParam;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.jira.project.Project;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.query.Query;
import com.atlassian.query.order.SortOrder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ath.asu.tickets.auth.AuthContextConfiguration;
import ru.ath.asu.tickets.auth.AuthTools;
import ru.ath.asu.tickets.auth.UserInfo;
import ru.ath.asu.tickets.settings.PluginSettingsServiceTickets;
import ru.ath.asu.tickets.settings.PluginSettingsServiceTools;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * A resource of message.
 */
@Path("/service")
public class TicketsRest {

    private static final Logger log = LoggerFactory.getLogger(TicketsRest.class);
    private final PluginSettingsServiceTickets pluginSettingService;

    @ComponentImport
    private final IssueManager issueManager;

    @Inject
    public TicketsRest(PluginSettingsServiceTickets pluginSettingService, IssueManager issueManager) {
        this.pluginSettingService = pluginSettingService;
        this.issueManager = issueManager;
    }

    @POST
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Path("/createticket")
    public Response createIssueFromTicket(@Context HttpServletRequest request,
                                          @MultipartFormParam("ticket-theme") FilePart ticketTheme,
                                          @MultipartFormParam("ticket-text") FilePart ticketText,
                                          @MultipartFormParam("ticket-username") FilePart ticketUserName,
                                          @MultipartFormParam("ticket-useremail") FilePart ticketUserEmail,
                                          @MultipartFormParam("ticket-userdepart") FilePart ticketUserDepart,
                                          @MultipartFormParam("ticket-file-upload") List<FilePart> filePartList)

    {


//        String sessUser = "";
//        String sessToken = "";
//
//
//        if ((session != null) && (!session.isNew())) {
//            sessUser = (String) session.getAttribute("user");
//            sessToken = (String) session.getAttribute("token");
//
//            log.warn(" ======== ");
//            log.warn(" sess user from rest:  " + sessUser);
//            log.warn(" sess token from rest: " + sessToken);
//        } else {
//            log.warn(" sess user from rest:  not found");
//
//        }
//
//
//        UserInfo userInfo = AuthTools.authenticate(sessUser, sessToken);

        UserInfo userInfo = AuthTools.authenticateFromSession(request.getSession(false));


        String cfg = pluginSettingService.getConfigJson();

        if ((cfg == null) || (cfg.isEmpty())) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("setup error").build();
        }

//        log.warn(cfg);

//        String projectKey = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "projectKey");



                // -- план --
        // + 1 получить проект
        // + 2 получить тему и текст
        // + 3 получить вложения (будем получать после создания задачи и прикреплять по одному)
        // + определится с именем автора
        // + определится с именем исполнителя
        // + 4 создать задачу
        // 5 вернуть идентификатор задачи


        // 1 получить проект
        String projectKey = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"projectKey");
        Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
//        Project project = projectManager.getProjectObjByKey("ZVK");

        // 2 получить тему и текст
//        String tTheme = ticketTheme.getValue();
//        String tText = ticketText.getValue();
        String tTheme = "";
        String tText = "";
        String tUserName = "";
        String tUserEmail = "";
        String tUserDepart = "";

        InputStream is = null;
        StringWriter writer = new StringWriter();

        try {
            // можно было так
//            String theString = IOUtils.toString(inputStream, encoding);

            is = ticketTheme.getInputStream();
            IOUtils.copy(is, writer, "UTF-8");
            tTheme = writer.toString();

            writer.getBuffer().setLength(0);
            writer.getBuffer().trimToSize();

            is = ticketText.getInputStream();
            IOUtils.copy(is, writer, "UTF-8");
            tText = writer.toString();

            writer.getBuffer().setLength(0);
            writer.getBuffer().trimToSize();

            is = ticketUserName.getInputStream();
            IOUtils.copy(is, writer, "UTF-8");
            tUserName = writer.toString();

            writer.getBuffer().setLength(0);
            writer.getBuffer().trimToSize();

            is = ticketUserEmail.getInputStream();
            IOUtils.copy(is, writer, "UTF-8");
            tUserEmail = writer.toString();

            writer.getBuffer().setLength(0);
            writer.getBuffer().trimToSize();

            is = ticketUserDepart.getInputStream();
            IOUtils.copy(is, writer, "UTF-8");
            tUserDepart = writer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }


        // определится с именем автора
        // имя автора будет пока не использовано, в качестве автора будет имя админской учетки
//        String authorEmail = "admin@admin.com";
        String authorUserName = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"reporterDefault");
        ApplicationUser authorUser = ComponentAccessor.getUserManager().getUserByName(authorUserName);
//        ApplicationUser authorUser = userManager.getUserByName("authorUserName");

        // определится с именем исполнителя
        // имя исполнителя всегда админ или ответственный сотрудник - его надо будет указать при настройке
        ApplicationUser assignUser = ComponentAccessor.getUserManager().getUserByName(authorUserName);

//        ApplicationUser assignUser = userManager.getUserByName("authorUserName");

        // 4 создать задачу
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



        // значения дополнительных полей - надо будет обязательно проверку
        String fieldId;
        fieldId = "customfield_" + PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"useremailFieldId");
        issueInputParameters.addCustomFieldValue(fieldId, userInfo.getEmail());

        fieldId = "customfield_" + PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"userdepartFieldId");
        issueInputParameters.addCustomFieldValue(fieldId, userInfo.getDepartment());

        fieldId = "customfield_" + PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"usernameFieldId");
        issueInputParameters.addCustomFieldValue(fieldId, userInfo.getLogin());

        fieldId = "customfield_" + PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"userFullNameFieldId");
        issueInputParameters.addCustomFieldValue(fieldId, userInfo.getFio());


//        issueInputParameters.addCustomFieldValue("customfield_10001", tUserEmail);
//        issueInputParameters.addCustomFieldValue("customfield_10002", tUserDepart);
//        issueInputParameters.addCustomFieldValue("customfield_10000", tUserName);
//        issueInputParameters.addCustomFieldValue("customfield_10000", tFullUserName);


        JiraAuthenticationContext jAC = ComponentAccessor.getJiraAuthenticationContext();
        jAC.setLoggedInUser(authorUser);


        IssueService.CreateValidationResult createValidationResult = issueService.validateCreate(jAC.getLoggedInUser(), issueInputParameters);

        MutableIssue createdIssue = null;

        if (createValidationResult.isValid()) {
            log.error("entrou no createValidationResult");


            IssueService.IssueResult createResult = issueService.create(jAC.getLoggedInUser(), createValidationResult);
            if (!createResult.isValid()) {
                log.error("Error while creating the issue.");
            } else {
                createdIssue = createResult.getIssue();
                log.warn(" ==================== new issue  ");
                log.warn(createdIssue.toString());

            }
        } else {
            log.warn(" ==================== create result is not valid  ");

            Map<String, String> errorCollection = createValidationResult.getErrorCollection().getErrors();
            log.warn("ERROR: Validation errors:");
            for (String errorKey : errorCollection.keySet()) {
                log.warn(errorKey);
                log.warn(errorCollection.get(errorKey));
            }

        }


        JsonObject jsonObject = new JsonObject();
        if (createdIssue != null) {
            jsonObject.addProperty("issueId", String.valueOf(createdIssue.getNumber()));
            jsonObject.addProperty("message", "-");
        } else {
            jsonObject.addProperty("issueId", "");
            jsonObject.addProperty("message", "ошибка создания задачи");
        }



        // 3 получить вложения (будем получать после создания задачи и прикреплять по одному)
        AttachmentManager attachmentManager = ComponentAccessor.getAttachmentManager();

        for (FilePart filePart : filePartList) {
            // будем пропускать циклы если была ошибка
            if (createdIssue == null) {
                continue;
            }

            try {

                // запишем файл в темп
                is = filePart.getInputStream();

//                File file = new File("/tmp/" + fileName);
                File tempFile = File.createTempFile("att_load", "tickets");

                FileOutputStream outputStream = new FileOutputStream(tempFile);

                int read;
                byte[] bytes = new byte[1024];
                while ((read = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                // прицепим файл во вложение
                CreateAttachmentParamsBean.Builder builder = new CreateAttachmentParamsBean.Builder();
                builder.file(new File(tempFile.getAbsolutePath()));
                builder.filename(filePart.getName());
                // builder.contentType("image/png");
                builder.author(assignUser);
                builder.issue(createdIssue);
                builder.copySourceFile(true);

                CreateAttachmentParamsBean bean = builder.build();
                attachmentManager.createAttachment(bean);

                tempFile.delete();

//                try (InputStream inputStream = u.toURL().openStream()) {
//
//                    File file = new File(FILE_TO);
//
//                    // commons-io
//                    FileUtils.copyInputStreamToFile(inputStream, file);
//                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AttachmentException e) {
                e.printStackTrace();
            }

        }

        Gson gsonToStr = new Gson();
        return Response.ok(gsonToStr.toJson(jsonObject)).build();
//        return Response.ok("[]").build();
    }


    // http://localhost:2990/jira/rest/exploretickets/1.0/service/gettickets/{status}/{page}/{datefirst}/{datelast}/{issuenum}

    // параметры

//    status - статус задачи open или done
//    page - номер страницы
//    datefirst - дата начала периода в формате yyyy-MM-dd, если пустое значение то -
//    datelast - дата начала периода в формате yyyy-MM-dd, если пустое значение то -
//    issuenum - номер задачи, если пустое значение то -

    // поле status принимает два значения open и done
    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
//    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/gettickets/{status}/{page}/{datefirst}/{datelast}/{issuenum}")
    public Response getTickets(@Context HttpServletRequest request,
                               @PathParam("status") String status,
                               @PathParam("page") String sPage,
                               @PathParam("datefirst") String sDateFirst,
                               @PathParam("datelast") String sDateLast,
                               @PathParam("issuenum") String issuenum)  {


//        HttpSession session = request.getSession(false);
//
//        String sessUser = "";
//        String sessToken = "";
//
//
//        if ((session != null) && (!session.isNew())) {
//            sessUser = (String) session.getAttribute("user");
//            sessToken = (String) session.getAttribute("token");
//
//            log.warn(" ======== ");
//            log.warn(" sess user from rest:  " + sessUser);
//            log.warn(" sess token from rest: " + sessToken);
//        } else {
//            log.warn(" sess user from rest:  not found");
//
//        }


        UserInfo userInfo = AuthTools.authenticateFromSession(request.getSession(false));

        if (userInfo.getLogin().equals("")) {
            return Response.ok("{\"status\":\"error\", \"description\":\"wrong username or password\"}").build();
        }

        log.warn("============ rest parameters ============");

        log.warn("status: " + status);
        log.warn("page: " + sPage);

        log.warn("datefirst: " + sDateFirst);
        log.warn("datelast: " + sDateLast);

        log.warn("issuenum: " + issuenum);

        log.warn("============ ============");

        ////////////////////////////////////////
        // проверка статуса на заполненность
        if ((status == null) || status.equals("")) {
            return Response.ok("{\"status\":\"error\", \"description\":\"issue status empty\"}").build();
        }

        if (!(status.equals("open") || status.equals("done"))) {
            return Response.ok("{\"status\":\"error\", \"description\":\"issue status wrong\"}").build();
        }
        ////////////////////////////////////////

        ////////////////////////////////////////
        // проверка номера страницы
        try {
            Integer.parseInt(sPage);
        } catch (Exception e) {
            return Response.ok("{\"status\":\"error\", \"description\":\"wrong page number\"}").build();
        }

        int iPage = Integer.parseInt(sPage);

        if (iPage <= 0) {
            return Response.ok("{\"status\":\"error\", \"description\":\"wrong page number\"}").build();
        }
        ////////////////////////////////////////

        ////////////////////////////////////////
        // проверка даты начала и даты окончания

        // дата в формате 2019-12-03
        Date dDateFirst = null;
        Date dDateLast = null;

        if (!sDateFirst.equals("-")) {
            try {
                dDateFirst = new SimpleDateFormat("yyyy-MM-dd").parse(sDateFirst);
            } catch (Exception e) {

            }
        }

        if (!sDateLast.equals("-")) {
            try {
                // смещаем дату окончания на конец дня
                dDateLast = new SimpleDateFormat("yyyy-MM-dd").parse(sDateLast);

                Timestamp ts = new Timestamp(dDateLast.getTime());
                ts.setTime(ts.getTime() + 24 * 60 * 60 * 1000);

                dDateLast = new Date(ts.getTime());

            } catch (Exception e) {

            }
        }

        // проверка
        if (dDateFirst == null) {
            log.warn("dDateFirst: null");

        } else {
            log.warn("dDateFirst: " + dDateFirst.toString());
        }

        if (dDateLast == null) {
            log.warn("dDateLast: null");

        } else {
            log.warn("dDateLast: " + dDateLast.toString());
        }

        ////////////////////////////////////////




        String cfg = pluginSettingService.getConfigJson();

        if ((cfg == null) || (cfg.isEmpty())) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("setup error").build();
        }

        log.warn(cfg);

        String projectKey = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "projectKey");
        String usernameFieldId = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "usernameFieldId");
        String reporterName = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "reporterDefault");

        Long uFielfId = 0L;

        try {
            uFielfId = Long.valueOf(usernameFieldId);
        } catch (Exception e) {
            return Response.ok("{\"status\":\"error\", \"description\":\"bad username field id <" + usernameFieldId + ">\"}").build();
        }



        Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);

        if (project == null) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("project not found").build();
            return Response.ok("{\"status\":\"error\", \"description\":\"project with key = " + projectKey + " not found\"}").build();
        }


//        String authorUserName = "admin";
        ApplicationUser authorUser = ComponentAccessor.getUserManager().getUserByName(reporterName);

        JiraAuthenticationContext jAC = ComponentAccessor.getJiraAuthenticationContext();
        jAC.setLoggedInUser(authorUser);


        JqlQueryBuilder queryBuilder = JqlQueryBuilder.newBuilder();

        JqlClauseBuilder builder = queryBuilder.orderBy().issueKey(SortOrder.DESC).endOrderBy().where().project(project.getId()).and().customField(Long.valueOf(usernameFieldId)).like(userInfo.getLogin());

        if (status.equals("open")) {
            builder.and();
            builder.sub();
            builder.status("To Do").or().status("Open").or().status("In Progress");
            builder.endsub();
        }

        if (status.equals("done")) {
            builder.and().status("Done");
        }

        if (dDateFirst != null) {
            builder.and().created().gtEq(dDateFirst);
        }

        if (dDateLast != null) {
            builder.and().created().ltEq(dDateLast);
        }

        if (issuenum != null) {

            if (!issuenum.equals("-")) {
                try {
                    Long.valueOf(issuenum);
//                  builder.and().issue().eq(lIssueKey);
                    builder.and().issue().eq(projectKey + "-" + issuenum);
                } catch (Exception e) {

                }
            }
        }

        builder.endWhere();


        Query query = builder.buildQuery();

//        Query query = builder.where().project(projectKey).and().created().gt(dDateFirst).or().created().lt(dDateLast).and() buildQuery();



//        SearchService searchService = ComponentAccessor


        SearchResults results = null;

        try {
            // используется для ограничения количества выводимых страниц
            // параметры new PagerFilter(int start, int max)
            // start - номер позиции с которой будет вывод в результате
            // max - количество выводимых позиций
            PagerFilter pagerFilter = new PagerFilter((iPage - 1) * 10, 10);
//            PagerFilter pagerFilter = new PagerFilter(0, 2);

            results = ComponentAccessor.getComponentOfType(SearchService.class).search(jAC.getLoggedInUser(), query, pagerFilter);

        } catch (SearchException e) {
            e.printStackTrace();
            return Response.ok("{\"status\":\"error\", \"description\":\"exception in query\"}").build();
        }

        log.warn(" ======= JQL query =======");
//        log.warn(((DefaultJqlClauseBuilder) queryBuilder.jqlClauseBuilder).builder.toString());
        log.warn(query.toString());

        log.warn(" ======= issues list from rest =======");

        log.warn(" ======= total pages =======");
        log.warn(String.valueOf(results.getPages().size()));
        log.warn(String.valueOf(results.getTotal()));


        ///////////////////////////////////////////////
        // сформируем ответ в виде json
        JsonObject jsonRestAnswer = new JsonObject();
        jsonRestAnswer.addProperty("status", "ok");
        jsonRestAnswer.addProperty("total", results.getTotal());
        jsonRestAnswer.addProperty("pages", results.getPages().size());
        jsonRestAnswer.addProperty("currPage", iPage);

        JsonArray jsonIssuesArray = new JsonArray();

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        for (Issue oneIssue : results.getIssues()) {
            log.warn("issue: " + oneIssue.getKey() + " - " + oneIssue.getSummary());

            JsonObject jsonIssue = new JsonObject();

            if (oneIssue.getCreated() != null) {
                jsonIssue.addProperty("createdate", dateFormat.format(oneIssue.getCreated()));
            } else {
                jsonIssue.addProperty("createdate", "");
            }

            jsonIssue.addProperty("number", oneIssue.getNumber());
            jsonIssue.addProperty("summary", oneIssue.getSummary());
            jsonIssue.addProperty("description", oneIssue.getDescription());
            jsonIssue.addProperty("status", oneIssue.getStatus().getName());

            if (oneIssue.getDueDate() != null) {
                jsonIssue.addProperty("duedate", dateFormat.format(oneIssue.getDueDate()));
            } else {
                jsonIssue.addProperty("duedate", "-");
            }


            jsonIssuesArray.add(jsonIssue);
        }

        jsonRestAnswer.add("issues", jsonIssuesArray);

        Gson gson = new Gson();


        return Response.ok(gson.toJson(jsonRestAnswer)).build();

    }



    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getticketinfo/{issuenum}")
    public Response getTicketInfo(@Context HttpServletRequest request, @PathParam("issuenum") String issuenum)  {


        UserInfo userInfo = AuthTools.authenticateFromSession(request.getSession(false));

        if (userInfo.getLogin().equals("")) {
            return Response.ok("{\"status\":\"error\", \"description\":\"wrong username or password\"}").build();
        }


        if ((issuenum == null) || (issuenum.equals(""))) {
            return Response.ok("[]").build();
        }

        String cfg = pluginSettingService.getConfigJson();
        String projectKey = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg,"projectKey");

        MutableIssue mutableIssue = issueManager.getIssueObject(projectKey + "-" + issuenum);

        if (mutableIssue == null) {
            return Response.ok("{\"status\":\"error\", \"description\":\"issue not found\"}").build();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        JsonObject jsonRestAnswer = new JsonObject();
        jsonRestAnswer.addProperty("status", "ok");

        jsonRestAnswer.addProperty("number", mutableIssue.getNumber());
        jsonRestAnswer.addProperty("created", dateFormat.format(mutableIssue.getCreated()));

        if (mutableIssue.getDueDate() == null) {
            jsonRestAnswer.addProperty("duedate", "");
        } else {
            jsonRestAnswer.addProperty("duedate", dateFormat.format(mutableIssue.getDueDate()));
        }

        jsonRestAnswer.addProperty("summary", mutableIssue.getSummary());
        jsonRestAnswer.addProperty("description", mutableIssue.getDescription());

        Gson gson = new Gson();

        return Response.ok(gson.toJson(jsonRestAnswer)).build();

    }


}