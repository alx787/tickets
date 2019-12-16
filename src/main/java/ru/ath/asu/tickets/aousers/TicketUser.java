package ru.ath.asu.tickets.aousers;


import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.Mutator;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;

@Table("TicketUser")
public interface TicketUser extends Entity {
    @Accessor("login")
    @NotNull
    public String getLogin();

    @Mutator("login")
    @NotNull
    public void setLogin(String login);

    @Accessor("username")
    @NotNull
    public String getUserName();

    @Mutator("username")
    @NotNull
    public void setUserName(String userName);

    @Accessor("depart")
    @NotNull
    public String getDepart();

    @Mutator("depart")
    @NotNull
    public void setDepart(String depart);

    @Accessor("password")
    @NotNull
    public String getPassword();

    @Mutator("password")
    @NotNull
    public void setPassword(String depart);

}

// https://bitbucket.org/cfuller/atlassian-scheduler-jira-example/src/master/src/main/java/com/atlassian/jira/plugins/example/scheduler/AwesomeStuffDao.java