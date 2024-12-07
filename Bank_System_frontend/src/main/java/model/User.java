package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private static Credential credential;

    public User(String account, String firstname, String lastname, String email, String password, String address, String createdate, String phonenumber, String fund, String token) {
        this.credential = new Credential(token, account);
        this.account = account;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.createdate = createdate;
        this.phonenumber = phonenumber;
        this.fund = fund;
    }

    public User(){
    }
    @JsonProperty("account")
    private String account;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("lastName")
    private String lastname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("address")
    private String address;

    @JsonProperty("createDate")
    private String createdate;

    @JsonProperty("phoneNumber")
    private String phonenumber;

    @JsonProperty("fund")
    private String fund;

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

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getCreatedate() {
        return createdate;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }
}



