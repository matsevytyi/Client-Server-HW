package integration_tests;

import app.MockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DatabaseIntegrationTest {
    @Test
    public void testAll(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);


        MockServer mockServer = new MockServer();
        mockServer.lauchService();

        String output = outputStream.toString();

        // Look manually in the output, also below there are some assertions
        System.err.println(output);

        Assertions.assertTrue(output.contains("Successfully executed"));
        Assertions.assertTrue(output.contains("Generated Message"));

        Assertions.assertTrue(output.contains("Query executed: SELECT * FROM public.items WHERE name = 'test0'"));
        Assertions.assertTrue(output.contains("Query executed: SELECT * FROM public.items WHERE price > '0' AND price < '200'"));
    }
}
