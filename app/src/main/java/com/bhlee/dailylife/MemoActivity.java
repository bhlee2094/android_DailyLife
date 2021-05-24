package com.bhlee.dailylife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bhlee.dailylife.databinding.ActivityMemoBinding;

public class MemoActivity extends AppCompatActivity {

    private ActivityMemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int day = intent.getIntExtra("day", 0);

        binding.dateTextView.setText(year + " " + month + " " + day + "메모");
    }
}