/* This is the user class. It holds the information about each user*/


public class Users {
    String username;
    String password;
    Boolean isManager;

    public Users (String username, String password, String position) {
        setUsername(username);
        setPassword(password);
        setManager(position);
    }

    public Users (String username, String password, boolean manager) {
        setUsername(username);
        setPassword(password);
        setManager(manager);
    }

    public boolean AuthenticateLogin (String currentUsername, String currentPassword) {
        return currentUsername.equals(username) && currentPassword.equals(password);
    }

    public Boolean getManager () {
        return isManager;
    }

    public void setManager (String position) {
        this.isManager = position.equals("Manager");
    }

    public void setManager (boolean isManager) {
        this.isManager = isManager;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

}
