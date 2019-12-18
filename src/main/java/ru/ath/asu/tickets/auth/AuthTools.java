package ru.ath.asu.tickets.auth;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Random;

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


    public static String generateToken() {
        byte[] array = new byte[10]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}