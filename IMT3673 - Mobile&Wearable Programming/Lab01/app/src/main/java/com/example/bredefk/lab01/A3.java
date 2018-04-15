// 06.03.18
// Brede Fritjof Klausen

package com.example.bredefk.lab01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class A3 extends AppCompatActivity {

    public static final String MESSAGE_A3 = "MessageFromA3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3);
    }

    public void sendMessage(View view) {

        Intent intent = new Intent(A3.this, A2.class);     // Create intent
        EditText editTextT4 = findViewById(R.id.T4);                    // Name the EditText, T4, to editTextT4
        String message = "From A3: " + editTextT4.getText().toString(); // Convert editTextT4 content to string
        intent.putExtra(MESSAGE_A3, message);                           // Add editTextT4 content to the intent
        startActivity(intent);                                          // Launches a new activity(A2)
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(A3.this, A2.class));      // Creates an intent and launches the Activity(A2)

        Intent intent = new Intent(A3.this, A2.class);     // I think this is how i complete the task under?:
        EditText editTextT4 = findViewById(R.id.T4);                    //
                                                                        // The field T3 shows the same value "From A3: "
        String message = "From A3: " + editTextT4.getText().toString(); // if T4 was edited but Back button was pressed
        intent.putExtra(MESSAGE_A3, message);                           //
        startActivity(intent);                                          //
    }
}
