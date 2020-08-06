package ru.ath.asu.tickets.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ath.asu.tickets.aousers.TicketUser;
import ru.ath.asu.tickets.aousers.TicketUserDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersRest {

    private static final Logger log = LoggerFactory.getLogger(UsersRest.class);
    private final TicketUserDao ticketUserDao;

    @Inject
    public UsersRest(TicketUserDao ticketUserDao) {
        this.ticketUserDao = ticketUserDao;
    }

    //http://localhost:2990/jira/rest/exploretickets/1.0/users/getusers

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getusers/{userid}")
    public Response getAllUsers(@PathParam("userid") String userId) {
        if ((userId == null) || userId.equals("")) {
            return Response.ok("[]").build();
        }

        if (userId.equals("all")) {
            // возвращаем всех пользователей

            TicketUser[] ticketUserAll = ticketUserDao.findByAll();

            ///////////////////////////////////////////////
            // сформируем ответ в виде json
            JsonObject jsonRestAnswer = prepareJsonObjectAnswer(ticketUserAll);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(jsonRestAnswer)).build();

        } else {
            // возвращаем одного пользователя в том же формате в виде массива, если он есть

            int intUserId = 0;
            try {
                intUserId = Integer.valueOf(userId);
            } catch (Exception e) {
                return Response.ok("{\"status\":\"error\", \"description\":\"exception in query - no legal parameter\"}").build();
            }

            TicketUser ticketUser = ticketUserDao.findById(intUserId);

            TicketUser[] ticketUserArr = new TicketUser[0];

            if (ticketUser != null) {
                ticketUserArr = new TicketUser[1];
                ticketUserArr[0] = ticketUser;
            }

            ///////////////////////////////////////////////
            // сформируем ответ в виде json
            JsonObject jsonRestAnswer = prepareJsonObjectAnswer(ticketUserArr);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(jsonRestAnswer)).build();
        }


        //return Response.ok("[]").build();
    }

    //////////////////////////////////////
    // формируем ответ
    private JsonObject prepareJsonObjectAnswer(TicketUser[] userArr) {
        JsonObject jsonRestAnswer = new JsonObject();
        jsonRestAnswer.addProperty("status", "ok");
        jsonRestAnswer.addProperty("total", userArr.length);

        JsonArray jsonUsersArray = new JsonArray();

        for (TicketUser oneUser : userArr) {
            JsonObject jsonUser = new JsonObject();

            jsonUser.addProperty("id", oneUser.getID());
            jsonUser.addProperty("depart", oneUser.getDepart());
            jsonUser.addProperty("email", oneUser.getEmail());
            jsonUser.addProperty("login", oneUser.getLogin());
            jsonUser.addProperty("password", oneUser.getPassword());
            jsonUser.addProperty("name", oneUser.getUserName());

            jsonUsersArray.add(jsonUser);
        }

        jsonRestAnswer.add("users", jsonUsersArray);

        return jsonRestAnswer;
    }

    // получаем от клиента запрос на изменение параметров пользователя
    // структура вида
    // {
    //    login: "alx1",
    //    password: "pass1",
    //    email: "alx1@aa.ru",
    //    name: "alx1 user1 name1",
    //    depart: "alxdepart1"
    // }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/newuser")
    public Response addUser(String inputJson) {

        JsonObject jsonInput = new JsonParser().parse(inputJson).getAsJsonObject();

        String login = jsonInput.get("login").getAsString();
        String pass = jsonInput.get("pass").getAsString();
        String email = jsonInput.get("email").getAsString();
        String name = jsonInput.get("name").getAsString();
        String depart = jsonInput.get("depart").getAsString();

        TicketUser ticketUser = ticketUserDao.create(login, name, email, depart, pass);

        if (ticketUser == null) {
            return Response.ok("{\"status\":\"error\", \"description\":\"error creating user\"}").build();
        }

        // готовим ответ
        JsonObject jsonOutput = new JsonObject();
        jsonOutput.addProperty("status", "ok");
        jsonOutput.addProperty("description", "ok");

        Gson gson = new Gson();

        return Response.ok(gson.toJson(jsonOutput)).build();

    }


    // получаем от клиента запрос на изменение параметров пользователя
    // структура вида
    // {
    //    id: 2,
    //    login: "alx1",
    //    password: "pass1",
    //    email: "alx1@aa.ru",
    //    name: "alx1 user1 name1",
    //    depart: "alxdepart1"
    // }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/updateuser")
    public Response updateUser(String inputJson) {

        JsonObject jsonInput = new JsonParser().parse(inputJson).getAsJsonObject();

        int userId = jsonInput.get("id").getAsInt();
        String login = jsonInput.get("login").getAsString();
        String pass = jsonInput.get("pass").getAsString();
        String email = jsonInput.get("email").getAsString();
        String name = jsonInput.get("name").getAsString();
        String depart = jsonInput.get("depart").getAsString();

//        log.warn("id " + userId + " login " + login + " pass " + pass + " email " + email + " name " + name + " depart " + depart);

        if (userId <= 0) {
            return Response.ok("{\"status\":\"error\", \"description\":\"wrong user id\"}").build();
        }

        // ищем пользователя в базе
        TicketUser ticketUser = ticketUserDao.findById(userId);

        if (ticketUser == null) {
            return Response.ok("{\"status\":\"error\", \"description\":\"user not found in database\"}").build();
        }

        ticketUser.setLogin(login);
        ticketUser.setPassword(pass);
        ticketUser.setEmail(email);
        ticketUser.setUserName(name);
        ticketUser.setDepart(depart);

        // сохраним
        ticketUserDao.update(ticketUser);

        // готовим ответ
        JsonObject jsonOutput = new JsonObject();
        jsonOutput.addProperty("status", "ok");
        jsonOutput.addProperty("description", "ok");

        Gson gson = new Gson();

        return Response.ok(gson.toJson(jsonOutput)).build();

    }


    // удаление пользователей из базы
    // передаем массив идентификаторов [id1, id2, .....]
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/deleteusers")
    public Response deleteUser(String inputJson) {

        JsonArray jsonInput = new JsonParser().parse(inputJson).getAsJsonArray();

        if (jsonInput.size() == 0) {
            return Response.ok("{\"status\":\"error\", \"description\":\"nothing to delete\"}").build();
        }




        return Response.ok("[]").build();

    }
}
