package ru.ath.asu.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class ordersAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(ordersAction.class);

    @Override
    public String execute() throws Exception {

        return super.execute(); //returns SUCCESS
    }

    public String doNewTicket() throws Exception {
        return "ticketnew";
    }

    public String doActiveTicket() throws Exception {
        return "ticketactive";
    }

}
