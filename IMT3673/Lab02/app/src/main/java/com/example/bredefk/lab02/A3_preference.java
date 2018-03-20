package com.example.bredefk.lab02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class A3_preference extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String[] refreshList = {"10 min.", "1 hour", "1 day"};
    private static String[] limitList = {"10", "20", "50", "100"};
    private Spinner spnRefresh;
    private Spinner spnLimit;
    private EditText txtURL;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3_preference);

        ArrayAdapter<String> adapter_r = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, refreshList);
        ArrayAdapter<String> adapter_l = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, limitList);
        adapter_r.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_l.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SharedPreferences selected = getPreferences(0);

        spnRefresh = findViewById(R.id.spn_refresh);
        spnLimit = findViewById(R.id.spn_limit);
        txtURL = findViewById(R.id.txt_URL);

        spnRefresh.setAdapter(adapter_r);
        spnRefresh.setOnItemSelectedListener(this);
        spnRefresh.setSelection(selected.getInt("selectedRefresh", 0));

        spnLimit.setAdapter(adapter_l);
        spnLimit.setOnItemSelectedListener(this);
        spnLimit.setSelection(selected.getInt("selectedLimit", 0));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Spinner code start //
        spnRefresh = findViewById(R.id.spn_refresh);
        spnLimit = findViewById(R.id.spn_limit);
        int selection_r = spnRefresh.getSelectedItemPosition();
        int selection_l = spnLimit.getSelectedItemPosition();
        saveVariable("selectedRefresh", selection_r);
        saveVariable("selectedLimit", selection_l);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public void saveVariable(String key, int var) {
        SharedPreferences.Editor editor = getPreferences(0).edit();
        editor.putInt(key, var);
        editor.apply();
    }

    public void saveUserPreferences(View view) {
        intent = new Intent(A3_preference.this, A1_list.class);
        txtURL = findViewById(R.id.txt_URL);
        spnRefresh = findViewById(R.id.spn_refresh);
        spnLimit = findViewById(R.id.spn_limit);
        String urlResult = txtURL.getText().toString();
        String refreshResult = spnRefresh.getSelectedItem().toString();
        String limitResult = spnLimit.getSelectedItem().toString();

        int refresh, limit;

        if (!urlResult.matches("")) {
            switch (refreshResult) {
                case "10 min.":
                    refresh = 10 * 60 * 1000;
                    break;
                case "1 hour":
                    refresh = 60 * 60 * 1000;
                    break;
                case "1 day":
                    refresh = 24 * 60 * 60 * 1000;
                    break;
                default:
                    refresh = 24 * 60 * 60 * 1000;
                    break;
            }
            switch (limitResult) {
                case "10":
                    limit = 10;
                    break;
                case "20":
                    limit = 20;
                    break;
                case "50":
                    limit = 50;
                    break;
                case "100":
                    limit = 100;
                    break;
                default:
                    limit = 20;
                    break;
            }
            intent.putExtra("address", urlResult);
            Log.d("RESULT A3 URL", urlResult);
            intent.putExtra("refresh", refresh);
            intent.putExtra("limit", limit);
            setResult(Activity.RESULT_OK, intent);
            setResult(Activity.RESULT_OK, intent);
            startActivity(intent);
        } else {
            Toast.makeText(this, "URL is blank!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(A3_preference.this, A1_list.class));
    }
}
