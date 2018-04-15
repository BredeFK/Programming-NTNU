package fritjof.lab04;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Database {
    private static final String TAG = "Database";
    private DatabaseReference refUsers;
    private DatabaseReference refMessages;

    public Database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refUsers = database.getReference("users");
        refMessages = database.getReference("messages");
    }

    // Add user to database
    public void addUserToDB(String nickname) {
        String userID = refUsers.push().getKey();
        refUsers.child(userID).setValue(nickname);
    }

    // Add message to database
    public void addMessageToDB(String message, String user) {
        long date = Calendar.getInstance().getTimeInMillis();
        Message messageClass = new Message(user, message, date);

        String messageID = refMessages.push().getKey();
        refMessages.child(messageID).setValue(messageClass);
    }
}
