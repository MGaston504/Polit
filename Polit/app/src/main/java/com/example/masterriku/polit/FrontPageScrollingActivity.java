package com.example.masterriku.polit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FrontPageScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String category = "bill?";
        String sort = "sort=number";

        new MyAsyncTask().execute(String.format("https://www.govtrack.us/api/v2/%s%s", category, sort ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_front_page_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected String[] grabURL(/*String name*/) throws IOException {

        int pageNum = 0;

        String name = "bill?sort=created";

        URL url = new URL(String.format("https://www.govtrack.us/api/v2/%s", name));

        ////////////////JSON
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String[] result = new String[]{"Error"};

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString().split("\n");

        } catch (Exception e) {
            // Oops

        } finally {
            //woopsss
        }

        return result;

        ///////////////

        /*
        bill?sort=created
        bills/{bill-type}/{bill-type}{number}/data.json
        bills/{bill-type}/{bill-type}{number}/text-versions/{version}/...
        amendments/{amdt-type}/{amdt-type}{amdt-number}/data.json
        votes/{session}/{chamber}{vote-number}/data.json

         */
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

                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                reader.close();
                result = sb.toString();//.split("\n");
            } catch (IOException i){
                result = i.toString();
            }
            return result;
        }

        protected void onPostExecute(String result) {

            List<String> titleList = new ArrayList<>();
            List<String> introList = new ArrayList<>();
            List<String> statusList = new ArrayList<>();

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray objects = jsonObject.getJSONArray("objects");

                for (int i = 0; i < objects.length(); i++) {
                    JSONObject JSONiter = objects.getJSONObject(i);
                    titleList.add(JSONiter.getString("title_without_number"));
                    introList.add(JSONiter.getString("introduced_date"));
                    statusList.add(JSONiter.getString("current_status_date"));
                }
            } catch (JSONException j){
                titleList.add(j.toString());
                introList.add(j.toString());
                statusList.add(j.toString());
            }

            MiddleGround(titleList, introList, statusList);



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

        public void MiddleGround(List<String> titleList, List<String> introList, List<String> statusList){

        }
    }
}
