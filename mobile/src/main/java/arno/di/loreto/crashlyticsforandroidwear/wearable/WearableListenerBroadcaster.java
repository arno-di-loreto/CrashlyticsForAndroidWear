package arno.di.loreto.crashlyticsforandroidwear.wearable;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import io.fabric.sdk.android.Fabric;

/**
 * The (only) WearableListenerService which will received message from the watch.
 * He will broadcast all events to receivers (WearableListenerReceiver subclass)
 * declared in AndroidManifest with action intent filter = ACTION_NAME ("arno.di.loreto.wearmessage").
 * I tried to use localbroadcasting but did not manage to succeed, so for the moment I use standard broadcast.
 * The events can be
 * - onCreate
 * - onPeerDisconnected
 * - onPeerConnected
 * - onMessageReceived
 * - onDataChanged (TODO)
 * You can manage different type of message within a WearableListenerService but what about clean
 * third party library integration which need to send message or use dataLayer too?
 * Personnally I don't want to mix things that don't need to be mixed.
 * So this is why I do it like that, it would have been much simpler if we could register
 * more that one WearableListenerService
 */
public class WearableListenerBroadcaster extends WearableListenerService {

    private static final String MYLOGGER = WearableListenerBroadcaster.class.getName();

    public static final String ACTION_NAME = "arno.di.loreto.wearmessage";

    public static final String EXTRA_DATA_EVENT = "EVENT";
    public static final String EXTRA_DATA_EVENT_TYPE = "EVENT_TYPE";
    public static final String EXTRA_DATA_PATH = "EVENT_PATH";

    public static final String EVENT_TYPE_ON_MESSAGE_RECEIVED = "ON_MESSAGE_RECEIVED";
    public static final String EVENT_TYPE_ON_DATA_CHANGED = "ON_DATA_CHANGED";
    public static final String EVENT_TYPE_ON_PEER_DISCONNECTED = "ON_PEER_DISCONNECTED";
    public static final String EVENT_TYPE_ON_PEER_CONNECTED = "ON_PEER_CONNECTED";
    public static final String EVENT_TYPE_ON_CREATE = "ON_CREATE";


    /**
     * Broadcasts the WearableListenerService.onPeerDisconnected event.
     * @param peer
     */
    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(MYLOGGER, "onPeerDisconnected "+ peer.getDisplayName());
        Intent intent = new Intent(ACTION_NAME);
        intent.setAction(ACTION_NAME);
        intent.putExtra(EXTRA_DATA_EVENT, objectToByArray(new SerializableNode(peer)));
        intent.putExtra(EXTRA_DATA_EVENT_TYPE, EVENT_TYPE_ON_PEER_DISCONNECTED);
        this.sendBroadcast(intent);
        super.onPeerDisconnected(peer);
    }

    /**
     * Broadcasts the WearableListenerService.onPeerConnected event.
     * @param peer
     */
    @Override
    public void onPeerConnected(Node peer) {
        Log.d(MYLOGGER, "onPeerConnected "+ peer.getDisplayName());
        Intent intent = new Intent(ACTION_NAME);
        intent.putExtra(EXTRA_DATA_EVENT, objectToByArray(new SerializableNode(peer)));
        intent.putExtra(EXTRA_DATA_EVENT_TYPE, EVENT_TYPE_ON_PEER_CONNECTED);
        this.sendBroadcast(intent);
        super.onPeerConnected(peer);
    }

    /**
     * Broadcasts the WearableListenerService.onMessageReceivedEvent
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(MYLOGGER, "onMessageReceived, path="+messageEvent.getPath());
        Intent intent = new Intent(ACTION_NAME);
        intent.putExtra(EXTRA_DATA_EVENT, objectToByArray(new SerializableMessageEvent(messageEvent)));
        intent.putExtra(EXTRA_DATA_EVENT_TYPE, EVENT_TYPE_ON_MESSAGE_RECEIVED);
        intent.putExtra(EXTRA_DATA_PATH, messageEvent.getPath());
        Log.d(MYLOGGER, "Broadcasting to " + ACTION_NAME);
        this.sendBroadcast(intent);
        super.onMessageReceived(messageEvent);
    }

    /**
     * Broadcasts the WearableListenerService.onDataChangedEvent TO DO
     * @param dataEvents
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(MYLOGGER, "onDataChanged"+ dataEvents.getStatus().getStatusMessage());
        /*
        Intent intent = new Intent(ACTION_NAME);
        intent.putExtra(EXTRA_DATA_EVENT, objectToByArray(dataEvents));
        intent.putExtra(EXTRA_DATA_EVENT_TYPE, EVENT_TYPE_ON_DATA_CHANGED);
        this.sendBroadcast(intent);
        */
        super.onDataChanged(dataEvents);
    }

    /**
     * Destroy is not broadcast. But it could using the same mecanism as for the other events.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Broadcast the WearableListenerService.onCreate event.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MYLOGGER, "onCreate");
        Intent intent = new Intent(ACTION_NAME);
        intent.putExtra(EXTRA_DATA_EVENT_TYPE, EVENT_TYPE_ON_CREATE);
        Log.d(MYLOGGER, "Broadcasting onCreate to " + ACTION_NAME);
        this.sendBroadcast(intent);
    }

    /**
     * Get byte array for an object.
     * It seems using Parcelable instead of Serialazable could me better (TODO)
     * @param object The object
     * @return the byte array
     */
    private static byte[] objectToByArray(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
        }
        catch (IOException _ex) {
            Log.e(MYLOGGER, "object to byte array failed", _ex);
        }
        finally {
            try {
                if (oos != null)
                    oos.close();
            }
            catch (IOException _ex) {}
            try {
                bos.close();
            }
            catch (IOException _ex) {}
        }
        return bos.toByteArray();
    }
}
