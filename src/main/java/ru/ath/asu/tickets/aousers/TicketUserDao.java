package ru.ath.asu.tickets.aousers;

public interface TicketUserDao {
    TicketUser findById(int id);
    TicketUser[] findByAll();
    TicketUser create(String login, String username, String depart, String password);
    void remove(TicketUser ticketUser);
}
