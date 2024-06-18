package unit_tests;

import database_access.DAO;
import database_access.DBConnection;
import database_access.DoConnection;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DataBaseConnectionUnitTest {
    static DBConnection connection;

    @BeforeAll
    public static void setUp(){
        connection = new DBConnection();
    }

    @Test
    public void executorServiceTest() {

        int threadAmountBefore = Thread.activeCount();

        connection.executeQuery(DAO.readItem("test0"));

        int threadAmountAfter = Thread.activeCount();

        Assertions.assertTrue(threadAmountBefore < threadAmountAfter);

    }

    @Test
    public void connectionTest(){

        try {
            Assertions.assertNotNull(DoConnection.getConnection());
            Assertions.assertEquals(DoConnection.getConnection().getMetaData().getUserName(), "postgres.qtncwvznrznqnrrdlkfb");
            Assertions.assertTrue(DoConnection.getConnection().getClientInfo().contains("Supavisor"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
