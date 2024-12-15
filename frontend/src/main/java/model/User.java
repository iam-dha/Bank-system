package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private static Credential credential;

    @JsonProperty("account")
    private String account;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("lastName")
    private String lastname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phoneNumber")
    private String phonenumber;

    @JsonProperty("fund")
    private String fund;


    public User(String account, String firstname, String lastname, String email, String address, String phonenumber, String fund) {
        this.account = account;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phonenumber = phonenumber;
        this.fund = fund;
    }

    public static void setCredential(Credential credential) {
        User.credential = credential;
    }

    public static Credential getCredential(){
        return User.credential;
    }

    public User(){
    }
    public String getAccount() {
        return account;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getFund() {
        return fund;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

}



