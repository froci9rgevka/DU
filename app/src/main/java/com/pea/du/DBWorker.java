package com.pea.du;



import java.sql.*;
import java.util.ArrayList;


public class DBWorker {
    private static final String URL = "jdbc:mysql://78.47.195.208:3306/zkh_gks3?useSSL=false";
    private static final String USERNAME = "e.polyakov";
    private static final String PASSWORD = "1";
    private static final String localURL = "jdbc:mysql://localhost:3306/mydbtest?useSSL=false";
    private static final String localUSERNAME = "root";
    private static final String localPASSWORD = "root";


    private static final String querry = "SELECT * FROM zkh_House";
    private static final String INSERT_NEW = "INSERT INTO users(name, age, email) VALUES(?,?,?)";
    private static final String DELETE = "DELETE FROM users WHERE idUsers = ?";


    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:sample.db";

    Connection connection = null;
    Driver driver = null;

    public Connection getConnection() {
        return connection;
    }

    DBWorker() {

            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                System.out.println("Can't get class. No driver found");
                e.printStackTrace();
                return;
            }
            try {
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException e) {
                System.out.println("Can't get connection. Incorrect URL");
                e.printStackTrace();
                return;
            }
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Can't close connection");
                e.printStackTrace();
                return;
            }

    }

    // Получение адреса из таблицы
    public ArrayList read () {
        ArrayList resultList = new ArrayList();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(querry);

            while (resultSet.next())  {
                String adress = new String(resultSet.getString("Name"));
                resultList.add(adress);
            }

        } catch (SQLException e) {
            System.out.println("Не удалось получить данные!");
        }
        finally {
            return resultList;
        }
    }
 /*
    // Запись данных в таблицу
    public void write (User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW);

            preparedStatement.setString(1,  "Kenny");
            preparedStatement.setInt(2,6);
            preparedStatement.setString(3, "ken@gmail.com");

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Не удалось записать данные!");
        }

    }


    // Получение данных из таблицы
    public ArrayList read () {
        ArrayList resultList = new ArrayList();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(querry);

            while (resultSet.next())  {
                User user = new User();
                user.setId(resultSet.getInt("idUsers"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                user.setEmail(resultSet.getString("email"));
                System.out.println(user);
                resultList.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Не удалось получить данные!");
        }
        finally {
            return resultList;
        }
    }


    // Удалить запись из таблицы
    public void delete (User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Не удалось удалить данные!");
        }
    }



    //execute - можно делать вставку и получение данных
    //statement.execute("INSERT INTO USERS(name, age, email) VALUES ('Dima', 33, 'dima33.gmail.com')");
    //executeUpdate - можено обновлять и вставлять, но нельзя получать данные
    //int result = statement.executeUpdate("UPDATE USERS SET email = 'dima33,mail.ru' WHERE (name='Dima') AND (age=33) ");
    //System.out.print(result);
    //executeCuerry - можно реализовать только select-ы
    //ResultSet res = statement.executeQuery("SELECT * FROM USERS");;

    //addBatch - для выполнения пакетной обработки. несколько запросов за одну команду execute

            statement.addBatch("INSERT INTO USERS(name, age, email) VALUES ('Dima', 34, 'dima34.gmail.com')");
            statement.addBatch("INSERT INTO USERS(name, age, email) VALUES ('Dima', 35, 'dima35.gmail.com')");
            statement.addBatch("INSERT INTO USERS(name, age, email) VALUES ('Dima', 36, 'dima36.gmail.com')");
            statement.executeBatch();
            statement.clearBatch();
            */

}
