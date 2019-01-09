package client_desktop.view;

import client_desktop.model.PipeGameModel;
import client_desktop.viewModel.PipeGameViewModel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            PipeGameModel pipeGameModel = new PipeGameModel();
            PipeGameViewModel pipeGameViewModel = new PipeGameViewModel(pipeGameModel);
            FXMLLoader fxl = new FXMLLoader();
            BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
            MainWindowController mainWindowController = fxl.getController();
            mainWindowController.setViewModel(pipeGameViewModel);
            pipeGameModel.initBoard();
            primaryStage.setOnCloseRequest((event -> mainWindowController.exit()));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
