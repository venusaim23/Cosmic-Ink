package com.hack.cosmicink.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hack.cosmicink.Adapters.MessageAdapter;
import com.hack.cosmicink.Models.Message;
import com.hack.cosmicink.R;
import com.hack.cosmicink.Utilities.Credentials;
import com.hack.cosmicink.databinding.FragmentTopicsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicsFragment extends Fragment {

    private static final String TAG = "Topics Fragment";
    private FragmentTopicsBinding binding;

    private String conversationId;
    private Context context;

    private String URL = "https://api.symbl.ai/v1/conversations/";
    private String append = "/topics";

    private List<Message> messages;
    private MessageAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TopicsFragment(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopicsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerTopics.setHasFixedSize(true);
        binding.recyclerTopics.setLayoutManager(new LinearLayoutManager(context));

        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages, context);
        binding.recyclerTopics.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL + conversationId + append, null,
                response -> {
                    Log.d(TAG, "Messages Response: " + response.toString());
                    retrieveMessages(response);
                }, error -> Toast.makeText(context, "Could not get messages. Error: " +
                error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Credentials.accessToken);
//                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }

    private void retrieveMessages(JSONObject response) {
        try {
            JSONArray messageArray = response.getJSONArray("messages");

            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject message = messageArray.getJSONObject(i);
                String id = message.getString("id");
                String text = message.getString("text");
                String startTime = message.getString("startTime");
                String endTime = message.getString("endTime");
                String convoID = message.getString("conversationId");

                Message m = new Message(id, text, startTime, endTime, convoID);
                messages.add(m);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}