package com.example.pizzaorderingapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ToggleGroup;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TextField customer_name;
    public TextField customer_number;
    public TextField no_toppings;

    public RadioButton xlRadio;
    public RadioButton lRadio;
    public RadioButton mRadio;
    public RadioButton sRadio;

    private static final double XL_PRICE = 15.00;
    private static final double L_PRICE = 12.00;
    private static final double M_PRICE = 10.00;
    private static final double S_PRICE = 8.00;
    private static final double TOPPING_PRICE = 1.50;
    private static final double TAX_RATE = 0.15;

    @FXML
    private ToggleGroup pizzaSizeGroup;

    @FXML
    private TableView<order> tableView;
    @FXML
    private TableColumn<order,String > c_name;
    @FXML
    private TableColumn<order, Integer> c_num;
    @FXML
    private TableColumn<order,String> p_size;
    @FXML
    private TableColumn<order,String> p_topping;
    @FXML
    private TableColumn<order,Double> total_bill;
    ObservableList<order> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c_name.setCellValueFactory(new
                PropertyValueFactory<order,String>("customerName"));
        c_num.setCellValueFactory(new
                PropertyValueFactory<order,Integer>("customerNumber"));
        p_size.setCellValueFactory(new
                PropertyValueFactory<order,String>("pizzaSize"));
        p_topping.setCellValueFactory(new
                PropertyValueFactory<order,String>("numToppings"));
        total_bill.setCellValueFactory(new
                PropertyValueFactory<order,Double>("totalBill"));
//        tableView.setItems(list);
        list.clear();tableView.setItems(list);

        pizzaSizeGroup = new ToggleGroup(); // Initialize if not done in FXML.
        xlRadio.setToggleGroup(pizzaSizeGroup);
        lRadio.setToggleGroup(pizzaSizeGroup);
        mRadio.setToggleGroup(pizzaSizeGroup);
        sRadio.setToggleGroup(pizzaSizeGroup);
    }

    public void orderbtn(ActionEvent event) {
        String customerName = customer_name.getText().trim();
        String customerNumber = customer_number.getText().trim();
        String toppingsInput = no_toppings.getText().trim();

        if (customerName.isEmpty() || customerNumber.isEmpty()) {
            showAlert("Name and Mobile number are required.");
            return;
        }

        if (pizzaSizeGroup.getSelectedToggle() == null) {
            showAlert("Please select a pizza size.");
            return;
        }

        int numToppings;
        try {
            numToppings = Integer.parseInt(toppingsInput);
            if (numToppings < 1) {
                showAlert("Please add at least one topping.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Number of toppings must be a valid number.");
            return;
        }

        double basePrice = 0.0;
        String pizzaSize = "";
        if (xlRadio.isSelected()) {
            basePrice = XL_PRICE;
            pizzaSize = "XL";
        } else if (lRadio.isSelected()) {
            basePrice = L_PRICE;
            pizzaSize = "L";
        } else if (mRadio.isSelected()) {
            basePrice = M_PRICE;
            pizzaSize = "M";
        } else if (sRadio.isSelected()) {
            basePrice = S_PRICE;
            pizzaSize = "S";
        }

        double toppingsCost = numToppings * TOPPING_PRICE;
        double subtotal = basePrice + toppingsCost;
        double tax = subtotal * TAX_RATE;
        double totalBill = subtotal + tax;

        // Save the order and update table
        order newOrder = new order(customerName, Integer.parseInt(customerNumber), pizzaSize, toppingsInput, totalBill);
        list.add(newOrder);
        tableView.setItems(list);
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void readorder(ActionEvent event) {
            String getName = customer_name.getText();


            // Establish a database connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/pizza-ordering-application";
            String dbUser = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                // Execute a SQL query to retrieve data for the given customer name
                String query = "SELECT * FROM customer WHERE `customer_name` = '"+getName+"'";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, getName);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        // Retrieve order details from the result set
                        String customerName = resultSet.getString("customer_name");
                        String customerNumber = resultSet.getString("customer_number");
                        String pizzaSize = resultSet.getString("Pizza_size");
                        int numToppings = resultSet.getInt("No_of_toppings");

                        // Populate the input fields with the retrieved order details
                        c_name.setText(customerName);
                        c_num.setText(customerNumber);
                        p_topping.setText(String.valueOf(numToppings));

                    } else {
                        showAlert("No Order Found");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Database Error");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Connection Error");
            }
        }


    public void updateorder(ActionEvent event) {
        String getName= customer_name.getText();
        Integer getnum= Integer.valueOf(customer_number.getText());
        Integer gettopp= Integer.valueOf(no_toppings.getText());
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza-ordering-application";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser,
                dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "UPDATE `customer` SET `customer_name`='"+getName+"',`Mobile_number`='"+getnum+"',`No_of_toppings`='"+gettopp+"'WHERE `customer_name` = '"+getName+"'";
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteorder(ActionEvent event) {
        String getname= customer_name.getText();
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza-ordering-application";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser,
                dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "DELETE FROM `customer` WHERE `customer_name`='"+getname+"' ";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Vieworders(ActionEvent event) {
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza-ordering-application";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser,
                dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "SELECT * FROM customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            list.clear();tableView.setItems(list);
            // Populate the table with data from the database
            while (resultSet.next()) {
                String  c_name = resultSet.getString("customer_name");
                Integer c_num = resultSet.getInt("Mobile_number");
                String  p_size = resultSet.getString("Pizza_size");
                String n_top = resultSet.getString("No_of_toppings");
                Double f_bill = resultSet.getDouble("Total_bill");
                tableView.getItems().add(new order(c_name, c_num, p_size, n_top,
                        f_bill));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}