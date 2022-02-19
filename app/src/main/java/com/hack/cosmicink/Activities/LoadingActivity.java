package com.hack.cosmicink.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.hack.cosmicink.R;
import com.hack.cosmicink.Utilities.Credentials;
import com.hack.cosmicink.databinding.ActivityLoadingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "Loading Log";
    private ActivityLoadingBinding binding;

    private final int[] backgrounds = {R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_3, R.drawable.bg_4};

    private String conversationId;
    private String jobId;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int card = intent.getIntExtra("Card", 0);

        if (card != 0)
            binding.loadingLayout.setBackgroundResource(backgrounds[card - 1]);
        else
            binding.loadingLayout.setBackgroundResource(backgrounds[3]);

        binding.meetRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.urlInputLayout.setVisibility(View.GONE);
            else
                binding.urlInputLayout.setVisibility(View.VISIBLE);
        });

        binding.goBtn.setOnClickListener(v -> {
            if (binding.radioGroup.getCheckedRadioButtonId() == R.id.video_radio)
                uploadVideo();
            else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.audio_radio)
                uploadAudio();
            else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.meet_radio)
                openDialog();
        });

        binding.convoBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);

            View view = getLayoutInflater().inflate(R.layout.simple_dialog_layout, null);
            builder.setTitle(getResources().getString(R.string.enter_conversation_id))
                    .setView(view).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();

            TextInputEditText et = view.findViewById(R.id.edittext);
            Button go = view.findViewById(R.id.dialog_btn);
            go.setOnClickListener(v1 -> {
                conversationId = et.getText().toString().trim();
                getConversation();
            });
        });
    }

    private void uploadVideo() {
        String URL = binding.urlEt.getText().toString().trim();
        if (URL.isEmpty()) {
            Toast.makeText(this, "URL cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject param = new JSONObject();
        try {
            param.put("url", URL);
            param.put("confidenceThreshold", 0.6);
            param.put("timezoneOffset", 0);
            param.put("name", "Video Session");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "https://api.symbl.ai/v1/process/video/url", param,
                response -> {
                    try {
                        conversationId = response.getString("conversationId");
                        jobId = response.getString("jobId");

                        Log.d(TAG, "Conversation ID: " + conversationId);
                        Log.d(TAG, "Job ID: " + jobId);

                        getStatus();
                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        try {
                            Log.d(TAG, "Message: " + response.getString("message"));
                        } catch (JSONException jsonException) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, error -> {
                    Toast.makeText(LoadingActivity.this,
                            "Could not send video URL. Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Video POST Error: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Credentials.accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }

    private void uploadAudio() {

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.phone_dialog_layout, null);
        builder.setView(v).setCancelable(true).setTitle("Fill the meeting details")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
    }

    private void getStatus() {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "https://api.symbl.ai/v1/job/" + jobId, null,
                response -> {
                    try {
                        String status = response.getString("status");
                        binding.statusTv.setText(status);

                        if (status.equals("completed")) {
                            getConversation();
                        } else {
                            new Handler().postDelayed(this::getStatus, 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(LoadingActivity.this,
                "Could not get status. Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Credentials.accessToken);
                return headers;
            }
        };

        queue.add(request);
    }

    private void getConversation() {
        Intent intent = new Intent(LoadingActivity.this, DetailsActivity.class);
        intent.putExtra("Conversation ID", conversationId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}