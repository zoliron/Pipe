package client_desktop.model;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PipeGameModel implements GameModel {

    public enum availableMoves {down, left, right, start, up}

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
    public TimerTask getNewTimerTask() {
        return new TimerTask() {
            public void run(){
                Platform.runLater(()-> timePassed.set(timePassed.get() + 1));
            }
        };
    }

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
                isGoalState.set(false);
                passedPipes.clear();
                isGoalStatePossible(startPosition.x, startPosition.y, availableMoves.start);
            }
        });

        this.isGoalState = new SimpleBooleanProperty();
        this.isGoalState.addListener((observableValue, s, t1) -> {
            if (true == isGoalState.get()) {
                this.timer.cancel();
                this.timer = null;
            } else {
                if (null == this.timer) {
                    this.timer = new Timer();
                    this.timer.scheduleAtFixedRate(getNewTimerTask(), 1000, 1000);
                }
            }
        });
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

    private boolean isGoalStatePossible(int column, int row, availableMoves from) {
        //check bounds
        if (column < 0 || column >= pipeGameBoard.get(0).length)
            return false;
        if (row < 0 || row >= pipeGameBoard.size())
            return false;
        // start
        if (from == availableMoves.start) {
            return (isGoalStatePossible(column + 1, row, availableMoves.left) ||
                    isGoalStatePossible(column - 1, row, availableMoves.right) ||
                    isGoalStatePossible(column, row + 1, availableMoves.up) ||
                    isGoalStatePossible(column, row - 1, availableMoves.down));
        }

        switch (pipeGameBoard.get(row)[column]) {
            case '-':
                if (from == availableMoves.left) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column + 1, row, availableMoves.left);
                } else if (from == availableMoves.right) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column - 1, row, availableMoves.right);
                } else {
                    return false;
                }
            case 'g':
                passedPipes.add(new Point(column, row));
                isGoalState.set(true);
                return true;
            case 'F':
                if (from == availableMoves.right) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row + 1, availableMoves.up);
                } else if (from == availableMoves.down) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column + 1, row, availableMoves.left);
                } else {
                    return false;
                }
            case '|':
                if (from == availableMoves.up) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row + 1, availableMoves.up);
                } else if (from == availableMoves.down) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row - 1, availableMoves.down);
                } else {
                    return false;
                }
            case 'L':
                if (from == availableMoves.up) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column + 1, row, availableMoves.left);
                } else if (from == availableMoves.right) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row - 1, availableMoves.down);
                } else {
                    return false;
                }

            case 'J':
                if (from == availableMoves.up) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column - 1, row, availableMoves.right);
                } else if (from == availableMoves.left) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row - 1, availableMoves.down);
                } else {
                    return false;
                }
            case '7':
                if (from == availableMoves.left) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column, row + 1, availableMoves.up);
                } else if (from == availableMoves.down) {
                    passedPipes.add(new Point(column, row));
                    return isGoalStatePossible(column - 1, row, availableMoves.right);
                } else {
                    return false;
                }
        }
        return false;
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
                {'s','-','7','-','L'},
                {'F','J','|','-','L'},
                {'F','F','|','-','L'},
                {'F','L','L','-','g'},
        };

        setCleanBoard(level);
        this.pipeGameBoard.addAll(level);
    }

    public void rotateCell(int column, int row) {
        switch (this.pipeGameBoard.get(row)[column]) {
            case '-':
                this.pipeGameBoard.get(row)[column] = '|';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case '7':
                this.pipeGameBoard.get(row)[column] = 'J';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'F':
                this.pipeGameBoard.get(row)[column] = '7';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'L':
                this.pipeGameBoard.get(row)[column] = 'F';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case 'J':
                this.pipeGameBoard.get(row)[column] = 'L';
                stepsNumber.set(stepsNumber.get() + 1);
                break;
            case '|':
                this.pipeGameBoard.get(row)[column] = '-';
                stepsNumber.set(stepsNumber.get() + 1);
                break;

            case ' ':
                this.pipeGameBoard.get(row)[column] = ' ';
                break;
            case 's':
                this.pipeGameBoard.get(row)[column] = 's';
                break;
            case 'g':
                this.pipeGameBoard.get(row)[column] = 'g';
                break;
            default:
                this.pipeGameBoard.get(row)[column] = ' ';
                break;
        }
        this.pipeGameBoard.set(row, this.pipeGameBoard.get(row));
    }

    public void loadFile(String fileName) {
        List<char[]> mapBuilder = new ArrayList<char[]>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Time Passed:")) {
                    int time = Integer.parseInt(line.split(":")[1]);
                    timePassed.set(time);
                } else if (line.startsWith("Steps Passed:")) {
                    int step = Integer.parseInt(line.split(":")[1]);
                    stepsNumber.set(step);
                } else {
                    mapBuilder.add(line.toCharArray());
                }
            }
            setCleanBoard(mapBuilder.toArray(new char[mapBuilder.size()][]));
            this.pipeGameBoard.setAll(mapBuilder.toArray(new char[mapBuilder.size()][]));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(File fileName) {
        try {
            PrintWriter savedFile = new PrintWriter(fileName);
            for (int i = 0; i < this.pipeGameBoard.size(); i++) {
                savedFile.println(new String(this.pipeGameBoard.get(i)));
            }
            savedFile.println("Time Passed:" + timePassed.get());
            savedFile.println("Steps Number:" + stepsNumber.get());
            savedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectServer(String serverIP, String serverPort) throws IOException {
        this.serverSocket = new Socket(serverIP, Integer.parseInt(serverPort));
        System.out.println("Connected to server");
    }

    public void solve() throws IOException, InterruptedException {
        // verify the connection.
        if (this.serverSocket != null) {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(this.serverSocket.getOutputStream());

            for (char[] line : this.pipeGameBoard.get()) {
                // send board to the server.
                outToServer.println(line);
                outToServer.flush();
            }
            outToServer.println("done");
            outToServer.flush();

            String line;
            // receive the solution.
            while (!(line = inFromServer.readLine()).equals("done")) {
                // changePipe accordingly. (consider sleep)
                String[] moves = line.split(",");
                int y = Integer.parseInt(moves[0]);
                int x = Integer.parseInt(moves[1]);
                int move = Integer.parseInt(moves[2]);
                // start counting from 1, to skip 0 moves
                for (int i = 1; i <= move; i++) {
                    Platform.runLater(()-> rotateCell(x, y));
                    Thread.sleep(100);
                }
            }
        }
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
