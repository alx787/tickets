package ru.ath.asu.tickets.orders;

import com.atlassian.jira.component.ComponentAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class OrdersAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(OrdersAction.class);
    private String ordersStatus;
    private String baseurl = ComponentAccessor.getApplicationProperties().getString("jira.baseurl");

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
        this.ordersStatus = "open";
        return "ticketactive";
    }

    public String doArchTicket() throws Exception {
        this.ordersStatus = "done";
        return "ticketactive";
    }

    /////////////////////////////////
    // getters and setters
    /////////////////////////////////

    public String getOrdersStatus() {
        return ordersStatus;
    }

    public void setOrdersStatus(String ordersStatus) {
        this.ordersStatus = ordersStatus;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
