CrashlyticsForAndroidWear
=========================

An example of Crashlytics implementation in an Android Wear Project.
The purpose of the demo app is to show how you can implement Crahslytics on the wear device.
This implementations try to avoid mixing Crashlytics report handling and other message.

#Context
An Android Wear device could not access internet directly so every library which need internet access could not function on such a device.
It's the case of Crashlytics (and many other libs of course).
What is show here may be reused for other libraries with the same needs.

#Basic solution
The base mecanism is the one everybody confronting this problem have found: 
- Implementing an UncaughtExceptionHandler and setting it for your wear application.
- This UncaughtExceptionHandler then trigger a service that send a message through Google API to the host device
- On the host device a WearableListenerService catch the message then log a report with Crashlytics.logException

#"Advanced" solution
This implementation try to avoid mixing Crashlytics report message handling and other message the wear application might send and have something "modular".

The problem is you can have only ONE WearableListenerService in an application.
I do not want to mix crashlyics error handling and the "real" code of my application, Google documentation (https://developer.android.com/reference/com/google/android/gms/wearable/WearableListenerService.html) explained you should use a broadcaster to dispatch message to other components.

I succeed implementing:
- A WearableListenerService which broadcast all wear events
- A BroadcastReceiver that mimic a WearableListenerService that you need to subclass and declare in our manifest. It will receive all wear events dispatched by the wearablelistenerservice

#Drawbacks
The drawbacks of this implementation are:
- the use of a global broacast instead of a local broadcast
- all crashlytics wear report are considered as non fatal due to the use of Crashlytics.logException
- the need to have a broadcasted and receivers => If Android guys could allow to have more than one WearableListenerService it would be more easyer to do this.
