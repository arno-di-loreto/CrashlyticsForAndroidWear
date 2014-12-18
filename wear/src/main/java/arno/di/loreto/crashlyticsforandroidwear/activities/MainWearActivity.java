package arno.di.loreto.crashlyticsforandroidwear.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import arno.di.loreto.crashlyticsforandroidwear.R;
import arno.di.loreto.crashlyticsforandroidwear.crashlytics.CrashlyticsWear;
import arno.di.loreto.crashlyticsforandroidwear.services.SendDummyMessageIntentService;

/**
 * This wear activity's purpose is to show how a Wear application can handle
 * sending crashlytics reports (crash or exception) AND sending other message
 * to the host device.
 */
public class MainWearActivity extends Activity {

    private static final String MYLOGGER = MainWearActivity.class.getName();

    /**
     * Here we initialize Crashlytics for android wear.
     * If the application crashes, report will be sent to the host device.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashlyticsWear.init(this.getApplication());
        setContentView(R.layout.main_wear_activity);
    }

    /**
     * This method is called when clicking on the crashtest
     * button (see main_wear_activity_layout.xml).
     * It will crash with a NullPointerException and be
     * catch by the CrashlyticsUncaughtExceptionHandler
     */
    public void crashTest(View view){
        String crash = null;
        Log.d(MYLOGGER, "Preparing to crash...");
        if(crash.length() > 0){
            Log.d(MYLOGGER, "I should not be here");
        }
    }

    /**
     * This method is called when clicking on the exceptiontest
     * button (see main_wear_activity_layout.xml).
     * It sends an exception report to Crashlytics.
     */
    public void exceptionTest(View view){
        try{
            throw new Exception("this is a test exception");
        }
        catch(Exception ex){
            Log.e(MYLOGGER, "exceptionTest", ex);
            //Sending an exception report to crashlytics
            CrashlyticsWear.logException(ex);
        }
    }

    /**
     * This method is called when clicking on the messageTest
     * button (see main_wear_activity_layout.xml).
     * It send a dummy message to the host device.
     */
    public void messageTest(View v){
        Log.d(MYLOGGER, "Preparing to send a dummy message...");
        Intent intent = new Intent(this, SendDummyMessageIntentService.class);
        intent.putExtra(SendDummyMessageIntentService.EXTRA_DATA_MESSAGE, "A dummy message");
        this.startService(intent);
    }

}
