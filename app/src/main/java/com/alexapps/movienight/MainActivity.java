package com.alexapps.movienight;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "529915439cfafbc35e2bed2706c2eebd";
        String type = "movie";
        String requestField = "550?";
        String url = "https://api.themoviedb.org/3/"
                + type +"/" + requestField +  "api_key=" + apiKey;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            Log.e (TAG, "Error: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                    Log.v ("Data loaded", response.body().string());
                    }
                } catch (IOException e) {
                    Log.e("Error loading data", "Exeption caught: ", e);
                }
            }
        });

        }
}
