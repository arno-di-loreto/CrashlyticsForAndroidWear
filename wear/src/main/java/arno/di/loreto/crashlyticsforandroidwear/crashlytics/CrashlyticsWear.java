package arno.di.loreto.crashlyticsforandroidwear.crashlytics;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Static methods to initialize the CrashlyticsWearUncaughtExceptionHandler and to log exception.
 */
public class CrashlyticsWear {

    private static final String MYLOGGER = CrashlyticsWear.class.getName();

    /**
     * The CrachlyticsUncaughtExceptionHandler singleton.
     */
    private static CrachlyticsWearUncaughtExceptionHandler crachlyticsUncaughtExceptionHandler;

    /**
     * The context (the application)
     */
    private static Application context;

    /**
     * Create the singleton (if needed).
     * Call this when you would have the .
     * @param application The application.
     */
    public static void init(Application application){
        if(crachlyticsUncaughtExceptionHandler == null) {
            Log.d(MYLOGGER, "Initialize CrachlyticsUncaughtExceptionHandler");
            crachlyticsUncaughtExceptionHandler = new CrachlyticsWearUncaughtExceptionHandler(application);
            context = application;
        }
        else{
            Log.d(MYLOGGER, "CrachlyticsUncaughtExceptionHandler already initialized");
        }
    }

    /**
     * Send an exception report.
     * @param ex The exception
     */
    public static void logException(Throwable ex){
        Intent intent = new Intent(context, CrashlyticsWearIntentService.class);
        intent.putExtra(CrashlyticsWearIntentService.EXTRA_DATA_ERROR, ex);
        intent.putExtra(CrashlyticsWearIntentService.EXTRA_DATA_REPORT_TYPE, CrashlyticsWearIntentService.REPORT_TYPE_EXCEPTION);
        context.startService(intent);
    }

}
