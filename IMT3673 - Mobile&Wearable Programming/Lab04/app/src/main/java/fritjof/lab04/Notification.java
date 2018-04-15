package fritjof.lab04;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification extends IntentService {

    private static final String KEY = "messagesCount";
    private static int oldCount = 0;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private SharedPreferences preferences;
    private Message newMessage;
    private MainActivity main;

    public Notification() {
        super("Notification");
    }


    @Override
    public void onHandleIntent(@Nullable Intent intent) {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        oldCount = preferences.getInt(KEY, 0);
        main = new MainActivity();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refMessages = database.getReference("messages");

        this.notificationManager = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);

        // Create the NotificationChannel only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    this.getString(R.string.app_name), "MessagesChannel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification");

            // Register channel with the system
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, channel.getId());
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        refMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentMessages = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    currentMessages++;
                    if (currentMessages > oldCount) {
                        newMessage = ds.getValue(Message.class);
                    }
                }

                // If new message
                if (currentMessages > oldCount && newMessage != null) {

                    // Send notification if it's not from current user
                    if (!newMessage.user.equals(main.nickname)) {
                        sendNotification();
                    }

                    // Save count of messages
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(KEY, currentMessages);
                    editor.apply();

                } else {
                    // If count is less: save new number
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(KEY, currentMessages);
                    editor.apply();
                }

                oldCount = currentMessages;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void sendNotification() {
        // Get resource
        String messageFrom = this.getString(R.string.newMessageFrom);
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

        // Set icon
        this.builder.setSmallIcon(R.drawable.ic_message_white_24dp);
        this.builder.setContentTitle(messageFrom + ' ' + newMessage.user);
        this.builder.setContentText(newMessage.message);
        this.builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.builder.setContentIntent(pendingIntent);
        this.builder.setAutoCancel(true);                           // Closes the notification when clicked on
        this.notificationManager.notify(0, builder.build());

    }
}
