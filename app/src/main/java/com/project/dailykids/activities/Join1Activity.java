package com.project.dailykids.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.dailykids.R;
import com.project.dailykids.models.UserSimple;
import com.project.dailykids.utils.HideKeyboard;

import java.util.regex.Pattern;

public class Join1Activity extends AppCompatActivity implements View.OnClickListener {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvCheckEmail, tvPasswordLength, tvCheckPassword, tvCheckNickname;
    private EditText edtEmail, edtPassword, edtCheckPassword, edtNickname;
    private Button btnCheckEmail, btnCheckNickname, btnNext;
    private RadioGroup rdGroup;
    private RadioButton rdButton;
    private boolean isEmailAddressFormat, isAvailableEmailAddress, isLongPassword, isSamePassword, isProperLengthForNickname, isAvailableNickname;
    private DatabaseReference mDbRef;
    private UserSimple userSimple;
    private String userEmail = "", userPassword = "", userPasswordForCheck = "", userNickname = "";
    private HideKeyboard hk = new HideKeyboard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_join_step1);

        setToolbar();
        initView();
        initData();
        checkPasswordLength();
        checkReEnterPassword();
        checkNicknameLength();
        setClickListener();
    }

    private void setClickListener() {
        btnCheckEmail.setOnClickListener(this);
        btnCheckNickname.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void setToolbar() {
        mView = findViewById(R.id.join1_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvCheckEmail = findViewById(R.id.join1_tvCheckEmail);
        tvPasswordLength = findViewById(R.id.join1_tvPasswordLength);
        tvCheckPassword = findViewById(R.id.join1_tvCheckPassword);
        tvCheckNickname = findViewById(R.id.join1_tvCheckNickname);
        edtEmail = findViewById(R.id.join1_edtEmail);
        edtPassword = findViewById(R.id.join1_edtPassword);
        edtCheckPassword = findViewById(R.id.join1_edtCheckPassword);
        edtNickname = findViewById(R.id.join1_edtNickname);
        btnCheckEmail = findViewById(R.id.join1_btnCheckEmail);
        btnCheckNickname = findViewById(R.id.join1_btnCheckNickname);
        btnNext = findViewById(R.id.join1_btnNext);
        rdGroup = findViewById(R.id.join1_rdGroup);
    }

    private void initData() {
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void checkEmailAddressFormat() {
        userEmail = edtEmail.getText().toString();
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(userEmail).matches()) {
            isEmailAddressFormat = true;
            tvCheckEmail.setVisibility(View.GONE);
        } else {
            isEmailAddressFormat = false;
            tvCheckEmail.setTextColor(Color.RED);
            tvCheckEmail.setText("이메일 형식이 아닙니다.");
            tvCheckEmail.setVisibility(View.VISIBLE);
        }
    }

    private void checkDuplicatedEmailAddress() {
        isAvailableEmailAddress = true;
        mDbRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    userSimple = item.getValue(UserSimple.class);
                    String strEmail = userSimple.getEmail();
                    if (userEmail.equals(strEmail)) {
                        isAvailableEmailAddress = false;
                        break;
                    }
                }
                if (isAvailableEmailAddress) {
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

    private void checkPasswordLength() {
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userPassword = edtPassword.getText().toString();
                if (userPassword.length() < 6) {
                    tvPasswordLength.setVisibility(View.VISIBLE);
                    isLongPassword = false;
                } else {
                    tvPasswordLength.setVisibility(View.GONE);
                    isLongPassword = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkReEnterPassword() {
        edtCheckPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCheckPassword.setVisibility(View.VISIBLE);
                userPasswordForCheck = edtCheckPassword.getText().toString();
                if (userPassword.equals(userPasswordForCheck)) {
                    isSamePassword = true;
                    tvCheckPassword.setTextColor(Color.BLUE);
                    tvCheckPassword.setText("비밀번호와 일치합니다.");
                } else {
                    isSamePassword = false;
                    tvCheckPassword.setTextColor(Color.RED);
                    tvCheckPassword.setText("비밀번호와 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkNicknameLength() {
        edtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userNickname = edtNickname.getText().toString();
                if (userNickname.length() < 2 || userNickname.length() > 8) {
                    isProperLengthForNickname = false;
                    tvCheckNickname.setTextColor(Color.RED);
                    tvCheckNickname.setText("2-8자 이내로 설정해주세요.");
                    tvCheckNickname.setVisibility(View.VISIBLE);
                } else {
                    isProperLengthForNickname = true;
                    tvCheckNickname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkDuplicatedNickname() {
        isAvailableNickname = true;
        mDbRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    userSimple = item.getValue(UserSimple.class);
                    String strNickname = userSimple.getNickname();
                    if (userNickname.equals(strNickname)) {
                        isAvailableNickname = false;
                        break;
                    }
                }
                if (!isAvailableNickname) {
                    tvCheckNickname.setTextColor(Color.RED);
                    tvCheckNickname.setText("이미 존재하는 닉네임입니다.");
                } else {
                    tvCheckNickname.setTextColor(Color.BLUE);
                    tvCheckNickname.setText("사용 가능한 닉네임입니다.");
                }
                tvCheckNickname.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void moveToSecondJoin() {
        rdButton = findViewById(rdGroup.getCheckedRadioButtonId());
        if (isAvailableEmailAddress && isLongPassword && isSamePassword && isAvailableNickname && (rdButton != null)) {
            Intent intent = new Intent(Join1Activity.this, Join2Activity.class);
            intent.putExtra("email", userEmail);
            intent.putExtra("password", userPassword);
            intent.putExtra("nickname", userNickname);
            intent.putExtra("who", rdButton.getText().toString());
            startActivity(intent);
        } else
            Toast.makeText(Join1Activity.this, "입력 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.join1_btnCheckEmail:
                hk.hideKeyboard();
                checkEmailAddressFormat();
                if (isEmailAddressFormat)
                    checkDuplicatedEmailAddress();
                break;
            case R.id.join1_btnCheckNickname:
                hk.hideKeyboard();
                checkNicknameLength();
                if (isProperLengthForNickname)
                    checkDuplicatedNickname();
                break;
            case R.id.join1_btnNext:
                moveToSecondJoin();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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