package client_desktop.view.Messeges;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NakedObjectDisplayer {

    private VBox dialogVbox;

    Stage displayConstructor() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog;
    }

    void displayDistructor(Stage dialog, VBox dialogVbox) {
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.show();
    }

    public void display(NakedObject obj) {

        final Stage dialog = this.displayConstructor();

        Field[] fields = obj.getClass().getFields();
        this.dialogVbox = new VBox(fields.length * 10);
        dialogVbox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        List<StringProperty> textPropertyList = new ArrayList<>();
        for (Field field : fields) {
            try {
                textPropertyList.add(createDisplayForField(field.getName(), (String) field.get(obj)));
            } catch (IllegalArgumentException | IllegalAccessException e) {

            }
        }
        javafx.scene.control.Button saveButton = new javafx.scene.control.Button();
        saveButton.setText("Apply");
        saveButton.setStyle("-fx-font: 16 arial;");
        saveButton.setOnAction(value -> {
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].set(obj, textPropertyList.get(i).get());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                }
            }
            dialog.close();
        });

        this.dialogVbox.getChildren().add(saveButton);

        displayDistructor(dialog, this.dialogVbox);
    }

    private StringProperty createDisplayForField(String fieldName, String fieldValue) {
        //create UI element and bind to it using the NakedObject methods - this I'm leaving to you :)
        //This should obviously involve JavaFX code.
        Text caption = new Text(fieldName);
        caption.setStyle("-fx-font: 16 arial;");
        this.dialogVbox.getChildren().add(caption);
        javafx.scene.control.TextField textBox = new javafx.scene.control.TextField(fieldValue);
        textBox.setStyle("-fx-font: 16 arial;");
        this.dialogVbox.getChildren().add(textBox);
        return textBox.textProperty();
    }

    public void display(NakedMessage obj) {

        final Stage dialog = this.displayConstructor();

        this.dialogVbox = new VBox(obj.getMessageList().size() * 10);
        dialogVbox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        for (String message : obj.getMessageList()) {
            Text caption = new Text(message);
            caption.setStyle("-fx-font: 16 arial;");
            this.dialogVbox.getChildren().add(caption);
        }

        javafx.scene.control.Button saveButton = new Button();
        saveButton.setText("OK");
        saveButton.setStyle("-fx-font: 16 arial;");
        saveButton.setOnAction(value -> dialog.close());

        this.dialogVbox.getChildren().add(saveButton);

        displayDistructor(dialog, this.dialogVbox);
    }

    public ComboBox<String> display(NakedList obj) {

        final Stage dialog = this.displayConstructor();

        this.dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));

        Text caption = new Text("Choose:");
        caption.setStyle("-fx-font: 16 arial;");
        this.dialogVbox.getChildren().add(caption);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(obj.getOptions());

        this.dialogVbox.getChildren().add(comboBox);

        displayDistructor(dialog, this.dialogVbox);
        return comboBox;
    }
}