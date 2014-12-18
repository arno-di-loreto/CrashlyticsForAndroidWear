package arno.di.loreto.crashlyticsforandroidwear.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import arno.di.loreto.crashlyticsforandroidwear.R;
import io.fabric.sdk.android.Fabric;

/**
 * A simple activity with crash test and exception test buttons.
 * Nothing special here, the Android Studio Fabric plugin
 * will write the necessary code here and modify the AndroidManifest.xml
 * and build.gradle files for you.
 * The purpose of this activity is to show how to use Crashlytics on android host
 * so you can compare with MainWearActivity (wear module).
 * NB: Crash report are sent in real time, exception report are not.
 */
public class MainActivity extends Activity {

    private static final String MYLOGGER = MainActivity.class.getName();

    /**
     * This on the main activity of your host (phone/tablet) application
     * that you initialize Crashlytics.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main_activity);
    }

    /**
     * This method is called when clicking on the crashtest
     * button (see main_activity_layout.xml).
     * It will crash the application with a NullPointerException
     * and Crashlytics will send a report.
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
     * button (see main_activity_layout.xml).
     * It logs the exception to Crashlytics (not in real time).
     */
    public void exceptionTest(View view){
        try{
            throw new Exception("this is a test exception");
        }
        catch(Exception ex){
            Log.e(MYLOGGER, "exceptionTest", ex);
            //Sending an exception report to Crashlytics
            Crashlytics.logException(ex);
        }
    }
}
