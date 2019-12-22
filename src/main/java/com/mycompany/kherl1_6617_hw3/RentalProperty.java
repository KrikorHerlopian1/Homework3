/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

RentalProperty.java
Rental Property that inherits from Property.

 */
package com.mycompany.kherl1_6617_hw3;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author krikorherlopian
 */
abstract class RentalProperty extends Property {

    //Monthly rental fee
    DoubleProperty monthlyFee;
    //The number of months for the lease period
    private int numberOfMonthsLease = 0;

    RentalProperty() {
    }

    RentalProperty(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource, double monthlyFee, int numberOfMonthsLease) {
        super(streetAddr, city, state, postalCode, numberOfBedrooms,
                numberOfBathrooms, description, imageResource);
        if (this.monthlyFee == null) {
            this.monthlyFee = new SimpleDoubleProperty(monthlyFee);
        }
        this.monthlyFee.set(monthlyFee);
        this.numberOfMonthsLease = numberOfMonthsLease;
    }

    public DoubleProperty monthlyFeeProperty() {
        return monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee.set(monthlyFee);
    }

    public Double getMonthlyFee() {
        return monthlyFee.get();
    }

    public int getNumberOfMonthsLease() {
        return numberOfMonthsLease;
    }

    public void setNumberOfMonthsLease(int numberOfMonthsLease) {
        this.numberOfMonthsLease = numberOfMonthsLease;
    }
}
