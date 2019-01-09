package client_desktop.model;

import javafx.application.Platform;
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
    public ListProperty<char[]> board;
    private Point startPosition = null;
    private Point goalPosition = null;
    private ListProperty<Point> points;

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            timePassed.set(timePassed.get() + 1);
        }
    };

    public PipeGameModel() {
        this.board = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.board.addListener((ObservableValue,s,t1) ->{
            char[][] cells = this.board.toArray(new char[this.board.size()][]);
            for(int i=0; i < cells.length; i++) {
                for(int j=0; j< cells[i].length; j++) {
                    if (cells[i][j] == 's')
                        startPosition = new Point(i, j);
                    if (cells[i][j] == 'g')
                        goalPosition = new Point(i, j);
                }
            }

            if (startPosition != null) {
                points.clear();
                isGoalState.set(isGoalStateCheck(startPosition, goalPosition));
            }
        });

        this.isGoalState = new SimpleBooleanProperty();
        this.points = new SimpleListProperty<>(FXCollections.observableArrayList(new LinkedHashSet<Point>()));
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
            switch (board.get(cell.y)[cell.x]) {
                case '|':
                    if ((board.get(cell.y -1)[cell.x] == '|') ||
                            (board.get(cell.y -1)[cell.x] == '7') ||
                            (board.get(cell.y -1)[cell.x] == 'F') ||
                            (board.get(cell.y -1)[cell.x] == 's') ||
                            (board.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'J':
                    if ((board.get(cell.y -1)[cell.x] == '|') ||
                            (board.get(cell.y -1)[cell.x] == '7') ||
                            (board.get(cell.y -1)[cell.x] == 'F') ||
                            (board.get(cell.y -1)[cell.x] == 's') ||
                            (board.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'L':
                    if ((board.get(cell.y -1)[cell.x] == '|') ||
                            (board.get(cell.y -1)[cell.x] == '7') ||
                            (board.get(cell.y -1)[cell.x] == 'F') ||
                            (board.get(cell.y -1)[cell.x] == 's') ||
                            (board.get(cell.y -1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((board.get(cell.y -1)[cell.x] == '|') ||
                            (board.get(cell.y -1)[cell.x] == '7') ||
                            (board.get(cell.y -1)[cell.x] == 'F'))
                        return true;
                    return false;
                case 'g':
                    if ((board.get(cell.y -1)[cell.x] == '|') ||
                            (board.get(cell.y -1)[cell.x] == '7') ||
                            (board.get(cell.y -1)[cell.x] == 'F'))
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
            switch (board.get(cell.y)[cell.x]) {
                case '|':
                    if ((board.get(cell.y +1)[cell.x] == '|') ||
                            (board.get(cell.y +1)[cell.x] == 'J') ||
                            (board.get(cell.y +1)[cell.x] == 'L') ||
                            (board.get(cell.y +1)[cell.x] == 's') ||
                            (board.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 'F':
                    if ((board.get(cell.y +1)[cell.x] == '|') ||
                            (board.get(cell.y +1)[cell.x] == 'J') ||
                            (board.get(cell.y +1)[cell.x] == 'L') ||
                            (board.get(cell.y +1)[cell.x] == 's') ||
                            (board.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case '7':
                    if ((board.get(cell.y +1)[cell.x] == '|') ||
                            (board.get(cell.y +1)[cell.x] == 'J') ||
                            (board.get(cell.y +1)[cell.x] == 'L') ||
                            (board.get(cell.y +1)[cell.x] == 's') ||
                            (board.get(cell.y +1)[cell.x] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((board.get(cell.y +1)[cell.x] == '|') ||
                            (board.get(cell.y +1)[cell.x] == 'J') ||
                            (board.get(cell.y +1)[cell.x] == 'L'))
                        return true;
                    return false;
                case 'g':
                    if ((board.get(cell.y +1)[cell.x] == '|') ||
                            (board.get(cell.y +1)[cell.x] == 'J') ||
                            (board.get(cell.y +1)[cell.x] == 'L'))
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
            switch (board.get(cell.y)[cell.x]) {
                case '-':
                    if ((board.get(cell.y)[cell.x -1] == '-') ||
                            (board.get(cell.y)[cell.x -1] == 'F') ||
                            (board.get(cell.y)[cell.x -1] == 'L') ||
                            (board.get(cell.y)[cell.x -1] == 's') ||
                            (board.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case '7':
                    if ((board.get(cell.y)[cell.x -1] == '-') ||
                            (board.get(cell.y)[cell.x -1] == 'F') ||
                            (board.get(cell.y)[cell.x -1] == 'L') ||
                            (board.get(cell.y)[cell.x -1] == 's') ||
                            (board.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case 'J':
                    if ((board.get(cell.y)[cell.x -1] == '-') ||
                            (board.get(cell.y)[cell.x -1] == 'F') ||
                            (board.get(cell.y)[cell.x -1] == 'L') ||
                            (board.get(cell.y)[cell.x -1] == 's') ||
                            (board.get(cell.y)[cell.x -1] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((board.get(cell.y)[cell.x -1] == '-') ||
                            (board.get(cell.y)[cell.x -1] == 'F') ||
                            (board.get(cell.y)[cell.x -1] == 'L'))
                        return true;
                    return false;
                case 'g':
                    if ((board.get(cell.y)[cell.x -1] == '-') ||
                            (board.get(cell.y)[cell.x -1] == 'F') ||
                            (board.get(cell.y)[cell.x -1] == 'L'))
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
            switch (board.get(cell.y)[cell.x]) {
                case '-':
                    if ((board.get(cell.y)[cell.x +1] == '-') ||
                            (board.get(cell.y)[cell.x +1] == 'J') ||
                            (board.get(cell.y)[cell.x +1] == '7') ||
                            (board.get(cell.y)[cell.x +1] == 's') ||
                            (board.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 'F':
                    if ((board.get(cell.y)[cell.x +1] == '-') ||
                            (board.get(cell.y)[cell.x +1] == 'J') ||
                            (board.get(cell.y)[cell.x +1] == '7') ||
                            (board.get(cell.y)[cell.x +1] == 's') ||
                            (board.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 'L':
                    if ((board.get(cell.y)[cell.x +1] == '-') ||
                            (board.get(cell.y)[cell.x +1] == 'J') ||
                            (board.get(cell.y)[cell.x +1] == '7') ||
                            (board.get(cell.y)[cell.x +1] == 's') ||
                            (board.get(cell.y)[cell.x +1] == 'g'))
                        return true;
                    return false;
                case 's':
                    if ((board.get(cell.y)[cell.x +1] == '-') ||
                            (board.get(cell.y)[cell.x +1] == 'J') ||
                            (board.get(cell.y)[cell.x +1] == '7'))
                        return true;
                    return false;
                case 'g':
                    if ((board.get(cell.y)[cell.x +1] == '-') ||
                            (board.get(cell.y)[cell.x +1] == 'J') ||
                            (board.get(cell.y)[cell.x +1] == '7'))
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
        if(current.y < this.board.size() - 1)
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
        if(current.x < this.board.get(0).length - 1)
            temp = new Point((int)current.getX() +1, (int)current.getY());

        return temp;
    }

    public void InitBoard() {
        char[][] level = {
                {'s','F',' ','-','L'},
                {' ','J','J','-','L'},
                {' ','F','|','-','L'},
                {' ','L','L','-','g'},
        };

        setCleanBoard(level);
        this.board.addAll(level);
    }

    public void rotateCell(int row, int col) {
        switch(this.board.get(row)[col]) {
            case 'L':
                this.board.get(row)[col] = 'F';
                break;
            case 'F':
                this.board.get(row)[col] = '7';
                break;
            case '7':
                this.board.get(row)[col] = 'J';
                break;
            case 'J':
                this.board.get(row)[col] = 'L';
                break;
            case '-':
                this.board.get(row)[col] = '|';
                break;
            case '|':
                this.board.get(row)[col] = '-';
                break;
            default:
                break;
        }

        if(this.board.get(row)[col] == 'F' || this.board.get(row)[col] == 'L' ||
                this.board.get(row)[col] == 'J' || this.board.get(row)[col] == '7' ||
                this.board.get(row)[col] == '-' || this.board.get(row)[col] == '|') {
            stepsNumber.set(stepsNumber.get() + 1);
        }
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
        this.board.setAll(getCleanBoard());
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
