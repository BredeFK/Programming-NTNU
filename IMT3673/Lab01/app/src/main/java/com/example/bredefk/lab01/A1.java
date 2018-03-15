// 06.03.18
// Brede Fritjof Klausen

package com.example.bredefk.lab01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class A1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String MESSAGE_A1 = "MessageFromA1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        String[] languages = {"Norwegian", "Swedish", "English", "Spanish", "Italian", "Dutch", "Danish", "Polish"};

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerL1 = findViewById(R.id.L1);
        spinnerL1.setAdapter(adapter);
        spinnerL1.setOnItemSelectedListener(this);

        SharedPreferences selectionL1 = getPreferences(0);
        spinnerL1.setSelection(selectionL1.getInt("selectionL1", 0));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        Spinner spinnerL1 = findViewById(R.id.L1);
        int selection = spinnerL1.getSelectedItemPosition();
        SharedPreferences.Editor edit = getPreferences(0).edit();
        edit.putInt("selectionL1", selection);
        edit.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void sendMessage(View view) {

        Intent intent = new Intent(A1.this, A2.class);  // Create intent
        EditText editTextT1 = findViewById(R.id.T1);                 // Name the EditText, T1, to editTextT1
        String message = "Hello " + editTextT1.getText().toString(); // Convert editTextT1 content to string
        intent.putExtra(MESSAGE_A1, message);                        // Add editTextT1 content to the intent
        startActivity(intent);                                       // Launches a new activity(A2)
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true); // Moves the task containing this activity to the back of the activity stack.
    }
}
