package com.xyz.recycletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String data;
    WeatherData weatherData;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Test");



        new MyAsyncTask().execute();


    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.list_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.dayName.setText(weatherData.getList().get(position).getDt().toString());
            holder.weatherDetails.setText(weatherData.getList().get(position).getWeather().get(0).getDescription());
            holder.maxTemp.setText(weatherData.getList().get(position).getTemp().getMax().toString());
            holder.minTemp.setText(weatherData.getList().get(position).getTemp().getMin().toString());

        }

        @Override
        public int getItemCount() {
            return weatherData.getList().size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView dayName;
        TextView weatherDetails;
        TextView maxTemp;
        TextView minTemp;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageView);
            dayName = itemView.findViewById(R.id.day_name);
            weatherDetails = itemView.findViewById(R.id.weather_detail);
            maxTemp = itemView.findViewById(R.id.max_temp);
            minTemp = itemView.findViewById(R.id.min_temp);


        }
    }

    public String getDataFromUrl(String url)throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public class MyAsyncTask extends AsyncTask {
        ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setTitle("Loading");
            progressBar.setMessage("Please Wait!");
            progressBar.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                data = getDataFromUrl("https://api.openweathermap.org/data/2.5/forecast/daily?APPID=c13159d2d9b7d01343afbc8acde7572b&q=London&mode=json&units=metric&cnt=15");
                weatherData = new Gson().fromJson(data, WeatherData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            rv = findViewById(R.id.recycle);
            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rv.setAdapter(new MyAdapter());
            progressBar.dismiss();

        }
    }

}




