package xyz.telosaddon.yuno.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TelosApi {
    private static final String BASE_URL = "https://api.telosrealms.com/lookup/player/";

    private final OkHttpClient client;

    public TelosApi() {
        this.client = new OkHttpClient();
    }

    public void fetchPlayerData(String playerName, ApiResponseCallback callback) {
        String url = BASE_URL + playerName;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                    response.close();
                } else {
                    callback.onFailure(new IOException("Unexpected code: " + response));
                    response.close();
                }
            }
        });
    }

    public interface ApiResponseCallback {
        void onSuccess(String response);
        void onFailure(IOException e);
    }
}
