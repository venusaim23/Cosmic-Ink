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

import org.json.JSONArray;
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
            String URL = binding.urlEt.getText().toString().trim();

            if (binding.radioGroup.getCheckedRadioButtonId() == R.id.meet_radio)
                openDialog();
            else {
                if (URL.isEmpty())
                    Toast.makeText(this, "URL cannot be empty!", Toast.LENGTH_SHORT).show();
                else {
                    if (binding.radioGroup.getCheckedRadioButtonId() == R.id.video_radio)
                        uploadVideo(URL);
                    else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.audio_radio)
                        uploadAudio(URL);

                    binding.progressBarLoad.setVisibility(View.VISIBLE);
                }
            }
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

    private void uploadVideo(String URL) {
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
            binding.progressBarLoad.setVisibility(View.GONE);
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

    private void uploadAudio(String URL) {
        JSONObject param = new JSONObject();
        try {
            param.put("url", URL);
            param.put("confidenceThreshold", 0.6);
            param.put("timezoneOffset", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "https://api.symbl.ai/v1/process/audio/url", param,
                response -> {
                    try {
                        conversationId = response.getString("conversationId");
                        jobId = response.getString("jobId");

                        Log.d(TAG, "Conversation ID: " + conversationId);
                        Log.d(TAG, "Job ID: " + jobId);

                        getStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(LoadingActivity.this,
                    "Could not send audio URL. Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Audio POST Error: " + error.getMessage());
            binding.progressBarLoad.setVisibility(View.GONE);
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

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog diag;

        View v = getLayoutInflater().inflate(R.layout.phone_dialog_layout, null);
        builder.setView(v).setCancelable(true).setTitle("Fill the meeting details")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        diag = builder.create();
        diag.show();

        TextInputEditText phoneET = v.findViewById(R.id.phone_et_dialog);
        TextInputEditText dtmfET = v.findViewById(R.id.dtmf_et_dialog);
        TextInputEditText emailET = v.findViewById(R.id.email_et_dialog);

        v.findViewById(R.id.connect_btn_phone).setOnClickListener(v1 -> {
            String phone = phoneET.getText().toString();
            String dtmf = dtmfET.getText().toString();
            String email = emailET.getText().toString();

            startCall(phone, dtmf, email);

            diag.dismiss();
            binding.progressBarLoad.setVisibility(View.VISIBLE);
        });
    }

    private void startCall(String phone, String dtmf, String email) {
        JSONObject param = new JSONObject();
        try {
            param.put("operation", "start");

            JSONObject endPoint = new JSONObject();
            endPoint.put("type", "pstn");
            endPoint.put("phoneNumber", phone);
            endPoint.put("dtmf", dtmf);

            param.put("endpoint", endPoint);

            JSONArray actions = new JSONArray();
            JSONObject action1 = new JSONObject();
            action1.put("invokeOn", "stop");
            action1.put("name", "sendSummaryEmail");
            action1.put("parameters", new JSONObject().put("emails", new JSONArray().put(email)));
            actions.put(action1);

            param.put("actions", actions);
            param.put("data", new JSONObject().put("session",
                    new JSONObject().put("name", "Android Meeting")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "https://api.symbl.ai/v1/endpoint:connect", param,
                response -> {
                    //Todo get call details and stop loading

                    binding.progressBarLoad.setVisibility(View.GONE);
                }, error -> {
            Toast.makeText(LoadingActivity.this,
                    "Could not join meeting. Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Meeting POST Error: " + error.getMessage());
            binding.progressBarLoad.setVisibility(View.GONE);
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

                    binding.progressBarLoad.setVisibility(View.GONE);
                }, error -> {
            Toast.makeText(LoadingActivity.this,
                    "Could not get status. Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            binding.progressBarLoad.setVisibility(View.GONE);
        }) {
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