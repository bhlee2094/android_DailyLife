package com.bhlee.dailylife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityJoinBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private ActivityJoinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.floatingActionButton3.setOnClickListener(this);
        binding.btnJoinFinish.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_joinFinish:
            {
                if(binding.editTextTextEmailAddress.getText().toString().length() > 0 && binding.editTextTextPassword.getText().toString().length() >= 6){
                    firebaseAuth.createUserWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(JoinActivity.this, "email 형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(this, "password 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.floatingActionButton3:
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}