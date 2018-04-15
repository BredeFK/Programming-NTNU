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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserListFragment extends Fragment {
    private static final String TAG = "UserListFragment";
    private ListView userListView;
    private UserAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userlist_fragment, container, false);

        getValues(view);
        onRefreshListener();
        onClickListener();
        getUsers();

        return view;
    }

    private void getValues(View view) {
        adapter = new UserAdapter(getActivity(), R.layout.list_user);
        userListView = view.findViewById(R.id.userListID);
        refreshLayout = view.findViewById(R.id.swipeRefreshUser);
    }

    private void onRefreshListener() {
        final String newestUsers = this.getString(R.string.loadedNewestUsers);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                Toast.makeText(getActivity(), newestUsers, Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = database.getReference("users");

        refUsers.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String user = ds.getValue(String.class);

                    adapter.add(user);
                }
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value in userList.", error.toException());
            }
        });
    }

    // TODO : show all messages the user has sent when pressed on
    private void onClickListener() {
        final String showingMessages = this.getString(R.string.showingMessages);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String user = userListView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(), UserMessages.class);
                intent.putExtra("user", user);
                Toast.makeText(getActivity(), showingMessages + ' ' + user, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
