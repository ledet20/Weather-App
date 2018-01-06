package com.ledetawano.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadJSON task = new DownloadJSON();

        String city = "London";

        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=ea574594b9d36ab688642d5fbeab847e");
    }

    public class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            String result = "";
            HttpURLConnection httpURLConnection;

            try {
                // url is surrounded by try and catch because it could fail
                url = new URL(urls[0]);
                httpURLConnection =  (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                // create a variable that will read our data from input stream
                int data = inputStreamReader.read();

                while(data != -1) {

                    char current = (char) data;

                    result += current;

                    data = inputStreamReader.read();
                }

                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();

            }


            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("result", result);

        }


    }

}
