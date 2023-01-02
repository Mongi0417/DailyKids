package com.project.dailykids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterStep1Activity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvCheckEmail, tvPasswordLength, tvCheckPassword, tvCheckNickname;
    private EditText edtEmail, edtPassword, edtCheckPassword, edtNickname;
    private RadioButton rdButton;
    private RadioGroup rdGroup;
    private Button btnCheckEmail, btnCheckNickname, btnNext;
    private String userEmail, userPassword, userNickname;
    private boolean checkEmailExist = true, checkPasswordLength = false, checkSamePassword = false, checkNicknameLength = false, checkNicknameExist = true;
    private DatabaseReference mRef;
    private SimpleUserDTO simpleUserDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.register_step1_layout);

        // 툴바 설정
        mView = findViewById(R.id.register1_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 위젯 지정
        tvCheckEmail = findViewById(R.id.register1_tvCheckEmail);
        tvPasswordLength = findViewById(R.id.register1_tvPasswordLength);
        tvCheckPassword = findViewById(R.id.register1_tvCheckPassword);
        tvCheckNickname = findViewById(R.id.register1_tvCheckNickname);
        edtEmail = findViewById(R.id.register1_edtEmail);
        edtPassword = findViewById(R.id.register1_edtPassword);
        edtCheckPassword = findViewById(R.id.register1_edtCheckPassword);
        edtNickname = findViewById(R.id.register1_edtNickname);
        rdGroup = findViewById(R.id.register1_rdGroup);
        btnCheckEmail = findViewById(R.id.register1_btnCheckEmail);
        btnCheckNickname = findViewById(R.id.register1_btnCheckNickname);
        btnNext = findViewById(R.id.register1_btnNext);
        // 파이어베이스 관련
        mRef = FirebaseDatabase.getInstance().getReference();

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userPassword = edtPassword.getText().toString();
                if (userPassword.length() < 6) {
                    tvPasswordLength.setVisibility(View.VISIBLE);
                    checkPasswordLength = false;
                } else {
                    tvPasswordLength.setVisibility(View.GONE);
                    checkPasswordLength = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtCheckPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCheckPassword.setVisibility(View.VISIBLE);
                if (edtPassword.getText().toString().equals(edtCheckPassword.getText().toString())) {
                    checkSamePassword = true;
                    tvCheckPassword.setTextColor(Color.BLUE);
                    tvCheckPassword.setText("비밀번호와 일치합니다.");
                } else {
                    checkSamePassword = false;
                    tvCheckPassword.setTextColor(Color.RED);
                    tvCheckPassword.setText("비밀번호와 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userNickname = edtNickname.getText().toString();
                if (userNickname.length() < 2 || userNickname.length() > 8) {
                    checkNicknameLength = false;
                    tvCheckNickname.setTextColor(Color.RED);
                    tvCheckNickname.setText("2-8자 이내로 설정해주세요.");
                    tvCheckNickname.setVisibility(View.VISIBLE);
                } else {
                    checkNicknameLength = true;
                    tvCheckNickname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                checkEmailExist = false;
                userEmail = edtEmail.getText().toString();
                mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            simpleUserDTO = item.getValue(SimpleUserDTO.class);
                            String strEmail = simpleUserDTO.getEmail();
                            if (userEmail.equals(strEmail)) {
                                checkEmailExist = true;
                                break;
                            }
                        }
                        if (!checkEmailExist) {
                            tvCheckEmail.setTextColor(Color.BLUE);
                            tvCheckEmail.setText("사용 가능한 메일입니다.");
                            edtEmail.setTextColor(Color.GRAY);
                            edtEmail.setFocusable(false);
                        } else {
                            tvCheckEmail.setTextColor(Color.RED);
                            tvCheckEmail.setText("이미 존재하는 메일입니다.");
                        }
                        tvCheckEmail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnCheckNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                checkNicknameExist = false;
                mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            simpleUserDTO = item.getValue(SimpleUserDTO.class);
                            String strNickname = simpleUserDTO.getNickname();
                            if (userNickname.equals(strNickname)) {
                                checkNicknameExist = true;
                                break;
                            }
                        }
                        if (!checkNicknameExist) {
                            tvCheckNickname.setTextColor(Color.BLUE);
                            tvCheckNickname.setText("사용 가능한 별명입니다.");
                            edtNickname.setTextColor(Color.GRAY);
                            edtNickname.setFocusable(false);
                        } else {
                            tvCheckNickname.setTextColor(Color.RED);
                            tvCheckNickname.setText("이미 존재하는 별명입니다.");
                        }
                        tvCheckNickname.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdButton = findViewById(rdGroup.getCheckedRadioButtonId());
                if (!checkEmailExist && checkPassword(checkPasswordLength, checkSamePassword) && checkNickname(checkNicknameLength, checkNicknameExist) && (rdButton != null)) {
                    Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("nickname", userNickname);
                    intent.putExtra("who", rdButton.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterStep1Activity.this, "입력 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkNickname(boolean checkNicknameLength, boolean checkNicknameExist) {
        return checkNicknameLength && !checkNicknameExist;
    }

    public boolean checkPassword(boolean checkPasswordLength, boolean checkSamePassword) {
        return checkPasswordLength && checkSamePassword;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.fadeout);
    }
}