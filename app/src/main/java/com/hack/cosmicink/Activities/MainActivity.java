package com.hack.cosmicink.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hack.cosmicink.Utilities.Credentials;
import com.hack.cosmicink.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity Log";

    private RequestQueue queue;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.doctorCard.setOnClickListener(v -> navigateNext(1));
        binding.teacherCard.setOnClickListener(v -> navigateNext(2));
        binding.lawyerCard.setOnClickListener(v -> navigateNext(3));
        binding.customCard.setOnClickListener(v -> navigateNext(4));

        generateAccessToken();
    }

    private void navigateNext(int card) {
        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        intent.putExtra("Card", card);
        startActivity(intent);
    }

    private void generateAccessToken() {
        JSONObject param = new JSONObject();
        try {
            param.put("type", "application");
            param.put("appId", Credentials.appID);
            param.put("appSecret", Credentials.appSecret);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "https://api.symbl.ai/oauth2/token:generate", param,
                response -> {
                    try {
                        Credentials.accessToken = response.getString("accessToken");
                        Log.d(TAG, "accessToken: " + Credentials.accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(MainActivity.this, "Could not retrieve access token. Error: "
                            + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Could not retrieve access token. Error: " + error.getMessage());
                });

        queue.add(request);
    }
}