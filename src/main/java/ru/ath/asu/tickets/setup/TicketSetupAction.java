package ru.ath.asu.tickets.setup;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.APKeys;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.ath.asu.tickets.settings.PluginSettingsServiceTickets;
import ru.ath.asu.tickets.settings.PluginSettingsServiceTools;

import javax.inject.Inject;

public class TicketSetupAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(TicketSetupAction.class);

    private final PluginSettingsServiceTickets pluginSettingService;

    // содержимое json для настроек
    // ProjectCode - код проекта
    // DefailtReporterName - автор проекта по умолчанию (пользователь jira)
    // FieldIdUserName - логин пользователя автора заявки
    // FieldIdUserFullName - фио пользователя автора заявки
    // FieldIdUserEmail - эл почта пользователя автора заявки
    // FieldIdUserDepart - подразделение пользователя автора заявки

    private String projectKey;
    private String reporterDefault;
    private String usernameFieldId;
    private String userFullNameFieldId;
    private String useremailFieldId;
    private String userdepartFieldId;
    private String portalUrl;



    @Inject
    public TicketSetupAction(PluginSettingsServiceTickets pluginSettingService) {
        this.pluginSettingService = pluginSettingService;
    }

    @Override
    public String execute() throws Exception {

        return super.execute(); //returns SUCCESS
    }

    @Override
    public String doDefault() throws Exception {

        projectKey = "";
        reporterDefault = "";
        usernameFieldId = "";
        userFullNameFieldId = "";
        useremailFieldId = "";
        userdepartFieldId = "";
        portalUrl = "";

        String cfg = pluginSettingService.getConfigJson();

         if (cfg == null) {
            return SUCCESS;
        }

        if (cfg.isEmpty()) {
            return SUCCESS;
        }

        projectKey = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "projectKey");
        reporterDefault = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "reporterDefault");
        usernameFieldId = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "usernameFieldId");
        userFullNameFieldId = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "userFullNameFieldId");
        useremailFieldId = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "useremailFieldId");
        userdepartFieldId = PluginSettingsServiceTools.getValueFromSettingsCfg(cfg, "userdepartFieldId");
        portalUrl = ComponentAccessor.getApplicationProperties().getString(APKeys.JIRA_BASEURL) + "/secure/portalAction!default.jspa";

//        log.warn("======= " + portalUrl);

        return SUCCESS;
    }

    public String doSave() throws Exception {
        JsonObject params = new JsonObject();

        if (projectKey != null) {
            params.addProperty("projectKey", projectKey);
        } else {
            params.addProperty("projectKey", "");
        };


        if (reporterDefault != null) {
            params.addProperty("reporterDefault", reporterDefault);
        } else {
            params.addProperty("reporterDefault", "");
        };

        if (usernameFieldId != null) {
            params.addProperty("usernameFieldId", usernameFieldId);
        } else {
            params.addProperty("usernameFieldId", "");
        };

        if (userFullNameFieldId != null) {
            params.addProperty("userFullNameFieldId", userFullNameFieldId);
        } else {
            params.addProperty("userFullNameFieldId", "");
        };

        if (useremailFieldId != null) {
            params.addProperty("useremailFieldId", useremailFieldId);
        } else {
            params.addProperty("useremailFieldId", "");
        };

        if (userdepartFieldId != null) {
            params.addProperty("userdepartFieldId", userdepartFieldId);
        } else {
            params.addProperty("userdepartFieldId", "");
        };

        pluginSettingService.setConfigJson(params.toString());

        return SUCCESS;
    }


    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getReporterDefault() {
        return reporterDefault;
    }

    public void setReporterDefault(String reporterDefault) {
        this.reporterDefault = reporterDefault;
    }

    public String getUsernameFieldId() {
        return usernameFieldId;
    }

    public void setUsernameFieldId(String usernameFieldId) {
        this.usernameFieldId = usernameFieldId;
    }

    public String getUseremailFieldId() {
        return useremailFieldId;
    }

    public void setUseremailFieldId(String useremailFieldId) {
        this.useremailFieldId = useremailFieldId;
    }

    public String getUserdepartFieldId() {
        return userdepartFieldId;
    }

    public void setUserdepartFieldId(String userdepartFieldId) {
        this.userdepartFieldId = userdepartFieldId;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public String getUserFullNameFieldId() {
        return userFullNameFieldId;
    }

    public void setUserFullNameFieldId(String userFullNameFieldId) {
        this.userFullNameFieldId = userFullNameFieldId;
    }
}
