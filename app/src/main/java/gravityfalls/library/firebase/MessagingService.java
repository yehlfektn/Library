package gravityfalls.library.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import gravityfalls.library.R;
import gravityfalls.library.main.BookDetailsActivity;
import gravityfalls.library.objects.Book;

/**
 * Created by 777 on 14.04.2018.
 */

public class MessagingService extends FirebaseMessagingService {

    String ADMIN_CHANNEL_ID = "13";

    NotificationManager notificationManager;
    private DatabaseReference mDatabase;
    private RemoteMessage remoteMessage;
    private String id;

    String TAG = "FMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            ValueEventListener booksListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Book book = dataSnapshot.getValue(Book.class);
                    if (book != null) {
                        Log.e(TAG,"Book title: "+book.getTitle());
                        buildNotification(book);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error: " + databaseError.getMessage());
                }
            };

            String category  = remoteMessage.getData().get("category");
            id  = remoteMessage.getData().get("id");
            Log.e(TAG,"Message, category: "+category+" id: "+id);

            mDatabase.child(category).child(id).addValueEventListener(booksListener);
        }
    }
    private void buildNotification(Book book) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        int notificationId = new Random().nextInt(60000);
        int requestID = (int) System.currentTimeMillis();



        // Create an Intent for the activity you want to start
        Intent resultIntent = BookDetailsActivity.getIntent(this,book,id);
     // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);




        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background) //a resource for your custom small icon
                .setAutoCancel(true) //dismisses the notification on click
                .setSound(defaultSoundUri);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle()) //the "title" value you sent in your notification
                    .setContentText(remoteMessage.getNotification().getBody()); //ditto
            Log.wtf(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationBuilder.setContentIntent(resultPendingIntent);
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);

        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
