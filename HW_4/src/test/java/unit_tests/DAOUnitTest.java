package unit_tests;

import database_access.DAO;
import database_access.DBConnection;
import database_access.DoConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DAOUnitTest {

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void processResultTest() throws SQLException {
        // Redirecting System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Mocking ResultSet behavior
        when(resultSet.next()).thenReturn(true, true, false); // First and second calls return true, third call returns false
        when(resultSet.getString(2)).thenReturn("test");
        when(resultSet.getInt(3)).thenReturn(100);

        // Calling the test method
        DAO.processResult(resultSet);

        // Capturing the output and asserting the expected result
        String output = outputStream.toString().trim();

        Assert.assertEquals("Item received: test 100", output);
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
