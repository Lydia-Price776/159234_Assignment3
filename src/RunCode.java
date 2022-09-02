/*This class runs the code*/

import java.io.File;

public class RunCode {
    public static void main (String[] args) {
         new ReadFile(new File("computers.txt"));
        setUserInformation();
        new LoginScreen();
    }

    private static void setUserInformation () {
        LoginDialog.setUsers("p1", "p1", "Salesperson");
        LoginDialog.setUsers("p2", "p2", "Salesperson");
        LoginDialog.setUsers("p3", "p3", "Salesperson");
        LoginDialog.setUsers("m1", "m1", "Manager");
        LoginDialog.setUsers("m2", "m2", "Manager");
    }
}
