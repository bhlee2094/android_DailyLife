package com.bhlee.dailylife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder dlg = new AlertDialog.Builder(HomeActivity.this);
            dlg.setTitle("로그아웃"); //제목
            dlg.setMessage("로그아웃 하시겠습니까?"); // 메시지
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    logout(firebaseAuth);
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomeActivity.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}