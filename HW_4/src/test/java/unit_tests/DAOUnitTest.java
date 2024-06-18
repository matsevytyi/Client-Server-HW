package unit_tests;

import database_access.DAO;
import database_access.DBConnection;
import database_access.DoConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DAOUnitTest {

    @Test
    public void processResultTest() {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            DBConnection connection = new DBConnection();

            connection.executeQuery(DAO.deleteItem("test0"));
            connection.executeQuery(DAO.createItem("test0", 100));

            String checkQuery = DAO.readItem("test0");

            Connection conn = DoConnection.getConnection();

            PreparedStatement statement = conn.prepareStatement(checkQuery);
            ResultSet resultSet = statement.executeQuery();

            DAO.processResult(resultSet);

            String answer = "";

            while (resultSet.next()) answer = resultSet.getString(2);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Assertions.assertTrue(outputStream.toString().contains("executed"));
            Assertions.assertTrue(outputStream.toString().contains(answer));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @Test
    public void createItemTest() {
        assertEquals("INSERT INTO public.items (name, price) VALUES ('test', '100')", DAO.createItem("test", 100));
    }

    @Test
    public void deleteItemTest() {
        assertEquals("DELETE FROM public.items WHERE name = 'test'", DAO.deleteItem("test"));
    }

    @Test
    public void readItemTest() {
        assertEquals("SELECT * FROM public.items WHERE name = 'test'", DAO.readItem("test"));
    }

    @Test
    public void updateItemTest() {
        assertEquals("UPDATE public.items SET name = 'newTest', price = '200' WHERE name = 'test'", DAO.updateItem("test", "newTest", 200));
    }

    @Test
    public void filterByPriceTest() {
        assertEquals("SELECT * FROM public.items WHERE price > '100' AND price < '200'", DAO.filterByPrice(100, 200));
    }
}
