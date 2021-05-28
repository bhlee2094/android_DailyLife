package com.bhlee.dailylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bhlee.dailylife.databinding.ActivityAccountBookBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AccountBookActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAccountBookBinding binding;
    private FirebaseFirestore db;
    private String listId;
    private FragmentEx fragmentEx;
    private FragmentIn fragmentIn;
    private FragmentInEx fragmentInEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        listId = intent.getStringExtra("listId");
        binding.floatingActionButton4.setOnClickListener(this);
        binding.Ex.setOnClickListener(this);
        binding.in.setOnClickListener(this);
        binding.inAndEx.setOnClickListener(this);

        fragmentEx = new FragmentEx();
        fragmentIn = new FragmentIn();
        fragmentInEx = new FragmentInEx();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentInEx).commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton4:
            {
                AccountDialog accountDialog = new AccountDialog(this);
                accountDialog.callFunction();
                break;
            }
            case R.id.inAndEx:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentInEx).commit();
                break;
            }
            case R.id.in:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentIn).commit();
                break;
            }
            case R.id.Ex:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentEx).commit();
                break;
            }
        }
    }

    private class AccountDialog {
        private final Context context;

        public AccountDialog(Context context){
            this.context = context;
        }

        public void callFunction(){
            final Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.account_dialog);
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.45);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            final RadioButton in = (RadioButton)dialog.findViewById(R.id.rb_in);
            final RadioButton ex = (RadioButton)dialog.findViewById(R.id.rb_ex);
            final EditText accountTitle = (EditText)dialog.findViewById(R.id.accountTitle);
            final EditText money = (EditText)dialog.findViewById(R.id.accountMoney);
            final Button okButton = (Button)dialog.findViewById(R.id.okButton);
            final Button cancleButton = (Button)dialog.findViewById(R.id.cancelButton);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("accountTitle", accountTitle.getText().toString());
                    map.put("accountMoney", money.getText().toString());
                    if(in.isChecked()){
                        db.collection("dailyList/"+listId+"/in").document().set(map, SetOptions.merge());
                    }else if(ex.isChecked()){
                        db.collection("dailyList/"+listId+"/ex").document().set(map, SetOptions.merge());
                    }
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