package client_desktop.view.Messeges;

import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class NakedObjectDisplayer {

    VBox dialogVbox;
    public void display(NakedObject obj) {

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        this.dialogVbox = new VBox(obj.getFieldNames().size() * 10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));

        List<StringProperty> textPropertyList = new ArrayList<>();
        for (String fieldName: obj.getFieldNames()) {
            String fieldValue = obj.getFieldValue(fieldName);
            textPropertyList.add(createDisplayForField(fieldName, fieldValue));
        }
        Button saveButton = new Button();
        saveButton.setText("Apply");
        saveButton.setStyle("-fx-font: 16 arial;");
        saveButton.setOnAction(value ->  {
            for (int i=0; i< obj.getFieldNames().size(); i++) {
                obj.fieldChanged(obj.getFieldNames().get(i), textPropertyList.get(i).get());
            }
            dialog.close();
        });

        this.dialogVbox.getChildren().add(saveButton);


        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.show();


    }

    private StringProperty createDisplayForField(String fieldName, String fieldValue) {
        //create UI element and bind to it using the NakedObject methods - this I'm leaving to you :)
        //This should obviously involve JavaFX code.
        Text caption = new Text(fieldName);
        caption.setStyle("-fx-font: 16 arial;");
        this.dialogVbox.getChildren().add(caption);
        TextField textBox = new TextField(fieldValue);
        textBox.setStyle("-fx-font: 16 arial;");
        this.dialogVbox.getChildren().add(textBox);
        return textBox.textProperty();
    }
}