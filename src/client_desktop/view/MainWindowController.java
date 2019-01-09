package client_desktop.view;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client_desktop.view.Messeges.NakedMessage;
import client_desktop.view.Messeges.NakedObjectDisplayer;
import client_desktop.view.Messeges.ThemeConfiguration;
import client_desktop.view.Messeges.ServerConfiguration;
import client_desktop.viewModel.PipeGameViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.scene.media.AudioClip.INDEFINITE;

public class MainWindowController implements Initializable {

    String currentTheme;
    PipeGameViewModel pipeGameViewModel;
    ListProperty<Point> passedPipes;
    BooleanProperty isGoalState;
    IntegerProperty stepsNumber;
    ListProperty<char[]> pipeGameBoard;
    IntegerProperty timePassed;
    String gameMusic;
    AudioClip music;


    NakedObjectDisplayer nakedObjectDisplayer = new NakedObjectDisplayer();
    ServerConfiguration serverConfiguration = new ServerConfiguration();
    ThemeConfiguration themeConfiguration = new ThemeConfiguration();

    @FXML
    PipeDisplayer pipeDisplayer;
    @FXML
    javafx.scene.control.TextField stepsText;
    @FXML
    javafx.scene.control.TextField secondsText;
    @FXML
    Label connectionStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setting the default theme before loading the proper images
        changeTheme("plumber");
        try {
            pipeDisplayer.imagesInit();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setViewModel(PipeGameViewModel pipeGameViewModel) {
        this.pipeGameViewModel = pipeGameViewModel;
        this.pipeGameBoard = new SimpleListProperty<>();
        this.pipeGameBoard.bind(this.pipeGameViewModel.pipeGameBoard);
        this.pipeGameBoard.addListener((observableValue, s, t1) -> {
            pipeDisplayer.setPipeData(this.pipeGameBoard.toArray(new char[this.pipeGameBoard.size()][]));
        });
        this.isGoalState = new SimpleBooleanProperty();
        this.isGoalState.bind(this.pipeGameViewModel.isGoalState);
        // Listener to check if we reached to goal state
        this.isGoalState.addListener((observableValue, s, t1) -> {
            if (isGoalState.get() == true) {
                System.out.println("You won !");
                NakedMessage newMessage = new NakedMessage("Congratulations");
                newMessage.addMessage("Time Passed: " + timePassed.get());
                newMessage.addMessage("Moves Count: " + stepsNumber.get());
                nakedObjectDisplayer.display(newMessage);
            }
        });
        this.passedPipes = new SimpleListProperty<Point>();
        this.passedPipes.bind(this.pipeGameViewModel.passedPipes);
        this.passedPipes.addListener((observableValue, s, t1) -> {
            pipeDisplayer.setPassedPipes(this.passedPipes);
        });

        // Enables clicks for the pipes to rotate
        pipeDisplayer.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent t) -> {
                    double w = pipeDisplayer.getWidth() / pipeGameBoard.get(0).length;
                    double h = pipeDisplayer.getHeight() / pipeGameBoard.size();
                    int x = (int) (t.getX() / w);
                    int y = (int) (t.getY() / h);
                    pipeGameViewModel.rotateCell(x, y);
                }
        );

        this.stepsNumber = new SimpleIntegerProperty();
        this.stepsNumber.bindBidirectional(this.pipeGameViewModel.stepsNumber);
        this.stepsNumber.addListener((observableValue, s, t1) -> {
            this.stepsText.setText(Integer.toString(stepsNumber.get()));
        });
        this.connectionStatus.setText("Server Disconnected");

        this.timePassed = new SimpleIntegerProperty();
        this.timePassed.bind(this.pipeGameViewModel.timePassed);
        this.timePassed.addListener((observableValue, s, t1) -> {
            this.secondsText.setText(Integer.toString(timePassed.get()));
        });
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
        fileChooser.setInitialDirectory(new File("/client_desktop/resources/"));

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
        pipeDisplayer.setBackgroundFileName("/client_desktop/resources/" + themeName + "/background.png");
        pipeDisplayer.setStartFileName("/client_desktop/resources/" + themeName + "/start.png");
        pipeDisplayer.setGoalFileName("/client_desktop/resources/" + themeName + "/goal.png");
        pipeDisplayer.setAngledPipeFileName("/client_desktop/resources/" + themeName + "/pipeAngle.png");
        pipeDisplayer.setVerticalPipeFileName("/client_desktop/resources/" + themeName + "/pipeVertical.png");
        this.currentTheme = themeName;
        this.gameMusic = "/client_desktop/resources/" + themeName + "/music.mp3";
        try {
            pipeDisplayer.imagesInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pipeDisplayer.boardDraw();
        this.playMusic();
    }


    public void serverConfig(ActionEvent actionEvent) {
        nakedObjectDisplayer.display(this.serverConfiguration);
    }

    public void themeConfig() {
        ComboBox<String> comboBox = nakedObjectDisplayer.display(this.themeConfiguration);
        comboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable,
                 String oldValue, String newValue) -> changeTheme(newValue));
    }

    void playMusic() {
        if (null != this.music) {
            music.stop();
        }
        this.music = new AudioClip(getClass().getResource(this.gameMusic).toExternalForm());
        music.setCycleCount(INDEFINITE);
        music.play();
    }
    public void solve() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    System.out.println("Trying to solve...");
                    Platform.runLater(()->connectionStatus.setText("Server Status: Connecting to " + serverConfiguration.ServerIP + ":" + serverConfiguration.ServerPort));
                    pipeGameViewModel.connectServer(serverConfiguration.ServerIP, serverConfiguration.ServerPort);
                    pipeGameViewModel.solve();
                    pipeGameViewModel.disconnectServer();
                    Platform.runLater(()->connectionStatus.setText("Server Disconnected"));
                } catch (IOException e) {
                    Platform.runLater(()->connectionStatus.setText("Connection Error"));
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public void reset(ActionEvent actionEvent) {
    }


}