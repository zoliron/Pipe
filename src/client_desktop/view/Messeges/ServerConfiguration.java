package client_desktop.view.Messeges;

import java.util.ArrayList;
import java.util.List;

/**
 * A rather naive implementation of the naked object interface. A "better" implementation would hae the NakedObjectDisplayer
 * use reflection to get the class's fields and display them, thus making all the boilerplate here redundant.
 * That does entail some voodoo though, and voodoo for something as simple as this may not be necessary.
 * @author giladber
 *
 */
public class ServerConfiguration implements NakedObject {
    // Field names here
    private final String serverIpText = "Server IP";
    private final String serverPortText = "Server Port";
    // Default values here
    private String serverIp;
    private String serverPort;

    private final List<String> fieldNames;

    public ServerConfiguration() {
        this.fieldNames = new ArrayList<>();
        this.fieldNames.add(serverIpText);
        this.fieldNames.add(serverPortText);
    }

    @Override
    public List<String> getFieldNames() {
        return this.fieldNames;
    }

    @Override
    public void fieldChanged(String fieldName, String newValue) {
        switch (fieldName) {
            case serverIpText:
                this.serverIp = newValue;
                break;
            case serverPortText:
                this.serverPort = newValue;
                break;
            default:
                throw new IllegalArgumentException("unexpected value " + fieldName);
        }
    }

    @Override
    public String getFieldValue(String fieldName) {
        switch (fieldName) {
            case serverIpText:
                return this.serverIp;
            case serverPortText:
                return this.serverPort;
            default:
                throw new IllegalArgumentException("unexpected value " + fieldName);
        }
    }

}
