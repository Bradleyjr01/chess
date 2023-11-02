package Server.DataAccessing;

import java.util.Objects;

public class UserData {

    private DataAccess database;
    private String username;
    private String password;
    private String email;

    public UserData(){}

    public UserData(UserData u) {
        username = u.getUsername();
        password = u.getPassword();
        email = u.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DataAccess getDatabase() {
        return database;
    }

    public void setDatabase(DataAccess database) {
        this.database = database;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(database, userData.database) && Objects.equals(username, userData.username) && Objects.equals(password, userData.password) && Objects.equals(email, userData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(database, username, password, email);
    }
}
