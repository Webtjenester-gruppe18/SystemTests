package fastmoney;

public class User {
    private String cprNumber;
    private String firstName;
    private String lastName;

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User(String cprNumber, String firstName, String lastName) {
        this.cprNumber = cprNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
