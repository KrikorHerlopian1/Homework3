/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

Condominium.java
Condominium Property that inherits from PurchaseProperty.

 */
package com.mycompany.kherl1_6617_hw3;

/**
 *
 * @author krikorherlopian
 */
public class Condominium extends PurchaseProperty {

    //Monthly management fee charged by the condominium association
    private double monthlyManagementFee;

    Condominium() {
        this.type.set("Condominium");
    }

    Condominium(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource, String type, double purchasePrice, double annualAmountOfTaxes,
            double monthlyManagementFee) {
        super(streetAddr, city, state, postalCode, numberOfBedrooms,
                numberOfBathrooms, description, imageResource, purchasePrice, annualAmountOfTaxes);
        this.monthlyManagementFee = monthlyManagementFee;
        this.type.set("Condominium");
        this.purchaseOrRentalPrice.set("" + purchasePrice);
    }

    public double getMonthlyManagementFee() {
        return monthlyManagementFee;
    }

    public void setMonthlyManagementFee(double monthlyManagementFee) {
        this.monthlyManagementFee = monthlyManagementFee;
    }
}
