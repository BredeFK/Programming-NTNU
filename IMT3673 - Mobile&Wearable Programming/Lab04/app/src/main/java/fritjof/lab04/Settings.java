package fritjof.lab04;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    private String[] languages = {"German", "Norwegian", "English"};
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
/*
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.dropDownLanguages);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                changeLanguage(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }


    private void changeLanguage(String language) {
        Configuration config;
        config = new Configuration(getResources().getConfiguration());
        switch (language) {
            case "German":
                config.locale = new Locale("de");
                break;

            case "Norwegian":
                config.locale = new Locale("no");
                break;

            default:
                config.locale = new Locale("en");
        }
    }
}
