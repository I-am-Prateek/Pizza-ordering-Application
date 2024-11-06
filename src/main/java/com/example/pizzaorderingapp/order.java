package com.example.pizzaorderingapp;

public class order {
    private String customerName;
    private Integer customerNumber;
    private String pizzaSize;
    private String numToppings;
    private Double totalBill;


    public order(String customerName, Integer customerNumber, String pizzaSize, String numToppings, Double totalBill) {
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.pizzaSize = pizzaSize;
        this.numToppings = numToppings;
        this.totalBill = totalBill;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public String getNumToppings() {
        return numToppings;
    }

    public void setNumToppings(String numToppings) {
        this.numToppings = numToppings;
    }

    public Double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }
}
