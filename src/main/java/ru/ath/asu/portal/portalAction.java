package ru.ath.asu.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;


public class portalAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(portalAction.class);

    private String userName;

    @Override
    public String execute() throws Exception {

        //return super.execute();
        //log.warn(" ==== this is log");
        super.execute();
        return "login";
    }

    public String doDefault() throws Exception {
        this.userName = "";
        return "login";
    }

    public String doAuth() {

        String loginName = getHttpRequest().getParameter("login-name");
        String loginPass = getHttpRequest().getParameter("login-pass");

        if (loginName == null) {
            loginName = "";
        }

        this.userName = loginName;


        // проверка логина пароля
        if (checkPass(loginName, loginPass)) {
            log.warn(" ===== авторизовался");
            return "login";
        }

        log.warn(" ===== не авторизовался");
        return "login";
    }

    // здесь проверка логина и пароля
    private boolean checkPass(String login, String password) {
        String uName = "alx";
        String uPass = "123";

        if (uName.equals(login) && uPass.equals(password)) {
            return true;
        }

        return false;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
