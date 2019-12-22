/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

Apartment.java
Apartment Property that inherits from RentalProperty.

 */
package com.mycompany.kherl1_6617_hw3;

/**
 *
 * @author krikorherlopian
 */
public class Apartment extends RentalProperty {

    Apartment() {
        this.type.set("Apartment");
    }

    Apartment(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource, String type, double monthlyFee, int numberOfMonthsLease) {
        super(streetAddr, city, state, postalCode, numberOfBedrooms,
                numberOfBathrooms, description, imageResource, monthlyFee, numberOfMonthsLease);
        this.type.set("Apartment");
        this.purchaseOrRentalPrice.set("" + monthlyFee);
    }
}
