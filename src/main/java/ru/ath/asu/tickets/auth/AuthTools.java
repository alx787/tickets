package ru.ath.asu.tickets.auth;

import ru.ath.asu.tickets.aousers.TicketUser;
import ru.ath.asu.tickets.aousers.TicketUserDao;

import javax.servlet.http.HttpSession;

import java.security.SecureRandom;

public class AuthTools {

    public static UserInfo authenticateFromUsernamePassword(TicketUserDao dao, String userId, String token) {

        UserInfo userInfo = new UserInfo();

        if (userId == null || userId.equals("") || token == null || token.equals("")) {
            return userInfo;
        }

        int id = 0;
        try {
            id = Integer.valueOf(userId);
        } catch (Exception e) {
            return userInfo;
        }

        // запрос в AO
        TicketUser ticketUser = dao.findById(id);

        if (ticketUser == null) {
            return userInfo;
        }

        if (!ticketUser.getToken().equals(token)) {
            return userInfo;
        }

        // получаем сущность с данными пользователя
        userInfo = getUserInfoFromTicketUser(ticketUser);

        return userInfo;
    }




    public static UserInfo authenticateFromSession(TicketUserDao dao, HttpSession session) {

        String sessUser = "";
        String sessToken = "";

        if ((session != null) && (!session.isNew())) {
            sessUser = (String) session.getAttribute("user");
            sessToken = (String) session.getAttribute("token");

            return authenticateFromUsernamePassword(dao, sessUser, sessToken);
        }

        return new UserInfo();
    }



    public static UserInfo getUserInfoFromTicketUser(TicketUser ticketUser) {
        if (ticketUser != null) {
            return new UserInfo(String.valueOf(ticketUser.getID()), ticketUser.getEmail(), ticketUser.getLogin(), ticketUser.getUserName(), ticketUser.getDepart(), "", ticketUser.getToken());
        }
        return null;
    }


    public static String generateToken() {
//        byte[] array = new byte[10]; // length is bounded by 7
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
//
        SecureRandom random = new SecureRandom();

        long longToken = Math.abs( random.nextLong() );
        String randomString = Long.toString( longToken, 16 );
        return randomString;
    }
}