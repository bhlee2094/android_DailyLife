package com.bhlee.dailylife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private ActivityHomeBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String str_listTitle;
    private ArrayList<DailyList> dailyLists;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(binding.recyclerView.getContext(), 1));
        binding.floatingActionButton.setOnClickListener(this);
        binding.floatingActionButtonAdd.setOnClickListener(this);
        binding.floatingActionButtonReflesh.setOnClickListener(this);
        binding.floatingActionButtonSend.setOnClickListener(this);

        dailyLists = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("dailyList")
                .whereEqualTo("masterId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                String listTitle = documentSnapshot.getString("listTitle");
                                String listId = documentSnapshot.getString("listId");
                                String masterId = documentSnapshot.getString("masterId");
                                DailyList dailyList = new DailyList(listTitle, listId, masterId);
                                dailyLists.add(dailyList);
                            }
                        }
                    }
                });

        db.collection("dailyList").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                String listTitle = documentSnapshot.getString("listTitle");
                                String listId = documentSnapshot.getString("listId");
                                String masterId = documentSnapshot.getString("masterId");
                                db.collection("dailyList/"+ listId + "/addFriends").whereEqualTo("uid", user.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful() && task.getResult() != null){
                                                    for(DocumentSnapshot documentSnapshot1 : task.getResult()) {
                                                        DailyList dailyList = new DailyList(listTitle, listId, masterId);
                                                        dailyLists.add(dailyList);
                                                    }
                                                    recyclerViewAdapter = new RecyclerViewAdapter(dailyLists);
                                                    binding.recyclerView.setAdapter(recyclerViewAdapter);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

        binding.uidTextView.setText("유저 ID: " + user.getUid());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder dlg = new AlertDialog.Builder(HomeActivity.this);
            dlg.setTitle("로그아웃"); //제목
            dlg.setMessage("로그아웃 하시겠습니까?"); // 메시지
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    logout(firebaseAuth);
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dlg.show();
        }
        return false;
    }

    private void logout(FirebaseAuth firebaseAuth){
        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(HomeActivity.this, firebaseAuth.getUid() + "님이 로그아웃하셨습니다", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton:
            {
                anim();
                break;
            }
            case R.id.floatingActionButton_add:
            {
                CustomDialog customDialog = new CustomDialog(this);
                customDialog.callFunction();
                break;
            }
            case R.id.floatingActionButton_reflesh:
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            }
            /*case R.id.floatingActionButton_send:
            {
                CustomDialog2 customDialog2 = new CustomDialog2(this);
                customDialog2.callFunction();
                break;
            }*/
        }
    }

    private void anim(){
        if(isFabOpen){
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_add_24);
            /*binding.floatingActionButtonSend.startAnimation(fab_close);*/
            binding.floatingActionButtonReflesh.startAnimation(fab_close);
            binding.floatingActionButtonAdd.startAnimation(fab_close);
            /*binding.floatingActionButtonSend.setClickable(false);*/
            binding.floatingActionButtonReflesh.setClickable(false);
            binding.floatingActionButtonAdd.setClickable(false);
            isFabOpen = false;
        }else{
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_close_24);
            /*binding.floatingActionButtonSend.startAnimation(fab_open);*/
            binding.floatingActionButtonReflesh.startAnimation(fab_open);
            binding.floatingActionButtonAdd.startAnimation(fab_open);
            /*binding.floatingActionButtonSend.setClickable(true);*/
            binding.floatingActionButtonReflesh.setClickable(true);
            binding.floatingActionButtonAdd.setClickable(true);
            isFabOpen = true;
        }
    }

    private class CustomDialog { //inner class

        private final Context context;

        public CustomDialog(Context context) {
            this.context = context;
        }

        public void callFunction() {
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.show();

            final EditText title = (EditText)dialog.findViewById(R.id.listTitle);
            final Button okButton = (Button)dialog.findViewById(R.id.okButton);
            final Button cancleButton = (Button)dialog.findViewById(R.id.cancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str_listTitle = title.getText().toString();
                    String listId = db.collection("dailyList").document().getId();
                    Map<String, Object> map = new HashMap<>();
                    map.put("listTitle", str_listTitle);
                    map.put("listId", listId);
                    map.put("masterId", user.getUid());
                    db.collection("dailyList").document(listId).set(map, SetOptions.merge());
                    Toast.makeText(HomeActivity.this, "생성을 완료했습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            cancleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, "취소했습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    /*private class CustomDialog2 { //inner class

        private Context context;

        public CustomDialog2(Context context) {
            this.context = context;
        }

        public void callFunction() {
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog3);
            dialog.show();

            final EditText title = (EditText)dialog.findViewById(R.id.listTitle);
            final Button okButton = (Button)dialog.findViewById(R.id.okButton);
            final Button cancleButton = (Button)dialog.findViewById(R.id.cancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = title.getText().toString();
                    String id = user.getUid();
                    if(number.length() > 0){
                        sendSMS(number, id);
                        Toast.makeText(HomeActivity.this, "메시지 전송 성공", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(HomeActivity.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

            cancleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, "취소했습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    private void sendSMS(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }*/
}