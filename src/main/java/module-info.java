module com.example.pizzaorderingapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.pizzaorderingapp to javafx.fxml;
    exports com.example.pizzaorderingapp;
}