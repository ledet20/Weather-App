package com.ledetawano.weatherapp;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText userInputCity;
    Button checkWeather;
    String city;

    public void searchForCity(View view) {

       String userInputForCity =  userInputCity.getText().toString();

        DownloadJSON task = new DownloadJSON();

        city = userInputForCity;

        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=ea574594b9d36ab688642d5fbeab847e");



    }

    public double kelvinToFahrenheit(double kelvin) {

        double initialConversion = kelvin * (1.8);

        return Math.round(initialConversion - 459.67);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInputCity = (EditText)  findViewById(R.id.userInputCity);
        checkWeather = (Button) findViewById(R.id.checkWeather);




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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String weatherObject = jsonObject.getString("weather");
                String tempObject = jsonObject.getString("main");
                String nameOfCity = jsonObject.getString("name");

                String tempVal = tempObject.substring(8,14);

                double kelvinVal = Double.parseDouble(tempVal);

                double val = kelvinToFahrenheit(kelvinVal);

                Log.i("Double temp", Double.toString(val));

                // parsing weather array to get description of Temp value
                JSONArray arr = new JSONArray(weatherObject);

                for(int i = 0; i < arr.length(); i++) {

                    JSONObject description = arr.getJSONObject(i);

                    Log.i("desc", description.getString("description"));

                }

                Log.i("temp", tempObject);
                Log.i("name", nameOfCity);
                Log.i("weather", weatherObject);
                Log.i("result", jsonObject.toString());


            } catch (JSONException e) {

                e.printStackTrace();

            }


        }


    }

}
