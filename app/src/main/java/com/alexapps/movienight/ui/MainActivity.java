package com.alexapps.movienight.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alexapps.movienight.R;

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
        String requestField = "549?";
        String url = "https://api.themoviedb.org/3/"
                + type + "/" + requestField + "api_key=" + apiKey;

        if (networkIsAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error: ", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.v("Data request", response.body().string());
                        if (response.isSuccessful()) {
                        } else {
                            alertError();
                        }
                    } catch (IOException e) {
                        Log.e("Error loading data", "Exeption caught: ", e);
                    }
                }
            });

        } else
        {
            alertInternetError();
        }

    }

    private void alertInternetError() {
        AlertNoInternetFragment dialog = new AlertNoInternetFragment();
        dialog.show(getFragmentManager(),getString(R.string.no_internet_tag));
    }

    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (network !=null && network.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertError() {
    AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getFragmentManager(),getString(R.string.error_dialog));
    }

}
