package tn.esprit.atlas.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    Connection cnx;



    public static DatabaseConnection instance;
    public DatabaseConnection(){

        String Url = "jdbc:mysql://localhost:3306/atlas";
        String Username="root";
        String Password="";

        try {
            cnx= DriverManager.getConnection(Url,Username,Password);
            System.out.println("Connextion etablie");
        } catch (SQLException e) {
            System.out.println("Connextion n'est pas etablie");
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if(instance==null){
            instance=  new DatabaseConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}