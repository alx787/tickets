package ru.ath.asu.tickets.aousers;

import com.atlassian.activeobjects.external.ActiveObjects;

public class TicketUserDaoImpl implements TicketUserDao {

    private final ActiveObjects ao;

    public TicketUserDaoImpl(ActiveObjects ao) {
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
    public TicketUser create(String login, String username, String depart, String password) {

        if (
            (login == null) || login.equals("") ||
            (username == null) || username.equals("") ||
            (depart == null) || depart.equals("") ||
            (password == null) || password.equals("")
        ) {
            return null;
        }

        final TicketUser ticketUser = ao.create(TicketUser.class);
        ticketUser.setLogin(login);
        ticketUser.setUserName(username);
        ticketUser.setDepart(depart);
        ticketUser.setPassword(password);
        ticketUser.save();

        return ticketUser;

    }

    @Override
    public void remove(TicketUser ticketUser) {
        ao.delete(ticketUser);
    }
}
