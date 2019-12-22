/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

Property.java
Property Class.

 */
package com.mycompany.kherl1_6617_hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author krikorherlopian
 */
abstract class Property {

    private String streetAddress = "";
    private String city = "";
    private String state = "";
    private String postalCode = "";
    private int numberOfBedrooms = 0;
    private int numberOfBathrooms = 0;
    StringProperty type;
    private String description = "";
    StringProperty imageResource;
    StringProperty purchaseOrRentalPrice;
    ImageView photo;

    Property() {
        if (this.type == null) {
            this.type = new SimpleStringProperty("");
        }
    }

    Property(String streetAddr, String city, String state, String postalCode, int numberOfBedrooms,
            int numberOfBathrooms, String description, String imageResource) {
        if (this.imageResource == null) {
            this.imageResource = new SimpleStringProperty(imageResource);
        }
        if (this.purchaseOrRentalPrice == null) {
            this.purchaseOrRentalPrice = new SimpleStringProperty("0.0");
        }
        if (this.type == null) {
            this.type = new SimpleStringProperty("");
        }
        this.streetAddress = streetAddr;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.description = description;
        this.imageResource.set(imageResource);
        setImage(imageResource);
    }

    public String getPurchaseOrRentalPrice() {
        return purchaseOrRentalPrice.get();
    }

    public void setPurchaseOrRentalPrice(String purchaseOrRentalPrice) {
        this.purchaseOrRentalPrice.set(Common.format(Double.parseDouble(purchaseOrRentalPrice.trim())));
    }

    public StringProperty purchaseOrRentalPriceProperty() {
        return purchaseOrRentalPrice;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageResource() {
        return imageResource.get();
    }

    public void setImageResource(String imageResource) {
        this.imageResource.set(imageResource);
        setImage(imageResource);
    }

    //resize the image for width and height to be lower than 200. Ratio kept too, so that image doesnt get stretched.
    private void setImage(String imageResource) {
        Image image1 = new Image(imageResource);
        double imageWidth = image1.getWidth();
        double imageHeight = image1.getHeight();
        double sizeToCheck = 200;
        while (imageWidth >= sizeToCheck || imageHeight >= sizeToCheck) {
            imageWidth = imageWidth / 1.01;
            imageHeight = imageHeight / 1.01;
        }
        photo = new ImageView(new Image(imageResource));
        photo.setFitHeight(imageHeight);
        photo.setFitWidth(imageWidth);
        photo.setImage(image1);
    }

    public StringProperty imageResourceProperty() {
        return imageResource;
    }
}
