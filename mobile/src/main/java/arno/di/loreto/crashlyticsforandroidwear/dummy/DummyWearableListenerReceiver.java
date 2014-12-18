package arno.di.loreto.crashlyticsforandroidwear.dummy;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;

import arno.di.loreto.crashlyticsforandroidwear.wearable.WearableListenerReceiver;

/**
 * A dummy receiver that will log the messages he received on path PATH_DUMMY = /dummy.
 * The purpose of this receiver is to show that when you create an Android Wear
 * application you need to send different type of message from the watch to the host device.
 */
public class DummyWearableListenerReceiver extends WearableListenerReceiver {

    private static final String MYLOGGER = DummyWearableListenerReceiver.class.getName();

    /**
     * The path handled by this receiver.
     */
    private static final String PATH_DUMMY = "/dummy";

    /**
     * A message is received, displaying it in the logs.
     * @param context
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(Context context, MessageEvent messageEvent) {
        if(PATH_DUMMY.equalsIgnoreCase(messageEvent.getPath())){
            String message = new String(messageEvent.getData());
            Log.d(MYLOGGER, "message received, path="+messageEvent.getPath() + ", message="+ message);
        }
        else {
            Log.d(MYLOGGER, "unknown path:"+ messageEvent.getPath());
            super.onMessageReceived(context, messageEvent);
        }
    }

}
