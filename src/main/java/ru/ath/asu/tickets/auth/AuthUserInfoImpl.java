package ru.ath.asu.tickets.auth;

import javax.annotation.PostConstruct;

public class AuthUserInfoImpl implements AuthUserInfo {

    private UserInfo userInfo;


//    public AuthUserInfoImpl(String email, String login, String fio, String department, String phone) {
//        this.userInfo = new UserInfo(email, login, fio, department, phone);
//    }

    @PostConstruct
    public void init() {
        this.userInfo = new UserInfo();
    }

    @Override
    public UserInfo getUserIhfo() {
        return userInfo;
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
