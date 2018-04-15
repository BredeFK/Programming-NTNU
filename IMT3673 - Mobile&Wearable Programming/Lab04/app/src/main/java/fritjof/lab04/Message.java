// Author: Brede Fritjof Klausen

package fritjof.lab04;

import com.google.firebase.database.IgnoreExtraProperties;

// Inspired by: https://firebase.google.com/docs/database/android/read-and-write?authuser=0
@IgnoreExtraProperties
public class Message {
    public String user;
    public String message;
    public long date;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String user, String message, long date) {
        this.user = user;
        this.message = message;
        this.date = date;
    }
}
