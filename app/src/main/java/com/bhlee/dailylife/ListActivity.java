package com.bhlee.dailylife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityListBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityListBinding binding;
    private String listId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        listId = intent.getStringExtra("listId");
        String listTitle = intent.getStringExtra("listTitle");
        String masterId = intent.getStringExtra("masterId");
        db = FirebaseFirestore.getInstance();

        binding.titleTextView.setText(listTitle);
        binding.floatingActionButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.callFunction();
    }

    private class CustomDialog { //inner class

        private Context context;

        public CustomDialog(Context context) {
            this.context = context;
        }

        public void callFunction() {
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog2);
            dialog.show();

            final EditText title = (EditText)dialog.findViewById(R.id.listTitle);
            final Button okButton = (Button)dialog.findViewById(R.id.okButton);
            final Button cancleButton = (Button)dialog.findViewById(R.id.cancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("uid", title.getText().toString());
                    db.collection("dailyList/" + listId + "/addFriends").document(title.getText().toString()).set(map, SetOptions.merge());
                    Toast.makeText(ListActivity.this, "추가를 완료했습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            cancleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}