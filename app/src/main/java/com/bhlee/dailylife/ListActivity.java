package com.bhlee.dailylife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityListBinding;
import com.bhlee.dailylife.databinding.CustomDialogBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_Memo = 2;
    private String listId;
    private FirebaseFirestore db;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private ActivityListBinding binding;
    private CalendarDay calendarDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        listId = intent.getStringExtra("listId");
        String listTitle = intent.getStringExtra("listTitle");
        db = FirebaseFirestore.getInstance();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        binding.titleTextView.setText(listTitle);
        binding.floatingActionButton2.setOnClickListener(this);
        binding.floatingActionButtonAccountBook.setOnClickListener(this);
        binding.floatingActionButtonAddFriends.setOnClickListener(this);
        binding.materialCalendarView.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator()
        );
        binding.materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent memoIntent = new Intent(ListActivity.this, MemoActivity.class);
                memoIntent.putExtra("listId", listId);
                memoIntent.putExtra("year", date.getYear());
                memoIntent.putExtra("month", date.getMonth() + 1);
                memoIntent.putExtra("day", date.getDay());
                memoIntent.putExtra("hash", date.hashCode());
                calendarDay = date;
                startActivityForResult(memoIntent, RC_Memo);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_Memo){
            if(resultCode == RESULT_OK){
                binding.materialCalendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(calendarDay)));
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton2:
            {
                anim();
                break;
            }
            case R.id.floatingActionButton_addFriends:
            {
                CustomDialog customDialog = new CustomDialog(this);
                customDialog.callFunction();
                break;
            }
            case R.id.floatingActionButton_accountBook:
            {
                Intent intent = new Intent(this, AccountBookActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void anim(){
        if(isFabOpen){
            binding.floatingActionButton2.setImageResource(R.drawable.ic_baseline_add_24);
            binding.floatingActionButtonAddFriends.startAnimation(fab_close);
            binding.floatingActionButtonAccountBook.startAnimation(fab_close);
            binding.floatingActionButtonAddFriends.setClickable(false);
            binding.floatingActionButtonAccountBook.setClickable(false);
            isFabOpen = false;
        }else{
            binding.floatingActionButton2.setImageResource(R.drawable.ic_baseline_close_24);
            binding.floatingActionButtonAddFriends.startAnimation(fab_open);
            binding.floatingActionButtonAccountBook.startAnimation(fab_open);
            binding.floatingActionButtonAddFriends.setClickable(true);
            binding.floatingActionButtonAccountBook.setClickable(true);
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

            TextView textView = (TextView)dialog.findViewById(R.id.title);
            final EditText title = (EditText)dialog.findViewById(R.id.listTitle);
            final Button okButton = (Button)dialog.findViewById(R.id.okButton);
            final Button cancleButton = (Button)dialog.findViewById(R.id.cancelButton);

            textView.setText("친구 추가히기");
            title.setHint("친구 ID를 입력하세요");

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