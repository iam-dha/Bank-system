package model;

public class Transaction {
    private int id;
    private String fromAccount;
    private String toAccount;
    private long balance;
    private String dateTime;
    private String time;
    private String fromUserName;
    private String toUserName;
    private String message;

    public Transaction(int id, String fromAccount, String toAccount, long balance, String dateTime, String time, String fromUserName, String toUserName, String message) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.balance = balance;
        this.dateTime = dateTime;
        this.time = time;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.message = message;
    }


    public Transaction(){

    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
