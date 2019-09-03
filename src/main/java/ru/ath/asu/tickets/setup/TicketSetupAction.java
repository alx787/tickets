package ru.ath.asu.tickets.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class TicketSetupAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(TicketSetupAction.class);

    // содержимое json для настроек
    // ProjectCode
    // DefaultAuthorName
    // DefailtReporterName
    // FieldIdUserName
    // FieldIdUserEmail
    // FieldIdUserDepart

    @Override
    public String doDefault() throws Exception {
        return SUCCESS;
    }


    @Override
    public String execute() throws Exception {

        return super.execute(); //returns SUCCESS
    }
}
