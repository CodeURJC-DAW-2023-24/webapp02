package com.example.candread.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDatos {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            // Paso 1: Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Paso 2: Establecer la conexión con la base de datos
            String url = "jdbc:mysql://localhost:8080/";
            String username = "tu_usuario";
            String password = "tu_contraseña";
            connection = DriverManager.getConnection(url, username, password);

            // Paso 3: Crear una declaración
            statement = connection.createStatement();

            // Paso 4: Ejecutar el script SQL para crear la base de datos
            String sql = "CREATE DATABASE IF NOT EXISTS nombre_de_tu_base_de_datos";
            statement.executeUpdate(sql);

            System.out.println("Base de datos creada con éxito.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Cerrar la conexión y la declaración
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
