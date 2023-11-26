package movies;

import java.sql.*;

public class Database {

    private String databasePath;
    Database(String databasePath) {

        this.databasePath = databasePath;

        try (Connection connection = DriverManager.getConnection(databasePath);
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS " +
                    "movies (name TEXT, stars INTEGER, watched BOOLEAN)");



        } catch (SQLException e) {
            System.out.println("Error creating movie DB table because " + e);
        }
    }

    public void addNewMovie(Movie movie) {

        try (Connection connection = DriverManager.getConnection(databasePath);
             Statement statement = connection.createStatement()) {


        } catch (SQLException e) {
            System.out.println("Error adding movie " + movie + " because " + e);
        }
    }

}
