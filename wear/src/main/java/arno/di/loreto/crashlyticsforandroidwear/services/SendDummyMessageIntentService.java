package arno.di.loreto.crashlyticsforandroidwear.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * A dummy message sender to illustrate the need of handling different type of message
 * between the watch and the host device.
 */
public class SendDummyMessageIntentService extends IntentService {

    private static final String MYLOGGER = SendDummyMessageIntentService.class.getName();
    /**
     * The Intent's name.
     */
    public static final String INTENT_NAME = "SendDummyMessageIntentService";
    /**
     * Path used to send the message.
     */
    public static final String PATH_DUMMY = "/dummy";
    /**
     *
     */
    public static final String EXTRA_DATA_MESSAGE = "MESSAGE";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendDummyMessageIntentService() {
        super(INTENT_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String message = intent.getStringExtra(EXTRA_DATA_MESSAGE);
        sendMessage(PATH_DUMMY, message);
    }

    /**
     * Connecting to Google API and sending a message to all connected devices.
     * @param path The path
     * @param message The message to send
     */
    private void sendMessage(String path, String message) {
        GoogleApiClient mApiClient = new GoogleApiClient.Builder(this)
                .addApi( Wearable.API )
                .build();
        Log.d(MYLOGGER, "Connecting to Google API");
        mApiClient.blockingConnect();
        Log.d(MYLOGGER, "Connected to Google API");

        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
        Log.d(MYLOGGER, "Connected nodes size "+nodes.getNodes().size());
        for(Node node : nodes.getNodes()) {
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mApiClient, node.getId(), path, message.getBytes() ).await();
            if(result.getStatus().isSuccess()) {
                Log.d(MYLOGGER, "Message sent on node:"+node.getDisplayName());
            }
            else{
                Log.e(MYLOGGER, "Sending message failed: " + result.getStatus().getStatusMessage() + ", Node:" + node.getDisplayName());
            }
        }
    }
}
