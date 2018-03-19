package com.example.bredefk.lab02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class A1_list extends AppCompatActivity {

    private static String ADDRESS = null;
    private static String empty = "empty";
    private static int LIMIT;
    private static int REFRESH;
    private static URL url;
    private Intent intent;
    private ListView listview;
    private Adapter listAdapter;
    private String rssURL;
    private RssFeedModel selected;
    private List<RssFeedModel> feedModelList;
    private Bundle extras;
    private Handler fetch = new Handler();
    private Runnable Feed = new Runnable() {
        @Override
        public void run() {
            new FetchFeedTask().execute();
            fetch.postDelayed(this, REFRESH);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1_list);

        getVariables();

        if (!ADDRESS.equals(empty)) {
            new FetchFeedTask().execute();
            listen();
            Feed.run();
        }
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(A1_list.this, A3_preference.class);
        startActivity(intent);
    }

    public void refresh(View view) {
        if (!ADDRESS.equals(empty)) {
            Toast.makeText(A1_list.this, "Refreshing...", Toast.LENGTH_LONG).show();
            new FetchFeedTask().execute();
        } else {
            Toast.makeText(A1_list.this, "Nothing to refresh", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void listen() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                selected = (RssFeedModel) parent.getAdapter().getItem(pos);
                rssURL = selected.link;
                intent = new Intent(A1_list.this, A2_display.class);
                intent.putExtra("rssURL", rssURL);
                startActivity(intent);
            }
        });
    }

    private void getVariables() {
        SharedPreferences saved = getPreferences(0);
        listview = findViewById(R.id.content);
        extras = getIntent().getExtras();
        if (extras != null) {
            ADDRESS = extras.getString("address");
            LIMIT = extras.getInt("limit");
            REFRESH = extras.getInt("refresh");
        }

        if (ADDRESS == null) {
            ADDRESS = saved.getString("key_address", empty);
        }
        if (LIMIT == 0) {
            LIMIT = saved.getInt("limit", -1);
        }
        if (REFRESH == 0) {
            REFRESH = saved.getInt("refresh", -1);
        }
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null) {
                    continue;
                }

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (isItem) {
                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    }
                }

                if (title != null && link != null) {
                    RssFeedModel item = new RssFeedModel(title, link);
                    items.add(item);

                    title = null;
                    link = null;
                    isItem = false;
                }
            }
            return items;
        } finally {
            inputStream.close();
        }
    }

    public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private static final String TAG = "A1_list.java";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(ADDRESS)) {
                return false;
            }
            try {
                if (!ADDRESS.startsWith("http://") && !ADDRESS.startsWith("https://")) {
                    ADDRESS = "https://" + ADDRESS; // DOES NOT WORK WITH HTTP:// :)))))
                }
                url = new URL(ADDRESS);
                InputStream inputStream = url.openConnection().getInputStream();
                feedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error IO", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error XML", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            SharedPreferences.Editor editor = getPreferences(0).edit();
            if (success) {
                // setup list
                listAdapter = new Adapter(A1_list.this, R.layout.rss_items, feedModelList, LIMIT);
                listview.setAdapter(listAdapter);
                // send url back to A3
                intent = new Intent(A1_list.this, A3_preference.class);
                intent.putExtra("url_key", url.toString());
                // Save limit
                editor.putInt("limit", LIMIT);
                Toast.makeText(A1_list.this, "Success ┬─┬ ノ( ゜-゜ノ)", Toast.LENGTH_SHORT).show();
            } else {
                ADDRESS = empty;
                REFRESH = -1;
                Toast.makeText(A1_list.this, "Not valid URL (╯°□°）╯︵ ┻━┻", Toast.LENGTH_SHORT).show();

            }
            // save address
            editor.putString("key_address", ADDRESS);
            editor.apply();
        }
    }
}
