package com.nicoft.bewise;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nicolas on 23/04/2016.
 */
public class AsyncURL extends AsyncTask<String,Integer,String> {

    protected String doInBackground(String... strs) {
        String str="";
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(strs[0])
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                str = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {

    }
}
