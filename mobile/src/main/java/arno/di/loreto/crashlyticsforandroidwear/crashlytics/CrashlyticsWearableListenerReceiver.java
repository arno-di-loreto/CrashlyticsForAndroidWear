package arno.di.loreto.crashlyticsforandroidwear.crashlytics;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

import arno.di.loreto.crashlyticsforandroidwear.wearable.WearableListenerReceiver;

/**
 * Receives message from the WearableListenerBroadcaster.
 * It will handle Crashlytics report (path PATH_CRASHLYTICS = /crashlytics).
 * Crash and Exception report are send using logException (I did not yet found a way
 * to send a real crash report)
 */
public class CrashlyticsWearableListenerReceiver extends WearableListenerReceiver {

    private static final String MYLOGGER = CrashlyticsWearableListenerReceiver.class.getName();

    /**
     * The path
     */
    public static final String PATH_CRASHLYTICS = "/crashlytics";
    public static final String DATA_MAP_ERROR = "ERROR";
    public static final String DATA_MAP_REPORT_TYPE = "REPORT_TYPE";
    public static final String CRASH_DATA_WEAR = "WEAR_REPORT";

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        Log.d(MYLOGGER, "onCreate");
    }

    @Override
    public void onMessageReceived(Context context, MessageEvent messageEvent) {
        if(PATH_CRASHLYTICS.equalsIgnoreCase(messageEvent.getPath())){
            sendCrashlyticsReport(DataMap.fromByteArray(messageEvent.getData()));
        }
        else {
            Log.d(MYLOGGER, "unknown path:"+ messageEvent.getPath());
            super.onMessageReceived(context, messageEvent);
        }
    }

    /**
     * Sending the report. For now a crash or an exception are logged as exception.
     * Does someone know how to report a crash?
     * @param dataMap The DataMap send by the wear device.
     */
    private void sendCrashlyticsReport(DataMap dataMap){
        Log.d(MYLOGGER, "Trying to send crashlytics report");
        String reportType = dataMap.getString(DATA_MAP_REPORT_TYPE);
        ByteArrayInputStream bis = new ByteArrayInputStream(dataMap.getByteArray(DATA_MAP_ERROR));
        try{
            ObjectInputStream ois = new ObjectInputStream(bis);
            Throwable data_map_error = (Throwable)ois.readObject();
            Log.d(MYLOGGER, "Crash report received from wear device: type=" + reportType, data_map_error);
            Crashlytics.setBool(CRASH_DATA_WEAR, Boolean.TRUE);
            for(Iterator<String> i = dataMap.keySet().iterator(); i.hasNext();){
                String key = i.next();
                if(!key.equalsIgnoreCase(DATA_MAP_ERROR)){
                    Log.d(MYLOGGER,"data_map."+key+"="+dataMap.getString(key));
                    Crashlytics.setString(key, dataMap.getString(key));
                }
            }
            //Is there a way to send a real crash report instead of log exception?
            Crashlytics.logException(data_map_error);
            Log.d(MYLOGGER, "Crashlytics report sent");
        }
        catch(IOException _ex){
            Log.e(MYLOGGER, "error deserialazing DATA_MAP_ERROR");
            try {
                bis.close();
            }
            catch(IOException _ioex){}
        }
        catch (ClassNotFoundException e) {
            Log.e(MYLOGGER, "error deserialazing DATA_MAP_ERROR");
            try {
                bis.close();
            }
            catch(IOException _ioex){}
        }
    }
}
