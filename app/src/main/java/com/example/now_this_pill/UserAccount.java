package com.example.now_this_pill;

public class UserAccount {
    private String usertype;    ////복용자, 보호자 선택
    private String email;   //이메일
    private String password;    //비밀번호
    private String name;    //이름
    private String idToken;


    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    public String getIdToken() {
        return idToken;
    }
    public UserAccount() { } //빈 생성자가 필요 (firebase 관련)


    public String getUsertype() {return usertype;}
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {return name;}



    public void setUsertype(String usertype) {this.usertype = usertype;}
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {this.name=name;}
}
