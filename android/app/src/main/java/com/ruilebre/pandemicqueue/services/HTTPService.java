package com.ruilebre.pandemicqueue.services;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public abstract class HTTPService extends AsyncTask<Void, Void, String> {
    private String url;
    private String method;
    private String body;

    public HTTPService(String url, String method) {
        this.url = url;
        this.method = method;
        this.body = null;
    }

    public HTTPService(String url, String method, String body) {
        this.url = url;
        this.method = method;
        this.body = body;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();

        try {
            //URL url = new URL("http://192.168.1.93:3000/api/v1/stores/get-store-details?name=pingo doce aveiro");
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(this.method);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);

            if (this.body != null) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = this.body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            connection.connect();

            InputStream stream = connection.getResponseCode() >= 250 ? connection.getErrorStream() : connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNext()) {
                resposta.append(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resposta.toString();
    }
}
