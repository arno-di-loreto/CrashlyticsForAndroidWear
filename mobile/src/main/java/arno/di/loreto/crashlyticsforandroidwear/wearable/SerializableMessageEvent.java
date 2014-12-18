package arno.di.loreto.crashlyticsforandroidwear.wearable;

import com.google.android.gms.wearable.MessageEvent;

import java.io.Serializable;

/**
 * MessageEvent is not serializable, so here is a Serializable version.
 */
public class SerializableMessageEvent implements MessageEvent, Serializable {

    private byte[] data;
    private String path;
    private int requestId;
    private String sourceNodeId;

    public SerializableMessageEvent(MessageEvent messageEvent){
        data = messageEvent.getData();
        path = messageEvent.getPath();
        requestId = messageEvent.getRequestId();
        sourceNodeId = messageEvent.getSourceNodeId();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

}
