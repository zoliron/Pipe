package client_desktop.view.Messeges;

import java.util.List;

public interface NakedObject {

    public List<String> getFieldNames();
    public void fieldChanged(String fieldName, String newValue);
    public String getFieldValue(String fieldName);
}
