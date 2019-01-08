package client_desktop.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PipeDisplayer extends Canvas {

    int [][] pipeData;

    private StringProperty backgroundFileName;

    public PipeDisplayer() {
        this.backgroundFileName = new SimpleStringProperty();
    }

    public String getBackgroundFileName() {
        return backgroundFileName.getValue();
    }

    public void setBackgroundFileName(String background) {
        this.backgroundFileName.setValue(background);;
    }

    public void setPipeData(int [][] pipeData) {
        this.pipeData = pipeData;
        this.boardDraw();
    }

    public void boardDraw() {
        if (pipeData != null) {
            double tempWidth = getWidth();
            double tempHeight = getHeight();
            double width = tempWidth / pipeData[0].length;
            double height = tempHeight / pipeData.length;

            GraphicsContext gc = getGraphicsContext2D();

            for (int column = 0; column < pipeData.length; column++) {
                for (int row = 0; row < pipeData[column].length; row++) {
                    if (pipeData[column][row] != 0) {
                        try {
                            gc.drawImage(new Image(new FileInputStream(backgroundFileName.get())), row*width, column*height, width, height);
                        } catch (FileNotFoundException e) {
                            gc.fillRect(row*width, column*height, width, height);
                        }
                    }
                }
            }

        }
    }
}
