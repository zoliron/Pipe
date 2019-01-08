package client_desktop.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class MainWindowController implements Initializable {

    String currentTheme;

    char [][] pipeData = {
            {'s', '-', '-', '-', '7', '7', '-', '|' , 'F'},
            {'-', '7', '7', '7', '|', 'L', '-', 'F' , '7'},
            {'-', 'L', '7', '7', '|', '7', '|', '7' , 'L'},
            {'|', '-', '7', '7', '|', 'F', '|', '|' , 'L'},
            {'F', 'F', '7', '7', '|', '7', '7', '7' , '7'},
            {'F', 'L', '-', '-', 'L', '-', '-', '-' , 'g'},
    };

    @FXML
    PipeDisplayer pipeDisplayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeTheme("plants");
        try {
            pipeDisplayer.imagesInit();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

        FileChooser.ExtensionFilter textExtensionFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(textExtensionFilter);
        fileChooser.setSelectedExtensionFilter(textExtensionFilter);
        File chosenFile = fileChooser.showOpenDialog(null);

        if (chosenFile != null) {
            System.out.println(chosenFile.getName());
            List<char[]> lines = new ArrayList<char[]>();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(chosenFile));

                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.toCharArray());
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            char[][] charArray = lines.toArray(new char[lines.size()][]);
            pipeDisplayer.setPipeData(charArray);
        }
        else {
            System.out.println("Test");
        }

    }

    void changeTheme(String themeName) {
        if (themeName.equals(this.currentTheme)) {
            System.out.println("Theme is ok");
            return;
        }
        System.out.println("Theme chosen: " + themeName);
        pipeDisplayer.setBackgroundFileName("src/client_desktop/resources/" + themeName + "/background.png");
        pipeDisplayer.setStartFileName("src/client_desktop/resources/" + themeName + "/start.png");
        pipeDisplayer.setGoalFileName("src/client_desktop/resources/" + themeName + "/goal.png");
        pipeDisplayer.setAngledPipeFileName("src/client_desktop/resources/" + themeName + "/pipeAngle.png");
        pipeDisplayer.setVerticalPipeFileName("src/client_desktop/resources/" + themeName + "/pipeVertical.png");
        this.currentTheme = themeName;
        // Configure the music path
        try {
            pipeDisplayer.imagesInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pipeDisplayer.boardDraw();
        // Start the music
    }


    public void serverConfig(ActionEvent actionEvent) {
    }

    public void themeConfig(ActionEvent actionEvent) {
    }

    public void solve(ActionEvent actionEvent) {
    }

    public void reset(ActionEvent actionEvent) {
    }
}