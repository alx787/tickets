package ru.ath.asu.tickets.rest;

import com.atlassian.plugins.rest.common.multipart.FilePart;
import com.atlassian.plugins.rest.common.multipart.MultipartFormParam;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.ath.asu.tickets.aousers.TicketUser;
import ru.ath.asu.tickets.aousers.TicketUserDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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


        for (JsonElement jsonElement : jsonInput) {
            TicketUser ticketUser = ticketUserDao.findById(jsonElement.getAsInt());
            ticketUserDao.remove(ticketUser);
        }

        return Response.ok("{\"status\":\"ok\", \"description\":\"delete complete\"}").build();

    }

    // передаем файл с пользователями
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Path("/loadusers")
    public Response loadUsers(@MultipartFormParam("users-file-upload") List<FilePart> filePartList) {

        // читаем файл
        if (filePartList.size() != 1) {
            return Response.ok("{\"status\":\"error\", \"description\":\"received " + String.valueOf(filePartList.size()) + " files, Need 1 file\"}").build();
        }

        int cnt = 0; // счетчик загруженных записей


        try {
            File tempFile = File.createTempFile("users_load", "tickets");

            FileOutputStream outputStream = new FileOutputStream(tempFile);

            InputStream is = null;
            is = filePartList.get(0).getInputStream();

            int read;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tempFile);


            // <user fio="" email="" depart="" password="">
            // обходим файл
            NodeList nList = doc.getElementsByTagName("user");



            // тут всех удалим если не будет ошибок
            ticketUserDao.removeAll();



            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    String fio = element.getAttribute("fio");
                    String email = element.getAttribute("email");
                    String depart = element.getAttribute("depart");
                    String password = element.getAttribute("password");


                    TicketUser ticketUser = ticketUserDao.create(email, fio, email, depart, password);

                    if (ticketUser == null) {
                        tempFile.delete();
                        return Response.ok("{\"status\":\"error\", \"description\":\"error creating user " + fio + " email " + email + "\"}").build();
                    }


//                    log.warn("fio: " + fio + ", email: " + email + ", depart: " +  depart + ", password: " +  password);

                    cnt++;
                }

            }


            tempFile.delete();

        } catch (IOException e) {
            return Response.ok("{\"status\":\"error\", \"description\":\"received " + String.valueOf(filePartList.size()) + " files, Need 1 file\"}").build();

        } catch (ParserConfigurationException e) {
            return Response.ok("{\"status\":\"error\", \"description\":\"error configure parse file\"}").build();
//            e.printStackTrace();
        } catch (SAXException e) {
//            return Response.ok("{\"status\":\"error\", \"description\":\"error SAX parser\"}").build();
            return Response.ok("{\"status\":\"error\", \"description\":\"error in XML parser\"}").build();
//            e.printStackTrace();
        }

        if (cnt == 0) {
            return Response.ok("{\"status\":\"error\", \"description\":\"no one record load\"}").build();
        }

        return Response.ok("{\"status\":\"ok\", \"description\":\"load " + String.valueOf(cnt) + " records\"}").build();

    }

}
