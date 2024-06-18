package database_access;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class DoConnection {
    private static final String URL = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:6543/postgres?user=postgres.qtncwvznrznqnrrdlkfb&password=StoreAutomation777!!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}

public class DBConnection {

    private ExecutorService executorService;

    public DBConnection(){
        // Create a fixed thread pool with a queue
        executorService = new ThreadPoolExecutor(
                10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    public void executeQuery(String query) {
        executorService.submit(() -> {
            try (Connection connection = DoConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                System.out.println("Query executed: " + query);

                DAO.processResult(resultSet);

            } catch (SQLException e) {
                if(e.toString().contains("duplicate key value violates unique constraint")){
                    System.out.println("Item with this name already exists, try again!");
                } else if (e.toString().contains("No results were returned")){
                    System.out.println("Successfully executed");
                } else e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            // Wait a bit before current tasks terminate
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();

                // Cancel currently executing tasks
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS))
                    System.err.println("Executor service did not terminate");
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}