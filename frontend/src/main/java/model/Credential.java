package model;

public class Credential {
    private String token;
    private String account;
    private String role;

    public Credential(){
    }

    public Credential(String token, String account, String role) {
        this.token = token;
        this.account = account;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
