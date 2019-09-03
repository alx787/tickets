package ru.ath.asu.tickets.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class OrdersAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(OrdersAction.class);

    @Override
    public String execute() throws Exception {

        return super.execute(); //returns SUCCESS
    }


    public String doMenu() {
        log.warn(" ===== menu");
        return "menu";
    }

    public String doNewTicket() throws Exception {
        return "ticketnew";
    }

    public String doActiveTicket() throws Exception {
        return "ticketactive";
    }

}
