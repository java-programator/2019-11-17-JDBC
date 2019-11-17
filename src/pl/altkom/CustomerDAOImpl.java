package pl.altkom;

import pl.altkom.model.Customer;
import pl.altkom.model.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private Connection connection = null;

    @Override
    public void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCustomerTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE customers (" +
                    "id INTEGER PRIMARY KEY," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "region TEXT," +
                    "age INTEGER," +
                    "sex TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Customer> readAllCustomers() {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Customer> customers = new ArrayList<>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from customers");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String region = resultSet.getString("region");
                int age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                Customer c = new Customer(
                        id,
                        firstName,
                        lastName,
                        region,
                        age,
                        Sex.valueOf(sex)
                );
                customers.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return customers;
    }

    @Override
    public void addCustomer(Customer c) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    String.format("INSERT INTO customers VALUES(%d, '%s', '%s', '%s', %d, '%s')",
                            c.getId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getRegion(),
                            c.getAge(),
                            c.getSex().toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteData() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM customers");
            statement.executeUpdate("DROP TABLE customers");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
