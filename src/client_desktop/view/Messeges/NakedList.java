package client_desktop.view.Messeges;

import javafx.collections.ObservableList;

public interface NakedList {

    ObservableList<String> options = null;

    ObservableList<String> getOptions();

    void setOptions(ObservableList<String> list);
}
