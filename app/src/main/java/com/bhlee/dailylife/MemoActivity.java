package com.bhlee.dailylife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityListBinding;
import com.bhlee.dailylife.databinding.ActivityMemoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MemoActivity extends AppCompatActivity {

    private ActivityMemoBinding binding;
    private FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String listId = intent.getStringExtra("listId");
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int day = intent.getIntExtra("day", 0);
        int hash = intent.getIntExtra("hash", 0);
        String str_hash = String.valueOf(hash);

        db.collection("dailyList/"+listId+"/memo").document(str_hash).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            binding.memoEditText.setText(documentSnapshot.getString("memo"));
                        }
                    }
                });

        binding.dateTextView.setText(year + " " + month + " " + day + " 메모");
        binding.btnMemoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("memo", binding.memoEditText.getText().toString());
                db.collection("dailyList/"+ listId + "/memo").document(str_hash).set(map, SetOptions.merge());
                Toast.makeText(MemoActivity.this, "메모 저장완료", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}