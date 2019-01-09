package client_desktop.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Timer;
import java.util.TimerTask;

public class PipeGameModel implements GameModel {

    public BooleanProperty isGoalState;
    public ListProperty<Point> passedPipes;
    public IntegerProperty stepsNumber;
    public IntegerProperty timePassed;
    public char[][] clean;
    private Socket serverSocket;
    public ListProperty<char[]> pipeGameBoard;
    private Point startPosition = null;
    private Point goalPosition = null;

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            timePassed.set(timePassed.get() + 1);
        }
    };

    public PipeGameModel() {
        this.pipeGameBoard = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.pipeGameBoard.addListener((ObservableValue, s, t1) ->{
            char[][] cells = this.pipeGameBoard.toArray(new char[this.pipeGameBoard.size()][]);
            for(int i=0; i < cells.length; i++) {
                for(int j=0; j< cells[i].length; j++) {
                    if (cells[i][j] == 's')
                        startPosition = new Point(i, j);
                    if (cells[i][j] == 'g')
                        goalPosition = new Point(i, j);
                }
            }

            if (startPosition != null) {
                passedPipes.clear();
                isGoalState.set(isGoalStateCheck(startPosition, goalPosition));
            }
        });

        this.isGoalState = new SimpleBooleanProperty();
        this.passedPipes = new SimpleListProperty<>(FXCollections.observableArrayList(new LinkedHashSet<Point>()));
        this.stepsNumber = new SimpleIntegerProperty(0);
        this.timePassed = new SimpleIntegerProperty(0);
        this.timer.scheduleAtFixedRate(task, 1000,  1000);
    }

    private boolean isGoalStateCheck(Point start, Point goal) {
        return isGoalFound(start, null, goal);
    }

    public boolean isGoalFound(Point current, Point cameFrom, Point goal)
    {
        if (current == null)
            return false;

        if (current == startPosition && cameFrom != null)
            return false;

        if (current == goalPosition) {
            this.isGoalState.set(true);
            return true;
        }

        return ((getTop(current) != cameFrom && isTopPossible(current) && isGoalFound(getTop(current), current, goal))    ||
                (getBottom(current) != cameFrom && isBottomPossible(current) && isGoalFound(getBottom(current), current, goal)) ||
                (getRight(current) != cameFrom && isRightPossible(current) && isGoalFound(getRight(current), current, goal))  ||
                (getLeft(current) != cameFrom && isLeftPossible(current) && isGoalFound(getLeft(current), current, goal)));
    }

    public boolean isTopPossible(Point cell) {
        if (getTop(cell) != null) {
            switch (pipeGameBoard.get(cell.y)[cell.x]) {
                case '|':
                    if ((pipeGameBoard.get(cell.y -1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == '7') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'F') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'J':
                    if ((pipeGameBoard.get(cell.y -1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == '7') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'F') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'L':
                    if ((pipeGameBoard.get(cell.y -1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == '7') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'F') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((pipeGameBoard.get(cell.y -1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == '7') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'F'))
                        return true;
                    return false;
                case 'g':
                    if ((pipeGameBoard.get(cell.y -1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == '7') ||
                            (pipeGameBoard.get(cell.y -1)[cell.x] == 'F'))
                        return true;
                    return false;

                default:
                    return false;
            }
        }
        return false;
    }

    public boolean isBottomPossible(Point cell) {
        if (getBottom(cell) != null) {
            switch (pipeGameBoard.get(cell.y)[cell.x]) {
                case '|':
                    if ((pipeGameBoard.get(cell.y +1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'J') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'L') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'F':
                    if ((pipeGameBoard.get(cell.y +1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'J') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'L') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case '7':
                    if ((pipeGameBoard.get(cell.y +1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'J') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'L') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 's') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((pipeGameBoard.get(cell.y +1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'J') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'L'))
                        return true;
                    return false;
                case 'g':
                    if ((pipeGameBoard.get(cell.y +1)[cell.x] == '|') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'J') ||
                            (pipeGameBoard.get(cell.y +1)[cell.x] == 'L'))
                        return true;
                    return false;

                default:
                    return false;
            }
        }
        return false;
    }

    public boolean isLeftPossible(Point cell) {
        if (getLeft(cell) != null) {
            switch (pipeGameBoard.get(cell.y)[cell.x]) {
                case '-':
                    if ((pipeGameBoard.get(cell.y)[cell.x -1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'F') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'L') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case '7':
                    if ((pipeGameBoard.get(cell.y)[cell.x -1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'F') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'L') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case 'J':
                    if ((pipeGameBoard.get(cell.y)[cell.x -1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'F') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'L') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((pipeGameBoard.get(cell.y)[cell.x -1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'F') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'L'))
                        return true;
                    return false;
                case 'g':
                    if ((pipeGameBoard.get(cell.y)[cell.x -1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'F') ||
                            (pipeGameBoard.get(cell.y)[cell.x -1] == 'L'))
                        return true;
                    return false;

                default:
                    return false;
            }
        }
        return false;
    }

    public boolean isRightPossible(Point cell) {
        if (getLeft(cell) != null) {
            switch (pipeGameBoard.get(cell.y)[cell.x]) {
                case '-':
                    if ((pipeGameBoard.get(cell.y)[cell.x +1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'J') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == '7') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 'F':
                    if ((pipeGameBoard.get(cell.y)[cell.x +1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'J') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == '7') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 'L':
                    if ((pipeGameBoard.get(cell.y)[cell.x +1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'J') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == '7') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 's') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((pipeGameBoard.get(cell.y)[cell.x +1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'J') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == '7'))
                        return true;
                    return false;
                case 'g':
                    if ((pipeGameBoard.get(cell.y)[cell.x +1] == '-') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == 'J') ||
                            (pipeGameBoard.get(cell.y)[cell.x +1] == '7'))
                        return true;
                    return false;
                default:
                    return false;
            }
        }
        return false;
    }

    public Point getTop(Point current) {
        Point temp = null;
        if(current.y > 0)
            temp = new Point((int)current.getX(), (int)current.getY() - 1);

        return temp;
    }

    public Point getBottom(Point current) {
        Point temp = null;
        if(current.y < this.pipeGameBoard.size() - 1)
            temp = new Point((int)current.getX(), (int)current.getY() + 1);

        return temp;
    }

    public Point getLeft(Point current) {
        Point temp = null;
        if(current.x > 0)
            temp = new Point((int)current.getX() -1, (int)current.getY());

        return temp;
    }

    public Point getRight(Point current) {
        Point temp = null;
        if(current.x < this.pipeGameBoard.get(0).length - 1)
            temp = new Point((int)current.getX() +1, (int)current.getY());

        return temp;
    }

    public void initBoard() {
        char[][] level = {
                {'s','F',' ','-','L'},
                {' ','J','J','-','L'},
                {' ','F','|','-','L'},
                {' ','L','L','-','g'},
        };

        setCleanBoard(level);
        this.pipeGameBoard.addAll(level);
    }

    public void rotateCell(int x, int y) {
        switch (this.pipeGameBoard.get(y)[x]) {
            case '-':
                this.pipeGameBoard.get(y)[x] = '|';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case '7':
                this.pipeGameBoard.get(y)[x] = 'J';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'F':
                this.pipeGameBoard.get(y)[x] = '7';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'L':
                this.pipeGameBoard.get(y)[x] = 'F';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'J':
                this.pipeGameBoard.get(y)[x] = 'L';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case '|':
                this.pipeGameBoard.get(y)[x] = '-';
                stepsNumber.set(stepsNumber.get() + 1);
                break;

            case ' ':
                this.pipeGameBoard.get(y)[x] = ' ';
                break;
            case 's':
                this.pipeGameBoard.get(y)[x] = 's';
                break;
            case 'g':
                this.pipeGameBoard.get(y)[x] = 'g';
                break;
            default:
                this.pipeGameBoard.get(y)[x] = ' ';
                break;
        }
        this.pipeGameBoard.set(y, this.pipeGameBoard.get(y));
    }

    public void loadGame(String fileName) {
    }

    public void saveGame(File fileName) {
    }

    public void connectServer(String serverIP, String serverPort) {
    }

    public void solve() throws IOException, InterruptedException {
    }

    public void exit() {
        System.out.println("Exit");
        timer.cancel();

        this.disconnectServer();

        System.exit(0);
    }

    public void reset() {
        timePassed.set(0);
        stepsNumber.set(0);
        this.pipeGameBoard.setAll(getCleanBoard());
    }

    public void setCleanBoard(char[][] board) {
    }

    public char[][] getCleanBoard() {
        char[][] cloneBoard = new char[this.clean.length][this.clean[0].length];
        for (int k = 0; k < this.clean.length; k++) {
            System.arraycopy(this.clean[k], 0, cloneBoard[k], 0, this.clean[0].length);
        }
        return cloneBoard;
    }


    public char[][] clean() {
        char[][] cloneBoard = new char[this.clean.length][this.clean[0].length];
        for (int k = 0; k < this.clean.length; k++) {
            System.arraycopy(this.clean[k], 0, cloneBoard[k], 0, this.clean[0].length);
        }
        return cloneBoard;
    }

    public void disconnectServer() {
        if(this.serverSocket != null)
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
