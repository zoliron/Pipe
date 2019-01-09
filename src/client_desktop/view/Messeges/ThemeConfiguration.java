package client_desktop.view.Messeges;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThemeConfiguration implements NakedList {

    // Default values here
    private ObservableList<String> options;

    public ThemeConfiguration() {
        this.options = FXCollections.observableArrayList("plants", "plumber");
    }

    @Override
    public ObservableList<String> getOptions() {
        return this.options;
    }

    @Override
    public void setOptions(ObservableList<String> list) {
        this.options = list;
    }
}

