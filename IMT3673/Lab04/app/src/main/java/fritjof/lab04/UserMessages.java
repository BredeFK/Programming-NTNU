package fritjof.lab04;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMessages extends AppCompatActivity {

    private static final String TAG = "UserMessages";
    private static String user;
    private DatabaseReference refMessages;
    private ListView userListView;
    private MessageAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private TextView userHeading;
    private MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        getValues();
        getMessages();
        onRefreshListener();
    }

    private void getValues() {
        user = getIntent().getStringExtra("user");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refMessages = database.getReference("messages");
        userListView = findViewById(R.id.userMessages_ID);
        refreshLayout = findViewById(R.id.refreshUserMessages_ID);
        userHeading = findViewById(R.id.userHeading_ID);
        main = new MainActivity();
    }

    private void onRefreshListener() {
        final String newestMessages = this.getString(R.string.loadedNewestMessages);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages();
                Toast.makeText(UserMessages.this, newestMessages, Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    // TODO : make query that gets the messages?
    private void getMessages() {
        // Read from the database
        refMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter = new MessageAdapter(UserMessages.this, R.layout.message_received, false);
                adapter.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    if (message != null) {
                        if (message.user.equals(user)) {
                            adapter.add(message);
                        }
                    }

                }

                if (main.nickname.equals(user)) {
                    userHeading.setBackgroundColor(getResources().getColor(R.color.colorMessage));
                } else {
                    userHeading.setBackgroundColor(getResources().getColor(R.color.color_MaterialGrey500));
                }
                String message = user + " | " + adapter.getCount();
                userHeading.setText(message);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value in messageList.", error.toException());
            }
        });
    }
}
