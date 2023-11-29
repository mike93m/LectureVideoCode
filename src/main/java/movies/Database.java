package movies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databasePath;


    Database(String databasePath) {
        // Hello

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

        String insertSQL = "INSERT INTO movies VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, movie.getName());
            preparedStatement.setInt(2, movie.getStars());
            preparedStatement.setBoolean(3, movie.isWatched());
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Error adding movie " + movie + " because " + e);
        }
    }

    public List<Movie> getAllMovies() {
        try (Connection connection = DriverManager.getConnection(databasePath);
             Statement statement = connection.createStatement()) {

            ResultSet movieResult = statement.executeQuery("SELECT * FROM movies ORDER BY name");
            List<Movie> movies = new ArrayList<>();

            while (movieResult.next()) {
                String name = movieResult.getString("name");
                int stars = movieResult.getInt("stars");
                boolean watched = movieResult.getBoolean("watched");
                Movie movie = new Movie(name, stars, watched);
                movies.add(movie);
            }
            return movies;

        } catch (SQLException e) {
            System.out.println("Error fetching all movies because " + e);
        }
        return null;
    }

    public List<Movie> getAllMoviesByWatched(boolean watchedStatus) {

        String preparedStatementString = "SELECT * FROM movies WHERE watched = ?";
        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementString)) {

            preparedStatement.setBoolean(1, watchedStatus);
            ResultSet movieResults = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();

            while (movieResults.next()) {
                String name = movieResults.getString("name");
                int stars = movieResults.getInt("stars");
                boolean watched = movieResults.getBoolean("watched");
                Movie movie = new Movie(name, stars, watched);
                movies.add(movie);
            }

            return movies;
        } catch (SQLException e) {
            System.out.println("Error fetching watched movies because " + e);
            return null;
        }

    }

    public void updateMovie(Movie movie) {

        String sql = "UPDATE movies SET stars = ?, watched = ? WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, movie.getStars());
            preparedStatement.setBoolean(2, movie.isWatched());
            preparedStatement.setString(3, movie.getName());
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Error updating movie because " + e);
        }
    }

    public void deleteMovie(Movie movie) {

        String sql = "DELETE FROM movies WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement=connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movie.getName());
            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println("Error deleting movie " + movie + " because " + e);
        }

    }

    public List<Movie> search(String searchTerm) {
         String sql = "SELECT * FROM movies WHERE UPPER(name) LIKE UPPER(?)";

         try (Connection connection = DriverManager.getConnection(databasePath);
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

             preparedStatement.setString(1, "%" + searchTerm + "%");

             ResultSet movieResults = preparedStatement.executeQuery();

             List<Movie> movies = new ArrayList<>();

             while (movieResults.next()) {
                 String name = movieResults.getString("name");
                 int stars = movieResults.getInt("stars");
                 boolean watched = movieResults.getBoolean("watched");

                 Movie movie = new Movie(name,stars,watched);
                 movies.add(movie);
             }
             return movies;



         } catch (SQLException e) {
             System.out.println("Error searching for " + searchTerm + " because " + e);
         }
         return null;
    }

}
