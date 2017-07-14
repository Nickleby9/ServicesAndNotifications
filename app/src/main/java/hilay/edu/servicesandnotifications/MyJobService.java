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
import android.support.annotation.RequiresApi;

import com.firebase.jobdispatcher.JobParameters;

import java.util.Date;

/**
 * Represents a job to be dispatched while the device is idle.
 */

public class MyJobService extends com.firebase.jobdispatcher.JobService {
    private static final String TAG = "Ness";

    @Override
    public boolean onStartJob(final JobParameters job) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //fake some work:
                try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
                //noinspection NewApi
                showNotification();
                jobFinished(job, true);
            }
        });
        t.start();


        return true; //is there ongoing work?
    }

    //No UI - can show notifications only!
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {

        //Picasso.with(this).load("url...").into(this);
        Intent intent = new Intent(this ,MainActivity.class);
        intent.putExtra("time", new Date().toString());
        PendingIntent pi = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Log.d(TAG, "showNotification: ");
//        android.support.v7.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle("Hello, From Recurring Service");
//        builder.setContentText("This is the text");
//        builder.setSmallIcon(R.drawable.ic_note);
//        builder.setChannel("Channel1");
//        //action
//        builder.setAutoCancel(true);
//        builder.setContentIntent(pi);
//
//        Notification notification = builder.build();
//
//        NotificationManagerCompat.from(this).notify(1, notification);

        String chanelID = "channel2";
        setupChannel(chanelID);
        Notification.Builder builder = new Notification.Builder(this, chanelID);
        builder.setContentTitle("Hello, From Recurring Service");
        builder.setContentText("This is the text");
        /*
        Picasso.with(this).load("url...").into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        //BitmapFactory.decodeStream()
        */
        builder.setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.notification)));
        //pre O channels
      //  builder.setPriority(Notification.PRIORITY_HIGH);
      //  builder.setDefaults(Notification.DEFAULT_ALL);

        builder.setSmallIcon(R.drawable.ic_note);
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.notify(3, builder.build());
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

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // should this job be retired?
    }
}
