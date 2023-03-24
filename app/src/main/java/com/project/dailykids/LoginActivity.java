package com.project.dailykids;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private View mView;
    private TextView tvToolbarTitle;
    private Toolbar toolbar;
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnJoin, btnTrial;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private long waitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        setToolbar();
        initView();
        initData();
        setLoginButton();
        setJoinButton();
    }

    private void setToolbar() {
        mView = findViewById(R.id.login_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("로그인");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        edtEmail = findViewById(R.id.login_edtEmail);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.login_btnLogin);
        btnJoin = findViewById(R.id.login_btnJoin);
        btnTrial = findViewById(R.id.login_btnTrial);
    }

    private void initData() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth mAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void setLoginButton() {
        btnLogin.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) { // NullPointerException 발생 위험성 있으므로 TextUtils 활용
                loginUser(edtEmail.getText().toString(), edtPassword.getText().toString());
            } else {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setJoinButton() {
        btnJoin.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, Join1Activity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String id, String password) {
        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) { // 로그인 성공 시,
                    setAuthListener();
                    mAuth.addAuthStateListener(mAuthListener);
                } else// 로그인 실패 시,
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 2000) {
            waitTime = System.currentTimeMillis();
            Toast.makeText(LoginActivity.this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}