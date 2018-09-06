package com.kodgames.wechattools.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hongchong on 2017/7/21.
 */
public class HttpClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }

    public static String doPost(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }

    public static String doPost(String url, String body, MediaType type) throws IOException {
        RequestBody requestBody = null;
        if (type == FORM) {
            FormBody.Builder builder = new FormBody.Builder();
            JSONObject json = JSONObject.parseObject(body);
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            requestBody = builder.build();
        } else {
            requestBody = RequestBody.create(type, body);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    public static void doGetAsync(String url, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void doPostAsyn(String url, String json, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void doPostAsyn(String url, String req, MediaType type, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(type, req);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
