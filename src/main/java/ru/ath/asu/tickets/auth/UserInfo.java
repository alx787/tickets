package ru.ath.asu.tickets.auth;

public class UserInfo {
    private String id;
    private String email;
    private String login;
    private String fio;
    private String department;
    private String phone;
    private String token;

    public UserInfo() {
        this.id = "";
        this.email = "";
        this.login = "";
        this.fio = "";
        this.department = "";
        this.phone = "";
        this.token = "";

    }

    public UserInfo(String id, String email, String login, String fio, String department, String phone, String token) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.fio = fio;
        this.department = department;
        this.phone = phone;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
