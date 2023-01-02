package com.project.dailykids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterStep2Activity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private EditText edtPhone, edtCode;
    private Button btnRequest, btnNext;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String userEmail, userPassword, userNickname, userWho, userPhone;
    private String verificationId;
    private FirebaseAuth mAuth;
    private boolean authOk = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.register_step2_layout);

        // 파이어베이스 관련
        mAuth = FirebaseAuth.getInstance();
        // 데이터 수신
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");
        userNickname = intent.getStringExtra("nickname");
        userWho = intent.getStringExtra("who");
        // 툴바 설정
        mView = findViewById(R.id.register2_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 위젯 지정
        edtPhone = findViewById(R.id.register2_edtPhone);
        edtCode = findViewById(R.id.register2_edtCode);
        btnRequest = findViewById(R.id.register2_btnRequest);
        btnNext = findViewById(R.id.register2_btnNext);
        // 전화 인증 콜백
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG", "Success");
                final String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    edtCode.setText(code);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("TAG", e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationId = s;
            }
        };

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = edtPhone.getText().toString();
                if (userPhone.length() == 11) {
                    sendVerificationCode("+82" + userPhone.substring(1));
                } else
                    Toast.makeText(RegisterStep2Activity.this, "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authOk) {
                    Intent intent = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("nickname", userNickname);
                    intent.putExtra("who", userWho);
                    intent.putExtra("phone", "01050120682");
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterStep2Activity.this, "전화번호 인증을 완료해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authOk = true;
                    edtPhone.setFocusable(false);
                    edtCode.setFocusable(false);
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                } else {
                    Log.d("TAG", task.getException().toString());
                }
            }
        });
    }

    public void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.fadeout);
    }
}
