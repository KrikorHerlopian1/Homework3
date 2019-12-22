module com.mycompany.kherl1_6617_hw3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.kherl1_6617_hw3 to javafx.fxml;
    exports com.mycompany.kherl1_6617_hw3;
}