/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

HomeController.java
This is the first and only controller  in application to handle showing properties, creating/editing properties
deleting properties and viewing property details.

 */
package com.mycompany.kherl1_6617_hw3;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static javafx.application.Application.launch;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author krikorherlopian
 */
public class HomeController implements Initializable {

    @FXML
    TableView table;//the table on screen that contains  properties.
    @FXML
    TabPane tb; // tabpane to switch between all properties, apartment, houses, and condos.
    @FXML
    SplitPane sp; // vertical splitpane between tableview and three create buttons at bottom of layout.
    @FXML
    TableColumn imageCol;//image column
    @FXML
    TableColumn typeCol;//type column
    @FXML
    TableColumn purchaseOrRentalPriceCol;//this is the column to show purchase price or rental fee
    @FXML
    HBox hbox;//horizontal box at bottom of application that contains the three create buttons 
    static String tabId = "all";// tabId selected  by default all ( all properties)
    static ArrayList<Property> propertyList = new ArrayList<Property>();//list of  properties
    static ObservableList<Property> propertiesData = FXCollections.observableArrayList();//list of properties for the table.
    String imageUri = "/icons/ic_image_placeholder.png";//default image to show when creating a new property
    Property property;//property to be created or edited.
    VBox vBox = new VBox();//this is vBox for creating editing form.
    Stage stage;
    GridPane gridPane;//gridpane for creating editing form.
    private final TextField tfStreetAddress = new TextField();//street address textfield in edit/create property form
    private final TextField tfCity = new TextField();//city textfield in edit/create property form
    private final TextField tfState = new TextField();//state textfield in edit/create property form
    private final TextField tfPostalCode = new TextField();//postal code textfield in edit/create property form
    private final TextField tfBedrooms = new TextField();//number of bedrooms textfield in edit/create property form
    private final TextField tfBathrooms = new TextField();//number of bathrooms textfield in edit/create property form
    private final TextField tfMonthlyFeeOrPurchasePrice = new TextField();//purchase price or monthly rental fee textfield in edit/create property form
    private final TextField tfNumberOfMonthsLease = new TextField();//months to lease textfield in edit/create property form
    private final TextField tfAnnualAmountOfTaxes = new TextField();//amount of taxes textfield in edit/create property form
    private final TextField tfMonthlyManagementFee = new TextField();//monthly management fee textfield in edit/create property form
    private final TextArea tfDescription = new TextArea();//description textarea in edit/create property form
    private final Button submit = new Button("Submit");//submit button inside dialog modal when editing or creating property.
    private static Scene scene;//scene for the dialog modal to create or edit property.
    private ImageView imageV;// this is the imageview in the modal when property to be created or edited.
    String state = "create";// this is the state to detect whether user is on form creating new property or editing
    Property selection;// this is the property details viewed on the right side.

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDummyData();
        setupSize();
        setupTable();
    }

    //set up the table for properties.
    private void setupTable() {
        table.setItems(propertiesData);
        table.setSelectionModel(null);
        TableColumn<Property, Property> actionCol = new TableColumn<>("actions");
        actionCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        actionCol.setCellFactory(param -> {
            return new TableCell<Property, Property>() {
                private final Button deleteButton = new Button("Delete");
                private final Button editButton = new Button("Modify");
                private final Button viewButton = new Button("View");

                @Override
                protected void updateItem(Property p1, boolean empty) {
                    super.updateItem(p1, empty);
                    if (p1 == null) {
                        setGraphic(null);
                        return;
                    }
                    HBox actionBox = new HBox();
                    actionBox.setAlignment(Pos.CENTER);
                    actionBox.getChildren().addAll(viewButton, editButton, deleteButton);
                    actionBox.setSpacing(5);
                    setGraphic(actionBox);
                    deleteButton.setOnAction(
                            event -> {
                                deleteClicked(p1);
                            }
                    );
                    editButton.setOnAction(
                            event -> {
                                editClicked(p1, false);
                            }
                    );
                    viewButton.setOnAction(
                            event -> {
                                editClicked(p1, true);
                            }
                    );
                }
            };
        });
        actionCol.setMinWidth(100);
        imageCol.setCellValueFactory(new PropertyValueFactory<>("photo"));
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        purchaseOrRentalPriceCol.setCellValueFactory(new PropertyValueFactory("purchaseOrRentalPrice"));
        table.getColumns().setAll(imageCol, typeCol, purchaseOrRentalPriceCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.refresh();

        tb.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> ov, Tab t, Tab t1) -> {
            tabPaneChanged(t1, typeCol);
        });
    }

    //delete clicked inside table column actions.
    private void deleteClicked(Property deleteProperty) {
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Delete");
        confirmDelete.setHeaderText("Are you sure you want to delete?");
        confirmDelete.showAndWait().filter(ButtonType.OK::equals).ifPresent((ButtonType b) -> {
            table.getItems().remove(deleteProperty);
            propertyList.remove(deleteProperty);
            propertiesData.remove(deleteProperty);
            if (deleteProperty == selection) {
                selection = null;
            }
        });
    }

    //modify clicked inside table column actions.
    private void editClicked(Property editProperty, boolean disabled) {
        try {
            if (disabled) {
                state = "view";
            } else {
                state = "edit";
            }
            property = editProperty;
            switch (editProperty.getType()) {
                case "Apartment":
                    editApartment();
                    break;
                case "House":
                    editPurchaseProperty(false);
                    break;
                default:
                    editPurchaseProperty(true);
                    break;
            }
        } catch (IOException ex) {
        }
    }

    //tab pane , user switching between viewing all properties, just apartments, just houses or just condos.
    private void tabPaneChanged(Tab t1, TableColumn typeCol) {
        tabId = t1.getId();
        switch (t1.getId().trim()) {
            case "all":
                setNewColumns("Purchase Price/Rental Fee", true, typeCol);
                break;
            case "apt":
                setNewColumns("Monthly Rental Fee", false, typeCol);
                break;
            case "condo":
                setNewColumns("Purchase Price", false, typeCol);
                break;
            case "house":
                setNewColumns("Purchase Price", false, typeCol);
                break;
            default:
                break;
        }
    }

    //when tab switched, change purchase price / monthly fee messages. Plus hide/show the type column.
    private void setNewColumns(String message, boolean visibility, TableColumn typeCol) {
        purchaseOrRentalPriceCol.setText("" + message);
        typeCol.setVisible(visibility);
        property = null;
        loadTable();
    }

    // prepopulate the table with data. At least one property for each type.
    private void setDummyData() {
        add(new Apartment("100 temple street", "New Haven", "CT", "06510", 2, 1, "My Apartment", "/icons/apartment.jpg", "Apartment",
                2500, 12));
        add(new House("200 george street", "New Haven", "CT", "06500", 2, 1, "My House", "/icons/house.jpg", "House",
                150000, 10000));
        add(new Condominium("200 Church street", "New York", "NY", "02500", 2, 1, "My Condominium", "/icons/condo.jpg", "Condominium",
                150000, 10000, 1200));
    }

    private void add(Property newProperty) {
        propertyList.add(newProperty);
        propertiesData.add(newProperty);
    }

    //set up the size for components on screen like scroll pane, table and tabpane.
    private void setupSize() {
        tb.setMinWidth(App.screenWidth - 30);
        tb.setMaxWidth(App.screenWidth - 30);
        sp.setMinWidth(App.screenWidth / 1.4);
        sp.setMaxWidth(App.screenWidth / 1.4);

        table.setPrefWidth(App.screenWidth / 1.4);
        table.setPrefHeight(App.screenHeight - 150);
        hbox.setMinWidth(App.screenWidth / 1.4);
        hbox.setMaxWidth(App.screenWidth / 1.4);
    }

    //create condo button clicked
    @FXML
    private void createCondo() throws IOException {
        property = new Condominium();
        state = "create";
        editPurchaseProperty(true);
    }

    //create house button clicked
    @FXML
    private void createHouse() throws IOException {
        property = new House();
        state = "create";
        editPurchaseProperty(false);
    }

    //create apartment button clicked
    @FXML
    private void createApartment() throws IOException {
        property = new Apartment();
        state = "create";
        editApartment();
    }

    //form to create or edit apartment.
    private void editApartment() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        createProperty();
        addLabelTextField("Monthly Fee:", tfMonthlyFeeOrPurchasePrice, 6);
        addLabelTextField("Number of Months Lease:", tfNumberOfMonthsLease, 7);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        setDescriptionTextField(8);
        Common.textFieldOnlyDouble(tfMonthlyFeeOrPurchasePrice);
        Common.textFieldOnlyInt(tfNumberOfMonthsLease);
        GridPane.setHalignment(submit, HPos.RIGHT);
        vBox.getChildren().add(gridPane);
        stage = new Stage();
        imageV.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!state.trim().equals("view")) {
                imageUri = Common.configureFileChooser(fileChooser, stage);
                Image photo = new Image(imageUri);
                imageV.setImage(photo);
            }

        });
        if (state.equals("create")) {
            tfMonthlyFeeOrPurchasePrice.setText("");
            tfNumberOfMonthsLease.setText("");
        } else if (state.equals("edit") || state.equals("view")) {
            Apartment apt = (Apartment) property;
            tfMonthlyFeeOrPurchasePrice.setText("" + apt.getMonthlyFee());
            tfNumberOfMonthsLease.setText("" + apt.getNumberOfMonthsLease());
        }
        submit.setOnAction(event -> {
            formSubmitted();
        });
        setEditCreateModal();
    }

    //form to create or edit purchase property like house or condo.
    private void editPurchaseProperty(boolean isCondo) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        createProperty();
        addLabelTextField("Purchase Price:", tfMonthlyFeeOrPurchasePrice, 6);
        addLabelTextField("Annual TAXES:", tfAnnualAmountOfTaxes, 7);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        if (isCondo) {
            addLabelTextField("Monthly Management Fee:", tfMonthlyManagementFee, 8);
            setDescriptionTextField(9);
            Common.textFieldOnlyDouble(tfMonthlyManagementFee);
        } else {
            setDescriptionTextField(8);
        }
        Common.textFieldOnlyDouble(tfMonthlyFeeOrPurchasePrice);
        Common.textFieldOnlyDouble(tfAnnualAmountOfTaxes);
        GridPane.setHalignment(submit, HPos.RIGHT);
        vBox.getChildren().add(gridPane);
        stage = new Stage();
        imageV.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!state.trim().equals("view")) {
                imageUri = Common.configureFileChooser(fileChooser, stage);
                Image photo = new Image(imageUri);
                imageV.setImage(photo);
            }
        });

        if (state.equals("create")) {
            tfMonthlyFeeOrPurchasePrice.setText("");
            tfAnnualAmountOfTaxes.setText("");
            if (isCondo) {
                tfMonthlyManagementFee.setText("");
            }
        } else if (state.equals("edit") || state.equals("view")) {
            PurchaseProperty apt = (PurchaseProperty) property;
            tfMonthlyFeeOrPurchasePrice.setText("" + apt.getPurchasePrice());
            tfAnnualAmountOfTaxes.setText("" + apt.getAnnualAmountOfTaxes());
            if (isCondo) {
                tfMonthlyManagementFee.setText("" + ((Condominium) apt).getMonthlyManagementFee());
            }
        }
        submit.setOnAction(event -> {
            formSubmitted();
        });
        setEditCreateModal();
    }

    //set up description text field in create/edit form.
    private void setDescriptionTextField(int row) {
        gridPane.add(new Label("Description:"), 0, row);
        gridPane.add(tfDescription, 1, row);
        if (!state.trim().equals("view")) {
            gridPane.add(submit, 1, (row + 1));
        }
        tfDescription.setPrefHeight(100);
        tfDescription.setPrefWidth(200);
        if (state.trim().equals("view")) {
            tfDescription.setEditable(false);
        } else {
            tfDescription.setEditable(true);
        }
    }

    //create, edit form modal setup and show.
    private void setEditCreateModal() {
        stage.setMinHeight(640);
        stage.setMinWidth(400);
        stage.setMaxHeight(640);
        stage.setMaxWidth(400);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    //set up property fields whren creating new or editing property.
    private void setPropertyFields(String imgResource, String street, String city, String state, String postalCode, String bedrooms, String bathrooms,
            String description) {
        imageUri = imgResource;
        tfStreetAddress.setText(street);
        tfCity.setText(city);
        tfState.setText(state);
        tfPostalCode.setText(postalCode);
        tfBedrooms.setText(bedrooms);
        tfBathrooms.setText(bathrooms);
        tfDescription.setText(description);
    }

    //create or edit property.
    private void createProperty() throws IOException {
        scene = new Scene(Common.loadFXML("addproperty"), 640, 400);
        imageV = (ImageView) scene.lookup("#image");
        if (state.equals("create")) {
            setPropertyFields("/icons/ic_image_placeholder.png", "", "", "", "", "", "", "");
        } else if (state.equals("edit") || state.equals("view")) {
            setPropertyFields(property.getImageResource(), property.getStreetAddress(),
                    property.getCity(), property.getState(), property.getPostalCode(), "" + property.getNumberOfBedrooms(),
                    "" + property.getNumberOfBathrooms(), property.getDescription());
        }
        imageV.setImage(new Image(imageUri));
        imageV.setPreserveRatio(true);
        imageV.setFitHeight(155);
        imageV.setFitWidth(310);
        imageV.setSmooth(true);
        imageV.setCache(true);
        vBox = (VBox) scene.lookup("#vbox");
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        addLabelTextField("Street Address:", tfStreetAddress, 0);
        addLabelTextField("City:", tfCity, 1);
        addLabelTextField("State:", tfState, 2);
        addLabelTextField("Postal Code:", tfPostalCode, 3);
        addLabelTextField("Number of Bedrooms:", tfBedrooms, 4);
        addLabelTextField("Number of Bathrooms:", tfBathrooms, 5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);
        Common.textFieldOnlyInt(tfBedrooms);
        Common.textFieldOnlyInt(tfBathrooms);
    }

    //adding label, textfields to create/edit form
    private void addLabelTextField(String labelValue, TextField tf, int row) {
        gridPane.add(new Label(labelValue), 0, row);
        gridPane.add(tf, 1, row);
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        if (state.trim().equals("view")) {
            tf.setEditable(false);
        } else {
            tf.setEditable(true);
        }
    }

    //checks if input is empty.
    private boolean checkTextFieldEmpty(StringBuilder sb, TextField tf, String errorMessage) {
        if (tf.getText().trim().equals("")) {
            sb.append(errorMessage);
            return true;
        }
        return false;
    }

    //checks if input is int number and not negative.
    private boolean checkTextFieldInputIntNumber(StringBuilder sb, TextField tf, String errorMessage1, String errorMessage2) {
        if (!Common.isNumeric(tf.getText().trim())) {
            sb.append(errorMessage1);
            return true;
        } else if (Integer.parseInt(tf.getText().trim()) < 0) {
            sb.append(errorMessage2);
            return true;
        }
        return false;
    }

    //checks if input is double number and not negative.
    private boolean checkTextFieldInputDoubleNumber(StringBuilder sb, TextField tf, String errorMessage1, String errorMessage2) {
        if (!Common.isNumericDouble(tf.getText().trim())) {
            sb.append(errorMessage1);
            return true;
        } else if (Double.parseDouble(tf.getText().trim()) < 0) {
            sb.append(errorMessage2);
            return true;
        }
        return false;
    }

    // submit button clicked when editing or creating property.all validation here.
    private void formSubmitted() {
        StringBuilder sb = new StringBuilder();
        checkTextFieldEmpty(sb, tfStreetAddress, "Enter a Street Address\n");
        checkTextFieldEmpty(sb, tfCity, "Enter a City\n");
        checkTextFieldEmpty(sb, tfState, "Enter a State\n");
        checkTextFieldEmpty(sb, tfPostalCode, "Enter a Postal Code\n");
        if (!checkTextFieldEmpty(sb, tfBedrooms, "Enter number of bedrooms\n")) {
            checkTextFieldInputIntNumber(sb, tfBedrooms,
                    "Number of bedrooms must be numeric.No Double or String.\n",
                    "Number of bedrooms must be equal or greater than zero\n");
        }
        if (!checkTextFieldEmpty(sb, tfBathrooms, "Enter number of bathrooms\n")) {
            checkTextFieldInputIntNumber(sb, tfBathrooms,
                    "Number of bathrooms must be numeric.No Double or String.\n",
                    "Number of bathrooms must be equal or greater than zero\n");
        }
        if (property.getType().equals("Apartment")) {
            if (!checkTextFieldEmpty(sb, tfMonthlyFeeOrPurchasePrice, "Enter monthly fee\n")) {
                checkTextFieldInputDoubleNumber(sb, tfMonthlyFeeOrPurchasePrice,
                        "Monthly Fee must be numeric.Not String.\n",
                        "Monthly Fee must be equal or greater than zero\n");
            }
            if (!checkTextFieldEmpty(sb, tfNumberOfMonthsLease, "Enter number of months to lease\n")) {
                checkTextFieldInputIntNumber(sb, tfNumberOfMonthsLease,
                        "Number of months to lease must be numeric.No Double or String.\n",
                        "Number of months to lease  must be equal or greater than zero\n");
            }
        } else if (property.getType().equals("House") || property.getType().equals("Condominium")) {
            if (!checkTextFieldEmpty(sb, tfMonthlyFeeOrPurchasePrice, "Enter purchase price\n")) {
                checkTextFieldInputDoubleNumber(sb, tfMonthlyFeeOrPurchasePrice,
                        "Purchase Price must be numeric.Not String.\n",
                        "Purchase must be equal or greater than zero\n");
            }
            if (!checkTextFieldEmpty(sb, tfAnnualAmountOfTaxes, "Enter annual amount of taxes\n")) {
                checkTextFieldInputDoubleNumber(sb, tfAnnualAmountOfTaxes,
                        "Annual amount of taxes  must be numeric.Not String\n",
                        "Annual amount of taxes must be equal or greater than zero\n");
            }

            if (property.getType().equals("Condominium")) {
                if (!checkTextFieldEmpty(sb, tfMonthlyManagementFee, "Enter Monthly Management fee\n")) {
                    checkTextFieldInputDoubleNumber(sb, tfMonthlyManagementFee,
                            "Monthly Management fee must be numeric.Not String.\n",
                            "Monthly Management fee  must be equal or greater than zero\n");
                }
            }
        }
        if (tfDescription.getText().trim().equals("")) {
            sb.append("Enter a description\n");
        }

        if (!sb.toString().trim().equals("")) {
            Common.showError(sb.toString());
        } else {
            //form validation successful, add property to list or edit.
            addEditProperty();
        }
    }

    //add new property to list, or edit property.
    private void addEditProperty() {
        switch (property.getType()) {
            case "Apartment":
                if (state.equals("create")) {
                    property = new Apartment(tfStreetAddress.getText().trim(), tfCity.getText().trim(),
                            tfState.getText().trim(), tfPostalCode.getText().trim(),
                            Integer.parseInt(tfBedrooms.getText().trim()), Integer.parseInt(tfBathrooms.getText().trim()),
                            tfDescription.getText().trim(), imageUri, "Apartment", Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()),
                            Integer.parseInt(tfNumberOfMonthsLease.getText().trim()));
                    add(property);
                } else if (state.equals("edit") || state.equals("view")) {
                    newPropertyValues("Apartment");
                    Apartment apt = (Apartment) property;
                    apt.setMonthlyFee(Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()));
                    apt.setNumberOfMonthsLease(Integer.parseInt(tfNumberOfMonthsLease.getText().trim()));
                }
                break;
            case "House":
                if (state.equals("create")) {
                    property = new House(tfStreetAddress.getText().trim(), tfCity.getText().trim(),
                            tfState.getText().trim(), tfPostalCode.getText().trim(),
                            Integer.parseInt(tfBedrooms.getText().trim()), Integer.parseInt(tfBathrooms.getText().trim()),
                            tfDescription.getText().trim(), imageUri, "House", Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()),
                            Double.parseDouble(tfAnnualAmountOfTaxes.getText().trim()));
                    add(property);
                } else if (state.equals("edit") || state.equals("view")) {
                    newPropertyValues("House");
                    House house = (House) property;
                    house.setPurchasePrice(Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()));
                    house.setAnnualAmountOfTaxes(Double.parseDouble(tfAnnualAmountOfTaxes.getText().trim()));
                }
                break;
            case "Condominium":
                if (state.equals("create")) {
                    property = new Condominium(tfStreetAddress.getText().trim(), tfCity.getText().trim(),
                            tfState.getText().trim(), tfPostalCode.getText().trim(),
                            Integer.parseInt(tfBedrooms.getText().trim()), Integer.parseInt(tfBathrooms.getText().trim()),
                            tfDescription.getText().trim(), imageUri, "Condominium", Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()),
                            Double.parseDouble(tfAnnualAmountOfTaxes.getText().trim()), Double.parseDouble(tfMonthlyManagementFee.getText().trim()));
                    add(property);
                } else if (state.equals("edit") || state.equals("view")) {
                    newPropertyValues("Condominium");
                    Condominium condominium = (Condominium) property;
                    condominium.setPurchasePrice(Double.parseDouble(tfMonthlyFeeOrPurchasePrice.getText().trim()));
                    condominium.setAnnualAmountOfTaxes(Double.parseDouble(tfAnnualAmountOfTaxes.getText().trim()));
                    condominium.setMonthlyManagementFee(Double.parseDouble(tfMonthlyManagementFee.getText().trim()));
                }
                break;
            default:
                break;
        }
        stage.close();
        loadTable();
    }

    //Set Property values when editing or creating.
    private void newPropertyValues(String type) {
        property.setStreetAddress(tfStreetAddress.getText().trim());
        property.setCity(tfCity.getText().trim());
        property.setState(tfState.getText().trim());
        property.setPostalCode(tfPostalCode.getText().trim());
        property.setNumberOfBedrooms(Integer.parseInt(tfBedrooms.getText().trim()));
        property.setNumberOfBathrooms(Integer.parseInt(tfBathrooms.getText().trim()));
        property.setDescription(tfDescription.getText().trim());
        property.setImageResource(imageUri);
        property.setPurchaseOrRentalPrice(tfMonthlyFeeOrPurchasePrice.getText().trim());
        property.setType(type);
    }

    //load table, like refresh.
    public void loadTable() {
        for (int i = 0; i < propertiesData.size(); i++) {
            table.getItems().remove(propertiesData.get(i));
        }
        propertiesData.clear();
        for (int i = 0; i < propertyList.size(); i++) {
            switch (tabId.trim()) {
                case "all":
                    propertiesData.add(propertyList.get(i));
                    break;
                case "apt":
                    if (propertyList.get(i).getType().equals("Apartment")) {
                        propertiesData.add(propertyList.get(i));
                    }
                    break;
                case "condo":
                    if (propertyList.get(i).getType().equals("Condominium")) {
                        propertiesData.add(propertyList.get(i));
                    }
                    break;
                case "house":
                    if (propertyList.get(i).getType().equals("House")) {
                        propertiesData.add(propertyList.get(i));
                    }
                    break;
                default:
                    break;
            }
        }
        table.refresh();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(Common.loadFXML(fxml));
    }

    public static void main(String[] args) {
        launch();
    }
}
