package org.example.client;

import org.example.database.Database;
import org.example.databaseInit.DatabaseInitService;
import org.example.prefs.ConfigsNames;
import org.example.prefs.Configurations;
import org.h2.tools.DeleteDbFiles;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDaoServiceTest {
    private static Database DB;
    private static ClientDaoService daoService;
    @BeforeAll
    static void init(){
        String dbUrl = Configurations.Configs.getConfigAsString(ConfigsNames.TEST_DB_URL);
        DB = new Database(dbUrl);
        DatabaseInitService.initDatabase(dbUrl);
        daoService = new ClientDaoService(DB);
    }
    @AfterAll
    static void drop() {
        DeleteDbFiles.execute(".",getTestDbName(),true);
    }
    private static String getTestDbName(){
        String locationUrl = Configurations.Configs.getConfigAsString(ConfigsNames.TEST_DB_LOCATION_URL);
        String[] splited = locationUrl.split("/");
        String fullDbName = splited[splited.length - 1];
        int index = fullDbName.indexOf('.');
        return fullDbName.substring(0, index);
    }

    @Test
    void testCreateClientHandledCorrectly() {
        long actual = daoService.createClient("Nazar");
        long expected = daoService.getByMaxID().id();
        assertEquals(expected,actual);
    }

    @Test
    void testGetByIDHandledCorrectly() {
        assertEquals(new Client(1,"Oleg"),daoService.getByID(1));
    }
    @Test
    void testGetByID_0() {
        assertNull(daoService.getByID(0));
    }
    @Test
    void testGetByID_Too_Big() {
        assertNull(daoService.getByID(8888888888888888889L));
    }

    @Test
    void getByNameHandledCorrectly() {
        assertEquals(List.of(new Client(1,"Oleg")),daoService.getByName("Oleg"));
    }
    @Test
    void testDeleteByIDHandledCorrectly() throws SQLException{
        Connection connection = DB.getConnection();
         try {
            connection.setAutoCommit(false);
            long id = daoService.createClient("Valera");
            daoService.deleteByID(id);
            Client client = daoService.getByID(id);
            assertNull(client);
        } catch (Exception e) {
            connection.rollback();
        }
    }
    @Test
    void testUpdateByIDHandledCorrectly() throws SQLException{
        Connection connection = DB.getConnection();
        try {
            connection.setAutoCommit(false);
            long id = daoService.createClient("Maksym");
            daoService.updateWhereId(id,"Valera");
            Client client = daoService.getByID(id);
            assertEquals(new Client(id,"Valera"),client);
        } catch (Exception e) {
            connection.rollback();
        }
    }

}