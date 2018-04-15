// 06.03.18
// Brede Fritjof Klausen

package com.example.bredefk.lab01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class A2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);

        Intent intentA1 = getIntent();                              // Get the intentA1
        String messageA1 = intentA1.getStringExtra(A1.MESSAGE_A1);  // Extract the string
        if (Objects.equals(messageA1,"Hello ")){                 // If T1 was empty:
            messageA1 = "";                                         // Empty string
        }                                                           //
        TextView textViewT2 = findViewById(R.id.T2);                // Name the TextView, T2, to textViewT2
        textViewT2.setText(messageA1);                              // Put messageA1 in textViewT2
                                                                    //
        ///////////////////// DO IT AGAIN FOR A3 /////////////////////
                                                                    //
        Intent intentA3 = getIntent();                              // Get the intentA3 from A3
        String messageA3 = intentA3.getStringExtra(A3.MESSAGE_A3);  // Extract the string
        if (Objects.equals(messageA3,"From A3: ")){              // If T4 was empty:
            messageA3 = "";                                         // Empty string
        }                                                           //
        TextView textViewT3 = findViewById(R.id.T3);                // Name the TextView, T3, to textViewT3
        textViewT3.setText(messageA3);                              // Put messageA3 in TextViewT3
    }

    public void nextActivity(View view) {
        startActivity(new Intent(A2.this, A3.class)); // Creates an intent and launches a new activity(A3)
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(A2.this, A1.class)); // Creates an intent and launches a new activity(A1)
    }
}
