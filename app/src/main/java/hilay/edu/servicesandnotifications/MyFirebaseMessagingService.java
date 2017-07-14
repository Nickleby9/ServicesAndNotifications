package hilay.edu.servicesandnotifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Receive firebase messages here
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "Ness";


    @Override
    public void handleIntent(Intent intent) {

        //get the payload from your notification
        String title = intent.getExtras().getString("title");
        String shortMessage = intent.getExtras().getString("short");
        //
        //super if the app is in the background:
        //send a push notification "DEFAULT" title and icon


        //if the app is in the foreground:
        //send the push to onMessageReceived



        Intent contentIntent = new Intent(this, MainActivity.class);
        //put some extras
        PendingIntent pi =
                PendingIntent.getActivity(this, 1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Deprecated in API LEVEL O
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = "Channel1";
            setupChannel(channelName);
            //android.app.Notification;
            Notification.Builder builder = new Notification.Builder(this, channelName);

 //title, text, smallIcon, ->99.9 contentIntent, setAutoCancel
            builder.setContentTitle(title).
                    setContentText(shortMessage).
                    setSmallIcon(R.drawable.ic_note).
                    setLargeIcon(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.ic_launcher_round
                    )).
                    setBadgeIconType(Notification.BADGE_ICON_LARGE).
                    setAutoCancel(true).
                    setContentIntent(pi);

            NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mgr.notify(1, builder.build());
        }else {
            Notification.Builder builder = new Notification.Builder(this);

            builder.setContentTitle(title).
                    setContentText(shortMessage).
                    setSmallIcon(R.drawable.ic_note).
                    setPriority(Notification.PRIORITY_HIGH). /*Push the notification from the top*/
                    setDefaults(Notification.DEFAULT_ALL).
                    setAutoCancel(true).
                    setContentIntent(pi);

            NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mgr.notify(1, builder.build());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupChannel(String id){
        String channelName = getResources().getString(R.string.channel1_name);
        NotificationChannel channel = new NotificationChannel(id,
                                                              channelName,
                                                              NotificationManager.IMPORTANCE_HIGH
        );

        channel.setDescription(getResources().getString(R.string.channel_description));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableVibration(true);
        channel.enableLights(true);

        channel.setSound(RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION),
                Notification.AUDIO_ATTRIBUTES_DEFAULT
        );

        channel.setShowBadge(true);

        //TODO: Custom sounds
        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.createNotificationChannel(channel);
    }
}
