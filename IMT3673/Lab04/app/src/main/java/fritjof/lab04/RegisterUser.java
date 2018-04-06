// Author: Brede Fritjof Klausen

package fritjof.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class RegisterUser extends AppCompatActivity {
    private static final String TAG = "RegisterUser";
    private static final int MAX_NUMBER = 1000;
    private static final int MAX_LENGTH = 35;
    private boolean isAnon;
    private Database db;
    private FirebaseAuth mAuth;
    private Intent regIntent;
    private Button btn_save;
    private Button btn_saveAuth;
    private TextView txt_nickname;
    private TextView txt_title;
    private EditText edt_nickname;
    private EditText edt_email;
    private EditText edt_password;
    private String nickName;
    private String authFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        getValues();

        if (isAnon) {
            // Propose nickname and set view
            proposeNickname();
        } else {
            // Set view for creating a user
            setViewForAuth();
        }
    }

    @Override
    public void onBackPressed() {

        // If anon: user has to create nickname
        if (isAnon) {
            moveTaskToBack(true);
        } else {
            regIntent = new Intent(RegisterUser.this, MainActivity.class);
            startActivity(regIntent);
        }
    }

    private void getValues() {
        // Get instance
        mAuth = FirebaseAuth.getInstance();

        // Get access to other classes functions
        db = new Database();

        // Title
        txt_title = findViewById(R.id.titleID);

        // Anon user: Create Nickname
        btn_save = findViewById(R.id.saveID);
        txt_nickname = findViewById(R.id.textNicknameID);
        edt_nickname = findViewById(R.id.nicknameID);

        // Anon to Auth user: Create Auth
        btn_saveAuth = findViewById(R.id.saveAuthID);
        edt_email = findViewById(R.id.emailID);
        edt_password = findViewById(R.id.passwordID);

        // Get boolean from MainActivity.java
        regIntent = getIntent();
        isAnon = regIntent.getBooleanExtra("anon", false);
    }

    private void proposeNickname() {
        String user = this.getString(R.string.user);
        // Set correct title
        txt_title.setText(R.string.createNickname);
        authFailed = this.getString(R.string.authenticationFailed);

        // Remove auth components
        btn_saveAuth.setVisibility(View.GONE);
        edt_email.setVisibility(View.GONE);
        edt_password.setVisibility(View.GONE);

        // Get nickname components
        btn_save.setVisibility(View.VISIBLE);
        txt_nickname.setVisibility(View.VISIBLE);
        edt_nickname.setVisibility(View.VISIBLE);

        // Generate a random number
        Random random = new Random();
        int randValue = random.nextInt(MAX_NUMBER) + 1;
        nickName = user + randValue;

        // Propose the nickname to the user
        edt_nickname.setText(nickName);
    }

    public void setViewForAuth() {
        // Set correct title
        txt_title.setText(R.string.createUser);

        // Remove nickname components
        btn_save.setVisibility(View.GONE);
        txt_nickname.setVisibility(View.GONE);
        edt_nickname.setVisibility(View.GONE);

        // get auth components
        btn_saveAuth.setVisibility(View.VISIBLE);
        edt_email.setVisibility(View.VISIBLE);
        edt_password.setVisibility(View.VISIBLE);
    }

    public void linkAnonWithAuth(View view) {
        // Get resources
        String emptyEmail, emptyPassword;
        emptyEmail = this.getString(R.string.emptyEmail);
        emptyPassword = this.getString(R.string.emptyPassword);

        // Get email and password
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        // Give error if they are empty
        if (email.isEmpty()) {
            Toast.makeText(RegisterUser.this, emptyEmail, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterUser.this, emptyPassword, Toast.LENGTH_SHORT).show();
        } else {
            // If both are not empty: Create user
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            convertAnonToAuthUser(credential);
        }
    }

    public void saveAnonNickName(View view) {
        // Get resources
        String emptyNickname, maxNickname, characters;
        emptyNickname = this.getString(R.string.emptyNickname);
        maxNickname = this.getString(R.string.maxNickname);
        characters = this.getString(R.string.characters);

        // Get nickname
        nickName = edt_nickname.getText().toString();

        // Give error if nickname is empty
        if (nickName.isEmpty()) {
            Toast.makeText(RegisterUser.this, emptyNickname, Toast.LENGTH_SHORT).show();
        } else {
            // If not empty: Check length
            if (nickName.length() > MAX_LENGTH) {
                Toast.makeText(RegisterUser.this, maxNickname + ' ' + MAX_LENGTH + ' ' + characters,
                        Toast.LENGTH_LONG).show();
            } else {
                // If not empty and correct length: Create user and store nickname
                createAnonUser();
            }
        }
    }

    private void createAnonUser() {
        // Get resources
        final String hello = this.getString(R.string.hello);

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            regIntent = new Intent(RegisterUser.this, MainActivity.class);
                            regIntent.putExtra("nickname", nickName);
                            db.addUserToDB(nickName);
                            Toast.makeText(RegisterUser.this, hello + ' ' + nickName + "!", Toast.LENGTH_SHORT).show();
                            startActivity(regIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(RegisterUser.this, authFailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void convertAnonToAuthUser(AuthCredential authCredential) {
        mAuth.getCurrentUser().linkWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Success: Send user back to MainActivity
                            Log.d(TAG, "linkWithCredential:success");
                            regIntent = new Intent(RegisterUser.this, MainActivity.class);
                            startActivity(regIntent);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(RegisterUser.this, authFailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}