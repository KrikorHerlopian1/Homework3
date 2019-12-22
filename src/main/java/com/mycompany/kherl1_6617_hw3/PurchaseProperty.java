/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

PurchaseProperty.java
Purchase Property that inherits from Property.

 */
package com.mycompany.kherl1_6617_hw3;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author krikorherlopian
 */
public class PurchaseProperty extends Property {

    //Purchase price of the house or condominium
    DoubleProperty purchasePrice;
    //Annual amount of taxes for the house or condominium
    private double annualAmountOfTaxes = 0;

    PurchaseProperty() {
    }

    PurchaseProperty(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource, double purchasePrice, double annualAmountOfTaxes) {
        super(streetAddr, city, state, postalCode, numberOfBedrooms,
                numberOfBathrooms, description, imageResource);
        if (this.purchasePrice == null) {
            this.purchasePrice = new SimpleDoubleProperty(purchasePrice);
        }
        this.purchasePrice.set(purchasePrice);
        this.annualAmountOfTaxes = annualAmountOfTaxes;
    }

    public DoubleProperty purchasePriceProperty() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice.set(purchasePrice);
    }

    public Double getPurchasePrice() {
        return purchasePrice.get();
    }

    public double getAnnualAmountOfTaxes() {
        return annualAmountOfTaxes;
    }

    public void setAnnualAmountOfTaxes(double annualAmountOfTaxes) {
        this.annualAmountOfTaxes = annualAmountOfTaxes;
    }

}
