package GestiondeTareasconBD;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_tareas";// Cambia gestion_tareas por el nombre de tu db
    private static final String USER = "root"; // Cambia si tu usuario es diferente
    private static final String PASSWORD = ""; // Cambia si tienes una contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}