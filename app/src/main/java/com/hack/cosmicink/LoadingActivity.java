package com.hack.cosmicink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hack.cosmicink.databinding.ActivityLoadingBinding;

public class LoadingActivity extends AppCompatActivity {

    private ActivityLoadingBinding binding;

    private int[] backgrounds = {R.drawable.doctor, R.drawable.teacher, R.drawable.law, R.drawable.custom};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int card = intent.getIntExtra("Card", 0);

        if (card != 0)
            binding.loadingLayout.setBackgroundResource(backgrounds[card - 1]);
        else
            binding.loadingLayout.setBackgroundResource(backgrounds[3]);
    }
}