package ru.ath.asu.tickets.auth;

import javax.servlet.http.HttpSession;

public class AuthTools {

    public static UserInfo authenticateFromUsernamePassword(String username, String password) {

        UserInfo userInfo = new UserInfo();

        if (username == null || password == null) {
            return userInfo;
        }

        if (username.equals("alx") && password.equals("123")) {

            userInfo.setLogin(username);
            userInfo.setFio(username + " " + username + " " + username);
            userInfo.setEmail(username + "@kiravto.ru");
            userInfo.setDepartment(username + " depart");

            return userInfo;

        }

        return userInfo;
    }


    public static UserInfo authenticateFromSession(HttpSession session) {

        String sessUser = "";
        String sessToken = "";


        if ((session != null) && (!session.isNew())) {
            sessUser = (String) session.getAttribute("user");
            sessToken = (String) session.getAttribute("token");

            return authenticateFromUsernamePassword(sessUser, sessToken);
        }

        return new UserInfo();

    }

}
