package ru.ath.asu.tickets.orders;

import com.atlassian.jira.component.ComponentAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import webwork.action.ActionContext;

import java.util.Map;

public class OrdersAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(OrdersAction.class);
    private String ordersStatus;
    private String baseurl = ComponentAccessor.getApplicationProperties().getString("jira.baseurl");

    private String department;
    private String fio;

    @Override
    public String execute() throws Exception {

        String param = (String) getHttpSession().getAttribute("fio");
        if (param != null) {
            this.fio = param;
        } else {
            this.fio = "";
        }

        param = (String) getHttpSession().getAttribute("department");
        if (param != null) {
            this.department = param;
        } else {
            this.department = "";
        }

        return super.execute(); //returns SUCCESS
    }


    public String doMenu() {
//        log.warn(" ===== menu");

        // можно посмотреть переменные сессии
//        Map session = ActionContext.getSession();
//        log.warn(session.toString());
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
