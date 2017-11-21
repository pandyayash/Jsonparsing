package com.example.yk.jsonparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView t1;
    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=(TextView)findViewById(R.id.text1);
        btn1= (Button) findViewById(R.id.button);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSON().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");
            }
        });
    }


    public class JSON extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try {
                URL url=new URL(params[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                String finaljson=buffer.toString();

                JSONObject jsonobject=new JSONObject(finaljson);
                JSONArray jasonarray=jsonobject.getJSONArray("movies");
                StringBuffer finalbuffer=new StringBuffer();
                for (int i=0;i<jasonarray.length();i++){
                    JSONObject finalobject=jasonarray.getJSONObject(i);
                    String moviename=finalobject.getString("movie");
                    int year=finalobject.getInt("year");
                    finalbuffer.append(moviename+"-"+year+"\n");
                }

                return finalbuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }
                try {
                    if (reader!=null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            t1.setText(result);
            super.onPostExecute(result);
        }
    }

}
