package client_desktop.view;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PipeDisplayer extends Canvas {

    char [][] pipeData = {
            {'s', '-', '-', '-', '7', '7', '-', '|' , 'F'},
            {'-', '7', '7', '7', '|', 'L', '-', 'F' , '7'},
            {'-', 'L', '7', '7', '|', '7', '|', '7' , 'L'},
            {'|', '-', '7', '7', '|', 'F', '|', '|' , 'L'},
            {'F', 'F', '7', '7', '|', '7', '7', '7' , '7'},
            {'L', 'F', '7', 'J', 'L', '-', '-', '-' , 'g'},
    };

    private StringProperty startFileName;
    private StringProperty goalFileName;
    private StringProperty angledPipeFileName;
    private StringProperty verticalPipeFileName;
    private StringProperty backgroundFileName;
    ListProperty<Point> passedPipes;


    // We create private plants so we wont need to have multiple plants of vertical pipe, we will just rotate it.
    private Image backgroundImage;
    private Image startImage;
    private Image goalImage;
    private Image pipeVerticalImage;
    private Image pipeHorizontalImage;
    private Image pipe0DegreeImage;
    private Image pipe90DegreeImage;
    private Image pipe180DegreeImage;
    private Image pipe270DegreeImage;

    public PipeDisplayer() {
        this.backgroundFileName = new SimpleStringProperty();
        this.startFileName = new SimpleStringProperty();
        this.goalFileName = new SimpleStringProperty();
        this.angledPipeFileName = new SimpleStringProperty();
        this.verticalPipeFileName = new SimpleStringProperty();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public double maxHeight(double width) {
        return 9999;
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double maxWidth(double height) {
        return 9999;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
        this.boardDraw();
    }

    public char[][] getPipeData() {
        return pipeData;
    }

    public String getStartFileName() {
        return startFileName.get();
    }

    public StringProperty startFileNameProperty() {
        return startFileName;
    }

    public void setStartFileName(String startFileName) {
        this.startFileName.set(startFileName);
    }

    public String getGoalFileName() {
        return goalFileName.get();
    }

    public StringProperty goalFileNameProperty() {
        return goalFileName;
    }

    public void setGoalFileName(String goalFileName) {
        this.goalFileName.set(goalFileName);
    }

    public String getAngledPipeFileName() {
        return angledPipeFileName.get();
    }

    public StringProperty angledPipeFileNameProperty() {
        return angledPipeFileName;
    }

    public void setAngledPipeFileName(String angledPipeFileName) {
        this.angledPipeFileName.set(angledPipeFileName);
    }

    public String getVerticalPipeFileName() {
        return verticalPipeFileName.get();
    }

    public StringProperty verticalPipeFileNameProperty() {
        return verticalPipeFileName;
    }

    public void setVerticalPipeFileName(String verticalPipeFileName) {
        this.verticalPipeFileName.set(verticalPipeFileName);
    }

    public StringProperty backgroundFileNameProperty() {
        return backgroundFileName;
    }

    public String getBackgroundFileName() {
        return backgroundFileName.getValue();
    }

    public void setBackgroundFileName(String background) {
        this.backgroundFileName.setValue(background);
    }

    public void setPassedPipes(ListProperty<Point> passedPipes) {
        this.passedPipes = passedPipes;
    }

    public void setPipeData(char[][] pipeData) {
        if (pipeData != null) {
            this.pipeData = pipeData;
            this.boardDraw();
        }
    }

    public void cleanBoard() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    public void imagesInit() throws IOException {
        try {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            // Get the base plants, we will rotate soon
            backgroundImage = new Image(getClass().getResource(backgroundFileName.get()).openStream());
            startImage = new Image(getClass().getResource(startFileName.get()).openStream());
            goalImage = new Image(getClass().getResource(goalFileName.get()).openStream());
            pipeVerticalImage = new Image(getClass().getResource(verticalPipeFileName.get()).openStream());

            ImageView imageView = new ImageView(pipeVerticalImage);
            // We rotate 90 degrees to get the horizontal state
            imageView.setRotate(90);
            pipeHorizontalImage = imageView.snapshot(params, null);

            pipe0DegreeImage = new Image(getClass().getResource(angledPipeFileName.get()).openStream());
            imageView = new ImageView(pipe0DegreeImage);
            imageView.setRotate(90);
            pipe90DegreeImage = imageView.snapshot(params, null);
            imageView.setRotate(180);
            pipe180DegreeImage = imageView.snapshot(params, null);
            imageView.setRotate(270);
            pipe270DegreeImage = imageView.snapshot(params, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void boardDraw() {
        if (pipeData != null) {
            cleanBoard();

            double tempWidth = getWidth();
            double tempHeight = getHeight();
            double width = tempWidth / pipeData[0].length;
            double height = tempHeight / pipeData.length;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.drawImage(backgroundImage, 0, 0, getWidth(), getHeight());

            for (int column = 0; column < pipeData.length; column++) {
                for (int row = 0; row < pipeData[column].length; row++) {
                    Image pipeImage;
                    switch (pipeData[column][row]) {
                        case 'L':
                            pipeImage = pipe180DegreeImage;
                            break;
                        case 'F':
                            pipeImage = pipe270DegreeImage;
                            break;
                        case '7':
                            pipeImage = pipe0DegreeImage;
                            break;
                        case 'J':
                            pipeImage = pipe90DegreeImage;
                            break;
                        case '-':
                            pipeImage = pipeHorizontalImage;
                            break;
                        case '|':
                            pipeImage = pipeVerticalImage;
                            break;
                        case 's':
                            pipeImage = startImage;
                            break;
                        case 'g':
                            pipeImage = goalImage;
                            break;
                        case ' ':
                            pipeImage = null;
                            break;
                        default:
                            pipeImage = null;
                            break;
                    }

                    if (pipeImage != null) {
                        if (null != passedPipes && passedPipes.contains(new Point(row, column))) {
                            graphicsContext.save();
                            Bloom bloom = new Bloom();
                            bloom.setThreshold(0.5);
                            graphicsContext.setEffect(bloom);
                            graphicsContext.drawImage(pipeImage, row * width, column * height, width, height);
                            graphicsContext.restore();
                        } else {
                            graphicsContext.drawImage(pipeImage, row * width, column * height, width, height);
                        }
                    }
                }
            }
        }
    }

}
