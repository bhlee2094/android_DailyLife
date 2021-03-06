package com.bhlee.dailylife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bhlee.dailylife.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        binding.btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView)binding.btnSignIn.getChildAt(0);
        textView.setText("Google ???????????? ?????????");
        binding.btnSignIn.setOnClickListener(this);
        binding.btnJoin.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signIn:
            {
                signIn();
                break;
            }
            case R.id.btn_join:
            {
                Intent intent = new Intent(this, JoinActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login:
            {
                if(binding.editTextTextEmailAddress2.getText().toString().length() > 0 && binding.editTextTextPassword2.getText().toString().length() > 0){
                    firebaseAuth.signInWithEmailAndPassword(binding.editTextTextEmailAddress2.getText().toString(), binding.editTextTextPassword2.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
                break;
            }
        }

    }

    private void signIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() { // ????????? ??????????????? ?????? ??????
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        checkUser(firebaseUser);
    }

    private void checkUser(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            }catch (ApiException ignored){

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            googleSignInClient.signOut();
                            Toast.makeText(MainActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();
                            checkUser(firebaseAuth.getCurrentUser());
                        } else{
                            Toast.makeText(MainActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}