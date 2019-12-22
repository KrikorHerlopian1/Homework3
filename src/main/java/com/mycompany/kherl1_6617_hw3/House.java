/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

House.java
House Property that inherits from PurchaseProperty.

 */
package com.mycompany.kherl1_6617_hw3;

/**
 *
 * @author krikorherlopian
 */
public class House extends PurchaseProperty {

    House() {
        this.type.set("House");
    }

    House(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource, String type, double purchasePrice, double annualAmountOfTaxes) {
        super(streetAddr, city, state, postalCode, numberOfBedrooms,
                numberOfBathrooms, description, imageResource, purchasePrice, annualAmountOfTaxes);
        this.type.set("House");
        this.purchaseOrRentalPrice.set("" + purchasePrice);
    }
}
