package com.example.masterriku.polit;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FrontPageScrollingActivity extends AppCompatActivity {

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page_scrolling);

        //final Spinner spinner = (Spinner) findViewById(R.id.spinner)
        String sort = "current_status_date";

        new MyAsyncTask().execute(String.format("https://www.govtrack.us/api/v2/bill?sort=-%s", sort));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_front_page_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FrontPageScrolling Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = null;

            try {
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder(inputStream.available());

                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                result = sb.toString();//.split("\n");
            } catch (IOException i) {
                result = i.toString();
            }
            return result;
        }

        protected void onPostExecute(String result) {

            List<String> titleList = new ArrayList<>();
            List<String> introList = new ArrayList<>();
            List<String> statusList = new ArrayList<>();
            List<String> webList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray objects = jsonObject.getJSONArray("objects");

                for (int i = 0; i < objects.length(); i++) {
                    JSONObject JSONiter = objects.getJSONObject(i);
                    titleList.add(JSONiter.getString("title_without_number"));
                    introList.add(JSONiter.getString("introduced_date"));
                    statusList.add(JSONiter.getString("current_status_date"));
                    webList.add(JSONiter.getString("link"));
                }
            } catch (JSONException j) {
                titleList.add(j.toString());
                introList.add(j.toString());
                statusList.add(j.toString());
                webList.add(j.toString());
            }

            MiddleGround(titleList, introList, statusList, webList);


//            TextView textView = (TextView) findViewById(R.id.billList);
//
//            StringBuilder sb = new StringBuilder();
//            for (String x: titleList) {
//                sb.append(x + "\n");
//            }
//            result = sb.toString();
//
//            textView.setText(result); //"a regular piece of text"
//            JSONObject obj = new JSONObject("introduced_date");
        }

        public void MiddleGround(List<String> titleList, List<String> introList, List<String> statusList, List<String> webList) {


            ListView listView = (ListView) findViewById(R.id.BillList);
            List<String> listItems = new ArrayList<>();

            for (int i = 0; i < titleList.size(); i++) {
                listItems.add(String.format("\n%s\n\nINTRODUCED:\t\t%s\nDATE OF LAST MAJOR ACTION:\t\t%s\n",
                        titleList.get(i), introList.get(i), statusList.get(i)));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(FrontPageScrollingActivity.this,
                    android.R.layout.simple_list_item_1, listItems);

            listView.setAdapter(adapter);
        }
    }
}
