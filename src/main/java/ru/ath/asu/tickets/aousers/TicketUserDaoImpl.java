package ru.ath.asu.tickets.aousers;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.DBParam;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;


@Transactional
@Named
public class TicketUserDaoImpl implements TicketUserDao {

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public TicketUserDaoImpl(@ComponentImport ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public TicketUser findById(int id) {
        return ao.get(TicketUser.class, id);
    }

    @Override
    public TicketUser[] findByAll() {
        return ao.find(TicketUser.class);
    }

    @Override
    public TicketUser findByLoginPassword(String login, String password) {
        TicketUser[] grEntityArr = ao.find(TicketUser.class, Query.select().where("LOGIN = ? AND PASSWORD = ?", login, password));
        if (grEntityArr.length >= 1) {
            // установить токен - наверное надо в другом классе логики работы
            return grEntityArr[0];
        }
        return null;

    }

    @Override
    public TicketUser create(String login, String username, String email, String depart, String password) {

        if (
            (login == null) || login.equals("") ||
            (username == null) || username.equals("") ||
            (email == null) || email.equals("") ||
            (depart == null) || depart.equals("") ||
            (password == null) || password.equals("")
        ) {
            return null;
        }


        final TicketUser ticketUser = ao.create(TicketUser.class,
                new DBParam("LOGIN", login),
                new DBParam("USERNAME", username),
                new DBParam("EMAIL", email),
                new DBParam("DEPART", depart),
                new DBParam("PASSWORD", password),
                new DBParam("TOKEN", "-")
        );
        ticketUser.save();

        return ticketUser;

    }

    @Override
    public void update(TicketUser tu) {
        TicketUser tuEntity = ao.get(TicketUser.class, tu.getID());
        tuEntity.setLogin(tu.getLogin());
        tuEntity.setUserName(tu.getUserName());
        tuEntity.setDepart(tu.getDepart());
        tuEntity.setPassword(tu.getPassword());
        tuEntity.setToken(tu.getToken());
        tuEntity.save();
    }

    @Override
    public void remove(TicketUser ticketUser) {
        ao.delete(ticketUser);
    }

    @Override
    public void removeAll() {
        ao.deleteWithSQL(TicketUser.class, "\"ID\" > ?", 0);
    }

}
