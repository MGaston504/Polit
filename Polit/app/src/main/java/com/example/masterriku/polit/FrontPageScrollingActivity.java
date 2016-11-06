package com.example.masterriku.polit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        new MyAsyncTask().execute("https://www.govtrack.us/api/v2/bill?sort=number");
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
//                if (line.substring(0, 20) == "\"introduced_date\":"){
//                    line.substring(22);
//             }
                /*
                introduced_date
                    "introduced_date": "2011-01-05",
                current_status_date
                    "current_status_date": "2011-01-05",
                title
                    "lock_title": false,
                terms

                 */
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
//                JSONObject jsonObject = new JSONObject(line);
//                jsonObject
                    sb.append(line + "\n");
//                if (line.substring(0, 20) == "\"introduced_date\":"){
//                    line.substring(22);
//             }
                /*
                introduced_date
                    "introduced_date": "2011-01-05",
                current_status_date
                    "current_status_date": "2011-01-05",
                title
                    "lock_title": false,
                terms

                 */
                }
//            if (sb==null){
//                return "you messed up";
//            }
                //new JSONObject(responseStrBuilder.toString());
                reader.close();
                result = sb.toString();//.split("\n");
            } catch (IOException i){
                result = i.toString();
            }
            return result;
        }

        protected void onPostExecute(String result) {

//            String page = new Communicator().executeHttpGet("Some URL");
//            JSONObject jsonObject = new JSONObject(page);

            TextView textView = (TextView) findViewById(R.id.billList);
            textView.setText(result); //"a regular piece of text"
//            JSONObject obj = new JSONObject("introduced_date");
        }
    }
}
