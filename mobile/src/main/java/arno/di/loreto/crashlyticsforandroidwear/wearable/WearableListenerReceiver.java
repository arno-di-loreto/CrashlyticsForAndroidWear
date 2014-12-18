package arno.di.loreto.crashlyticsforandroidwear.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * A "Wear event" receiver.
 * Subclass it to create your own receiver, don't forget to declare it in the manifest like this:
 * <receiver android:name="your.packages.YourWearableListenerReceiver" >
 *  <intent-filter>
 *      <action android:name="arno.di.loreto.wearmessage" />
 *  </intent-filter>
 * </receiver>
 *
 * It will received event with onReceive and dispatchh this event on the right method.
 * As you can see, this class maps WearableListenerService methods.
 */
public class WearableListenerReceiver extends BroadcastReceiver {

    private static final String MYLOGGER = WearableListenerReceiver.class.getName();

    public void onCreate(Context context){}

    public void onPeerDisconnected(Context context, Node peer){}

    public void onPeerConnected(Context context, Node peer) {}

    public void onMessageReceived(Context context, MessageEvent messageEvent){}

    public void onDataChanged(Context context, DataEventBuffer dataEvents) {}

    /**
     * Do not override this: it receives the broadcasted wear event and dispatch it to the right method.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MYLOGGER, "onReceive");
        String eventType = intent.getStringExtra(WearableListenerBroadcaster.EXTRA_DATA_EVENT_TYPE);
        byte[] bytes = intent.getByteArrayExtra(WearableListenerBroadcaster.EXTRA_DATA_EVENT);
        if(eventType != null && eventType.equalsIgnoreCase(WearableListenerBroadcaster.EVENT_TYPE_ON_MESSAGE_RECEIVED)){
            MessageEvent messageEvent = getObjectFromByteArray(bytes, MessageEvent.class);
            onMessageReceived(context, messageEvent);
        }
        else if(eventType != null && eventType.equalsIgnoreCase(WearableListenerBroadcaster.EVENT_TYPE_ON_DATA_CHANGED)){
            DataEventBuffer dataEvents = getObjectFromByteArray(bytes, DataEventBuffer.class);
            onDataChanged(context, dataEvents);
        }
        else if(eventType != null && eventType.equalsIgnoreCase(WearableListenerBroadcaster.EVENT_TYPE_ON_PEER_CONNECTED)){
            Node peer = getObjectFromByteArray(bytes, Node.class);
            onPeerConnected(context, peer);
        }
        else if(eventType != null && eventType.equalsIgnoreCase(WearableListenerBroadcaster.EVENT_TYPE_ON_PEER_DISCONNECTED)){
            Node peer = getObjectFromByteArray(bytes, Node.class);
            onPeerDisconnected(context, peer);
        }
        else if(eventType != null && eventType.equalsIgnoreCase(WearableListenerBroadcaster.EVENT_TYPE_ON_CREATE)){
            onCreate(context);
        }
        else{
            Log.e(MYLOGGER, "Unexpected eventType:"+eventType);
        }

    }

    /**
     * Gets the real object from the byte array.
     * @param byteArray
     * @param T
     * @param <T>
     * @return
     */
    private<T> T getObjectFromByteArray(byte[] byteArray, Class T){
        T result = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            result = (T)ois.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
