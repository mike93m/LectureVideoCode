package movies;

import java.util.List;

import static input.InputUtils.*;

public class MovieList {

    private static final String DB_PATH = "jdbc:sqlite:movie_watchlist.sqlite";
    private static Database database;
    public static void main(String[] args) {
        database = new Database(DB_PATH);
        //addNewMovies();
        checkIfWatchedAndRate();
        deleteWatchedMovies();
        searchMovie();
        displayAllMovies();
    }

    public static void addNewMovies() {
        do {
            String movieName = stringInput("Enter the movie name");
            boolean movieWatched = yesNoInput("Have you seen this movie yet?");
            int movieStars = 0;
            if (movieWatched) {
                movieStars = positiveIntInput("How many stars would you rate this movie out of 5?");
                while (movieStars < 1 || movieStars > 5){
                    System.out.println("Invalid entry \nPlease enter a number between 1 and 5");
                    movieStars = positiveIntInput("How many stars would you rate this movie out of 5?");
                }
            }
            Movie movie = new Movie(movieName, movieStars, movieWatched);
            database.addNewMovie(movie);
        } while (yesNoInput("Add a movie to the watchlist"));

    }

    public static void displayAllMovies() {
        List<Movie> movies = database.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("No movies");
        } else {
            for (Movie movie: movies) {
                System.out.println(movie);
            }
        }

    }

    public static void checkIfWatchedAndRate() {
        List<Movie> unwatchedMovies = database.getAllMoviesByWatched(false);

        for (Movie movie: unwatchedMovies) {
            boolean hasWatched = yesNoInput("Have you watched " + movie.getName() + " yet?");
            if (hasWatched) {
                int stars = positiveIntInput("What is your rating for " + movie.getName() + " out of 5?");
                movie.setWatched(true);
                movie.setStars(stars);
                database.updateMovie(movie);
            }
        }
    }

    public static void deleteWatchedMovies() {

        System.out.println("Here are all the movies you have seen");

        List<Movie> watchedMovies = database.getAllMoviesByWatched(true);

        for (Movie movie: watchedMovies) {
            boolean delete = yesNoInput("Delete " + movie.getName() + "?");
            if (delete) {
                database.deleteMovie(movie);
            }
        }
    }

    public static void searchMovie() {
        String movieName = stringInput("Enter movie name:");
        List<Movie> movieMatches = database.search(movieName);
        if (movieMatches.isEmpty()) {
            System.out.println("No matches");
        } else {
            for (Movie movie: movieMatches) {
                System.out.println(movie);
            }
        }
    }

}
