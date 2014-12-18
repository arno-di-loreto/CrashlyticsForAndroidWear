package arno.di.loreto.crashlyticsforandroidwear.wearable;

import com.google.android.gms.wearable.Node;

import java.io.Serializable;

/**
 * Node is not serializable, so here is a Serializable version.
 */
public class SerializableNode implements Serializable, Node {

    private String displayName;
    private String id;

    public SerializableNode(Node peer){
        displayName = peer.getDisplayName();
        id = peer.getId();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
