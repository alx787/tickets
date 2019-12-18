package ru.ath.asu.tickets.portal;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.ath.asu.tickets.aousers.TicketUser;
import ru.ath.asu.tickets.aousers.TicketUserDao;
import ru.ath.asu.tickets.auth.AuthUserInfo;
//import org.springframework.beans.factory.annotation.Qualifier;

//import javax.inject.Inject;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//@Named
public class PortalAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(PortalAction.class);

    private String userName;
    private final TicketUserDao ticketUserDao;

//    private AuthUserInfo authUserInfo;


//    @Inject
    public PortalAction(TicketUserDao ticketUserDao) {
        this.ticketUserDao = ticketUserDao;
    }

    @Override
    public String execute() throws Exception {

//        HttpServletRequest req = getHttpRequest();
//        HttpSession session = req.getSession();
//        String sessUser = (String)session.getAttribute("user");
//        String sessToken = (String)session.getAttribute("token");
//
//        log.warn(" ======== ");
//        log.warn(" sess user:  " + sessUser);
//        log.warn(" sess token: " + sessToken);

        return super.execute();
        //return "login";
    }

    public String doDefault() throws Exception {

        // используем метод для разрегистрации пользователя
        HttpServletRequest req = getHttpRequest();
        HttpSession session = req.getSession();

        session.invalidate();


        this.userName = "";
        return "login";
    }

    public String doAuth() throws Exception {

        String loginName = getHttpRequest().getParameter("login-name");
        String loginPass = getHttpRequest().getParameter("login-pass");

        if (loginName == null) {
            loginName = "";
        }

        this.userName = loginName;


        // проверка логина пароля
        if (checkPass(loginName, loginPass)) {

            HttpServletRequest req = getHttpRequest();
            HttpSession session = req.getSession();
            session.setAttribute("user", loginName);
            session.setAttribute("token", loginPass);


            log.warn(" ===== авторизовался");
            return getRedirect("ordersAction!menu.jspa");
//            return "menu";
        }

        log.warn(" ===== не авторизовался");
        return "login";
    }




    // здесь проверка логина и пароля
    private boolean checkPass(String login, String password) {
        String uName = "alx";
        String uPass = "123";


        // проверка дао
        TicketUser ticketUser = ticketUserDao.create("alxlogin", "alxusername", "alxdepart", "alxpassword");


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
