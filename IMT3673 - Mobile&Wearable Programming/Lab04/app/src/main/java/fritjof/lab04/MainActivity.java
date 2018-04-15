// Author: Brede Fritjof Klausen

package fritjof.lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY = "nickname";
    private static final int time = 1 * 60 * 1000; // 5min
    public static String nickname;
    private final Handler fetch = new Handler();
    private Intent mainIntent;
    private Intent serviceIntent;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private Runnable feed;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1_main);

        setup();

        if (isConnectedToNetwork()) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            userStatus(currentUser);
        } else {
            Toast.makeText(MainActivity.this, "Device not connected to internet!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(serviceIntent);
        feed.run();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "Under maintenance!", Toast.LENGTH_SHORT).show();
                /*
                mainIntent = new Intent(MainActivity.this, Settings.class);
                startActivity(mainIntent);
                */
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setup() {
        serviceIntent = new Intent(MainActivity.this, Notification.class);
        String main = this.getString(R.string.mainFeed);
        String users = this.getString(R.string.userList);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);

        pageAdapter.addFragment(new MainFeedFragment(), main);
        pageAdapter.addFragment(new UserListFragment(), users);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        preferences = getPreferences(0);
        toolbar = findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);

        feed = new Runnable() {
            @Override
            public void run() {
                fetch.postDelayed(this, time);
            }
        };
    }

    private void userStatus(FirebaseUser currentUser) {
        // User is logged in
        if (currentUser != null) {
            saveNickname();
            nickname = preferences.getString(KEY, "");

            System.out.println("userStatus[MainActivity] Nickname ====> " + nickname + " && " + currentUser.getUid() + " <==== ID");

            Bundle bundle = new Bundle();
            bundle.putString("nickname", nickname);
            MainFeedFragment mainFeedFragment = new MainFeedFragment();
            mainFeedFragment.setArguments(bundle);


            // If nickname hasn't been chosen
            if (nickname.isEmpty()) {
                System.out.println("userStatus[MainActivity]: User is logged in, but no nickname is chosen");
                mainIntent = new Intent(MainActivity.this, RegisterUser.class);
                mainIntent.putExtra("anon", true);
                startActivity(mainIntent);
            }
        } else {
            // User is not logged in
            System.out.println("userStatus[MainActivity]: User is not logged in");
            mainIntent = new Intent(MainActivity.this, RegisterUser.class);
            mainIntent.putExtra("anon", true);
            startActivity(mainIntent);
        }
    }

    private void saveNickname() {
        mainIntent = getIntent();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if user is logged in
        if (currentUser != null) {

            // Get nickname
            nickname = mainIntent.getStringExtra("nickname");

            // check if nickname isn't null and save it
            if (nickname != null) {
                SharedPreferences.Editor editor = getPreferences(0).edit();
                editor.putString(KEY, nickname);
                editor.apply();
            } else {
                System.out.println("saveNickname[MainActivity]: User is logged in, nickname id null");
            }
        } else {
            System.out.println("saveNickname[MainActivity]: User not logged in");
        }
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if device is connected to internet or connecting and activeNetworkInfo isn't null
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}