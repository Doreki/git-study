package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/user","user","123456");

                return c;
    }
}
