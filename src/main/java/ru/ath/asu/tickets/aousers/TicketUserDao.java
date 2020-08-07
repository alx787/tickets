package ru.ath.asu.tickets.aousers;

public interface TicketUserDao {
    TicketUser findById(int id);
    TicketUser[] findByAll();
    TicketUser findByLoginPassword(String login, String password);
    TicketUser create(String login, String username, String email, String depart, String password);
    void update(TicketUser ticketUser);
    void remove(TicketUser ticketUser);
    void removeAll();
}

/*
    Finding entities in Active Objects is done through the EntityManager find methods.

        Finding all entities
        The simplest one will return all entities of a given type. This is as simple as:

        Copy
        entityManager.find(Post.class);
        Here all the posts are going to be returned. Note that by default Active Object is lazy and therefore only IDs of the posts are going to be loaded. However it might still not be wise to load all the entities of a given type at once.

        Finding entities with a given criteria
        Adding a criteria is as simple as writing a SQL where clause. Here is what we would write to get the posts published between two dates:

        Copy
        entityManager.find(Post.class, Query.select().where("published > ? AND published < ?", date1, date2));
        Note how we made used of the ? placeholder just as when using JDBC's prepared statements. It is preferable to appending strings directly which might make the code prone to SQL injection.

        We've made used of the Query object here, but EntityManager provides a shortcut that allows us to write that same query like this:

        Copy
        entityManager.find(Post.class, "published > ? AND published < ?", date1, date2);
        Finding entities with like
        Adding a wildcard is also much like a SQL Query, here is what we would write to get a (contrived) field that is like the argument:

        Copy
        entityManager.find(Post.class, Query.select().where("SOME_FIELD LIKE ?", "%" + arg));
        Note:

        The naming of column is configured via the FieldNameConverter. When using the ActiveObjects plugin this is configured for you and column names used in queries should match the strategy used.

        The case is especially important when working with databases such as Postgres - which is one of the supported databases. See those FAQ entries for the strategy used in the Active Objects plugin:

        Column names
        Table names
        Ordering elements returned
        The order of returned element is not guaranteed by default in SQL, so here is how to apply the order clause when using Active Objects:

        Copy
        entityManager.find(Post.class, Query.select().order("published DESC"));
        Limiting the number of elements returned, and paging
        There is also the possibility to limit the number of entities returned:

        Copy
        entityManager.find(Post.class, Query.select().limit(10).offset(9));
        This query also starts selecting the elements from the 9th in the given order (which is not specified here). It is a good idea to specify a given order when using offset and limit for paging.
*/