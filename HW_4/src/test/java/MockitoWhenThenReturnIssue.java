import database_access.DAO;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoWhenThenReturnIssue {
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

        // Call the method under test with the mocked ResultSet
        DAO.processResult(resultSet);

        // Capturing the output and asserting the expected result
        String output = outputStream.toString().trim();

        // Make sure the output matches the format printed by processResult method
        assertEquals("Item received: test 100", output);
    }
}
