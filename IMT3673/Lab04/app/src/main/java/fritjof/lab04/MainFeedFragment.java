// Author: Brede Fritjof Klausen

package fritjof.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainFeedFragment extends Fragment {
    private static final String TAG = "MainFeedFragment";
    private static final int MAX_MESSAGE_LENGTH = 256;
    private String nickname;
    private Database db;
    private Button sendMessage;
    private EditText message;
    private ListView messageListView;
    private String textMessage;
    private FirebaseAuth mAuth;
    private Intent intent;
    private MessageAdapter adapter;
    private DatabaseReference refMessages;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfeed_fragment, container, false);

        getValues(view);
        onRefreshListener();
        onClickListener();
        getMessages();

        return view;
    }

    private void getValues(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refMessages = database.getReference("messages");
        nickname = getActivity().getPreferences(0).getString("nickname", "");

        refreshLayout = view.findViewById(R.id.swipeRefreshMessage);
        sendMessage = view.findViewById(R.id.sendID);
        message = view.findViewById(R.id.messageID);
        messageListView = view.findViewById(R.id.messageListID);

        db = new Database();
        mAuth = FirebaseAuth.getInstance();
    }

    private void onRefreshListener() {
        final String newestMessages = this.getString(R.string.loadedNewestMessages);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages();
                Toast.makeText(getActivity(), newestMessages, Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void onClickListener() {
        final String maxMessage, orEmpty, error;
        maxMessage = this.getString(R.string.maxMessage);
        orEmpty = this.getString(R.string.orEmpty);
        error = maxMessage + ' ' + MAX_MESSAGE_LENGTH + ' ' + orEmpty;
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If user is logged in
                if (mAuth.getCurrentUser() != null) {

                    // If user is anonymous: send to RegisterUser
                    if (mAuth.getCurrentUser().isAnonymous()) {
                        intent = new Intent(getActivity(), RegisterUser.class);
                        intent.putExtra("anon", false);
                        startActivity(intent);
                    } else {
                        textMessage = message.getText().toString();
                        if (textMessage.length() > MAX_MESSAGE_LENGTH || textMessage.isEmpty()) {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                        } else {
                            // Add to db
                            db.addMessageToDB(textMessage, nickname);

                            // Clear the text
                            message.setText("");
                        }
                    }
                }
            }
        });
    }

    private void getMessages() {
        // Read from the database
        refMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter = new MessageAdapter(getActivity(), R.layout.message_received, true);
                adapter.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    adapter.add(message);
                }
                messageListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value in messageList.", error.toException());
            }
        });
    }
}
