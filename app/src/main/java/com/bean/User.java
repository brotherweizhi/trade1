package com.bean;

public class User {

    int uid;
    String tel;
    String username;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String account;
    int isOnLine;
    public String getAccount(){
        return account;
    }
    public int getIsOnline(){
        return isOnLine;
    }
    public void setAccount(String account){
        this.account=account;
    }
    public void setIsOnline(int isOnline){
        this.isOnLine=isOnline;
    }
}
