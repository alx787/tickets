package ru.ath.asu.tickets.aousers;

public interface TicketUserDao {
    TicketUser findById(int id);
    TicketUser[] findByAll();
//    TicketUser findByLoginPassword();
    TicketUser create(String login, String username, String depart, String password);
    void remove(TicketUser ticketUser);
//    void update(int id, TicketUser ticketUser);
}
