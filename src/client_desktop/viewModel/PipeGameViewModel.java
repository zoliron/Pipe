package client_desktop.viewModel;

import java.awt.*;
import java.io.IOException;

import client_desktop.model.PipeGameModel;
import javafx.beans.property.*;

import java.io.File;


public class PipeGameViewModel {

    public ListProperty<char[]> pipeGameBoard;
    public BooleanProperty isGoalState;
    public ListProperty<Point> passedPipes;
    public IntegerProperty stepsNumber;
    public IntegerProperty timePassed;
    PipeGameModel pipeGameModel;

    public PipeGameViewModel(PipeGameModel pipeGameModel) {
        this.pipeGameModel = pipeGameModel;
        this.pipeGameBoard = new SimpleListProperty<>();
        this.pipeGameBoard.bind(this.pipeGameModel.pipeGameBoard);
        this.isGoalState = new SimpleBooleanProperty();
        this.isGoalState.bind(this.pipeGameModel.isGoalState);
        this.passedPipes = new SimpleListProperty<>();
        this.passedPipes.bind(this.pipeGameModel.passedPipes);
        this.passedPipes = new SimpleListProperty<>();
        this.passedPipes.bind(this.pipeGameModel.passedPipes);
        this.stepsNumber = new SimpleIntegerProperty();
        this.stepsNumber.bindBidirectional(this.pipeGameModel.stepsNumber);
        this.timePassed = new SimpleIntegerProperty();
        this.timePassed.bind(this.pipeGameModel.timePassed);
    }

    public void rotateCell(int x, int y) {
        this.pipeGameModel.rotateCell(x, y);
    }

    public void loadFile(String fileName) {
        this.pipeGameModel.loadFile(fileName);
    }

    public void saveFile(File file) {
        this.pipeGameModel.saveFile(file);
    }

    public void connectServer(String serverIp, String serverPort) throws IOException {
        this.pipeGameModel.connectServer(serverIp, serverPort);
    }

    public void exit() throws IOException {
        this.pipeGameModel.exit();
    }

    public void solve() throws IOException, InterruptedException {
        this.pipeGameModel.solve();
    }
    public void resetStats() {
        this.pipeGameModel.resetStats();
    }

    public void disconnectServer() throws IOException {
        this.pipeGameModel.disconnectServer();
    }
}
