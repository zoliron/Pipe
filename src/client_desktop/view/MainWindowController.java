package client_desktop.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class MainWindowController implements Initializable {

    int [][] pipeData = {
            {1,0,1,0,1,0,1,0,1},
            {0,1,0,1,0,1,0,1,0},
            {1,0,1,0,1,0,1,0,1},
            {0,1,0,1,0,1,0,1,0},
            {1,0,1,0,1,0,1,0,1},
            {0,1,0,1,0,1,0,1,0}
    };

    @FXML
    PipeDisplayer pipeDisplayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pipeDisplayer.setPipeData(pipeData);
    }

    public void start() {
        System.out.println("Start clicked");
    }

    public void stop() {
        System.out.println("Stop clicked");
    }

    public void exit() {
        System.out.println("Exiting");
        System.exit(0);
    }

    public void openFile() {
        System.out.println("Open File.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Pipe File");
        fileChooser.setInitialDirectory(new File("./resources"));

        FileChooser.ExtensionFilter xmlExtensionFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(xmlExtensionFilter);
        fileChooser.setSelectedExtensionFilter(xmlExtensionFilter);
        File chosenFile = fileChooser.showOpenDialog(null);

        if (chosenFile != null) {
            System.out.println(chosenFile.getName());
        }
        else {
            System.out.println("Test");
        }

    }
}