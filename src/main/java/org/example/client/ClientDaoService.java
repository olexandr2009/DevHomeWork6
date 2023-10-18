package org.example.client;

import org.example.database.Database;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class ClientDaoService {
    private static final String CREATE_SQL = "./src/main/resources/sql/query/client/create_client.sql";
    private static final String GET_BY_ID_SQL =  "./src/main/resources/sql/query/client/find_client_by_id.sql";
    private static final String GET_BY_NAME_SQL =  "./src/main/resources/sql/query/client/find_clients_by_name.sql";
    private static final String DELETE_WHERE_ID_SQL =  "./src/main/resources/sql/query/client/delete_client.sql";
    private static final String UPDATE_NAME_WHERE_ID_SQL =  "./src/main/resources/sql/query/client/update_name_where_id.sql";
    private static final String GET_MAX_ID_SQL = "./src/main/resources/sql/query/client/get_by_max_id.sql";
    private static final String GET_ALL_SQL = "./src/main/resources/sql/query/client/get_all_clients.sql";

    private final Database DB;
    private PreparedStatement CREATE_ST;
    private PreparedStatement GET_ALL_ST;
    private PreparedStatement GET_BY_ID_ST;
    private PreparedStatement GET_MAX_ID_ST;
    private PreparedStatement GET_BY_NAME_ST;
    private PreparedStatement DELETE_WHERE_ID_ST;
    private PreparedStatement UPDATE_NAME_WHERE_ID_ST;

     public ClientDaoService(Database database){
        if (database == null){
            throw new NullPointerException("database shouldn't be null");
        }
        DB = database;

       initStatementsFromFiles();

    }
    private void initStatementsFromFiles(){
        try{
            String sql;
            sql = Files.readString(Path.of(GET_BY_ID_SQL));
            GET_BY_ID_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(GET_BY_NAME_SQL));
            GET_BY_NAME_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(CREATE_SQL));
            CREATE_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(DELETE_WHERE_ID_SQL));
            DELETE_WHERE_ID_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(GET_MAX_ID_SQL));
            GET_MAX_ID_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(UPDATE_NAME_WHERE_ID_SQL));
            UPDATE_NAME_WHERE_ID_ST = DB.getPreparedStatement(sql);

            sql = Files.readString(Path.of(GET_ALL_SQL));
            GET_ALL_ST = DB.getPreparedStatement(sql);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public long createClient(String name){
        try{
            CREATE_ST.setString(1,name);
            CREATE_ST.executeUpdate();

            ResultSet rs = CREATE_ST.executeQuery();
            if (rs.next()){
                return rs.getLong("client_id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public Client getByMaxID(){
        try{
            ResultSet rs = GET_MAX_ID_ST.executeQuery();
            if (rs.next()){
                long clientId = rs.getLong("client_id");
                String clientName = rs.getString("client_name");
                return new Client(clientId, clientName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Client> getAll(){
         List<Client> clients = new ArrayList<>();
        try{
            ResultSet rs = GET_ALL_ST.executeQuery();
            while (rs.next()){
                long clientId = rs.getLong("client_id");
                String clientName = rs.getString("client_name");
                clients.add(new Client(clientId, clientName));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return clients;
    }
    public Client getByID(long id){
        try{
            GET_BY_ID_ST.setLong(1,id);
            ResultSet rs = GET_BY_ID_ST.executeQuery();
            if (rs.next()){
                long clientId = rs.getLong("client_id");
                String clientName = rs.getString("client_name");
                return new Client(clientId, clientName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Client> getByName(String name){
        List<Client> clients = new ArrayList<>();
        try{
            GET_BY_NAME_ST.setString(1,name);
            ResultSet rs = GET_BY_NAME_ST.executeQuery();
            while (rs.next()){
                long clientId = rs.getLong("client_id");
                String clientName = rs.getString("client_name");
                clients.add(new Client(clientId, clientName));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return clients;
    }
    public boolean deleteByID(long id){
         try{
            DELETE_WHERE_ID_ST.setLong(1,id);
            int deleted = DELETE_WHERE_ID_ST.executeUpdate();
            return deleted >= 0;
         }catch (SQLException ex){
            ex.printStackTrace();
         }
         return false;
    }

    public boolean updateWhereId(long id,String name){
        try {
            UPDATE_NAME_WHERE_ID_ST.setString(1,name);
            UPDATE_NAME_WHERE_ID_ST.setLong(2,id);
            int deleted = UPDATE_NAME_WHERE_ID_ST.executeUpdate();
            return deleted >= 0;
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        ClientDaoService daoService = new ClientDaoService(Database.getDefaultDB());
        System.out.println(daoService.getAll());
    }

}
