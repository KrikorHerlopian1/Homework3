/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

Common.java
Common functions implemented in this class

 */
package com.mycompany.kherl1_6617_hw3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author krikorherlopian
 */
public final class Common {

    //with this function, you disable user enter anything other than double number.
    static void textFieldOnlyDouble(TextField tx) {
        tx.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                tx.setText(oldValue);
            }
        });
    }

    //with this function, you disable user enter anything other than int number.
    static void textFieldOnlyInt(TextField tx) {
        tx.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,7}")) {
                tx.setText(oldValue);
            }
        });
    }

    //function that check if string is of type double.
    static boolean isNumericDouble(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    //function that check if string is of type int.
    static boolean isNumeric(String strNum) {
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    //format a double number into two places
    static String format(double number) {
        number = Math.round(number * 100);
        number = number / 100;
        return String.format("%.2f", number);
    }

    //copy from one source file to destination file
    static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    //alert dialog in case wrong submission, with error messages.
    static void showError(String errorMessage) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error Submitting Form");
        a.setHeaderText("" + errorMessage);
        a.show();
    }
    
    
    //when file selected, store in icons folder.and return path to it.
    //UUID.randomUUID() generates random number before the name of the file so that in case two files same it does not overwrite.
    static String openFile(File file) {
        File dest = new File(System.getProperty("user.dir") + "/src/main/resources/icons/" + UUID.randomUUID() + "-" + file.getName());
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            Common.copyFileUsingStream(file, dest);
        } catch (IOException e) {
        }
        return dest.toURI().toString();
    }

    //file chooser configuration with certain types allowed only ( images).
    //it will return path in icons folder stored.
    static String configureFileChooser(FileChooser fileChooser, Stage stage) {
        fileChooser.setTitle("Choose Picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return openFile(file);
        }
        return "";
    }
    
     static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
