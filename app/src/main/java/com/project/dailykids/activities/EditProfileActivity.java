package com.project.dailykids.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.dailykids.R;
import com.project.dailykids.models.User;
import com.project.dailykids.models.UserSimple;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvCurrentPassword, tvNewPassword, tvNewNickname;
    private EditText edtCurrentPassword, edtNewPassword, edtRePassword, edtNewNickname;
    private Button btnChangePassword, btnChangeName;
    private boolean isLongPassword, isSamePassword, isProperLengthForNickname, isAvailableNickname;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_edit_profile);

        setToolbar();
        initView();
        initData();
        setClickListener();
        checkNewPasswordLength();
        checkReNewPassword();
        checkNicknameLength();
    }

    private void setToolbar() {
        mView = findViewById(R.id.edit_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("프로필 변경");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvCurrentPassword = findViewById(R.id.edit_tvCurrentPassword);
        tvNewPassword = findViewById(R.id.edit_tvNewPassword);
        tvNewNickname = findViewById(R.id.edit_tvNewNickname);
        edtCurrentPassword = findViewById(R.id.edit_edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edit_edtNewPassword);
        edtRePassword = findViewById(R.id.edit_edtRePassword);
        edtNewNickname = findViewById(R.id.edit_edtNewNickname);
        btnChangePassword = findViewById(R.id.edit_btnChangePassword);
        btnChangeName = findViewById(R.id.edit_btnChangeNickname);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
    }

    private void setClickListener() {
        btnChangePassword.setOnClickListener(this);
        btnChangeName.setOnClickListener(this);
    }

    private void checkNewPasswordLength() {
        edtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtNewPassword.getText().toString().length() < 6) {
                    isLongPassword = false;
                    tvNewPassword.setTextColor(Color.RED);
                    tvNewPassword.setText("비밀번호는 6자 이상이어야 합니다.");
                } else {
                    isLongPassword = true;
                    tvNewPassword.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkReNewPassword() {
        edtRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtRePassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                    isSamePassword = false;
                    tvNewPassword.setTextColor(Color.RED);
                    tvNewPassword.setText("새로운 비밀번호와 일치하지 않습니다.");
                } else {
                    isSamePassword = true;
                    tvNewPassword.setTextColor(Color.BLUE);
                    tvNewPassword.setText("새로운 비밀번호와 일치합니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkNicknameLength() {
        edtNewNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtNewNickname.getText().toString().length() > 8 || edtNewNickname.getText().toString().length() < 2) {
                    isProperLengthForNickname = false;
                    tvNewNickname.setTextColor(Color.RED);
                    tvNewNickname.setText("닉네임은 2-8자 이내로 정해주세요.");
                } else {
                    isProperLengthForNickname = true;
                    tvNewNickname.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btnChangePassword:
                checkAboutPassword(edtCurrentPassword.getText().toString(), edtNewPassword.getText().toString(), edtRePassword.getText().toString());
                break;
            case R.id.edit_btnChangeNickname:
                checkAboutNickname(edtNewNickname.getText().toString());
                break;
        }
    }

    private void checkAboutPassword(String currentPassword, String newPassword, String rePassword) {  // 현재 비밀번호 일치 여부 및 새로운 비밀번호 상태 확인
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(rePassword))
            Toast.makeText(this, "입력되지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
        else if (!(isLongPassword && isSamePassword))
            Toast.makeText(this, "입력하신 내용을 확인해 주세요.", Toast.LENGTH_SHORT).show();
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            changePassword(newPassword, user);
                        else {
                            tvCurrentPassword.setTextColor(Color.RED);
                            tvCurrentPassword.setText("현재 비밀번호가 일치하지 않습니다.");
                        }
                    }
                });
            }
        }
    }

    private void changePassword(String newPassword, FirebaseUser user) { // 비밀번호 변경
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "비밀번호가 재설정되었습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                finishAffinity();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAboutNickname(String newNickname) { // 닉네임 길이 및 변경 희망 닉네임 중복 여부 확인
        if (TextUtils.isEmpty(newNickname))
            Toast.makeText(this, "새로운 닉네임을 입력해 주세요,", Toast.LENGTH_SHORT).show();
        else if (!isProperLengthForNickname)
            Toast.makeText(this, "닉네임의 길이를 확인해 주세요.", Toast.LENGTH_SHORT).show();
        else {
            DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
            mDbRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isAvailableNickname = true;
                    for (DataSnapshot item : snapshot.getChildren()) {
                        UserSimple userSimple = item.getValue(UserSimple.class);
                        String name = userSimple.getNickname();
                        if (name.equals(newNickname)) {
                            isAvailableNickname = false;
                            tvNewNickname.setTextColor(Color.RED);
                            tvNewNickname.setText("이미 존재하는 이름입니다.");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            if (isAvailableNickname) {
                tvNewNickname.setText("");
                changeNickname(uid, newNickname, mDbRef);
            }
        }
    }

    private void changeNickname(String uid, String newNickname, DatabaseReference databaseReference) { // 닉네임 변경
        databaseReference.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String simpleDTOKey = user.getSimpleDTOKey();
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("nickname", newNickname);
                databaseReference.child("SimpleUserData").child(simpleDTOKey).updateChildren(userMap);
                databaseReference.child("UserData").child(uid).updateChildren(userMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        finishAffinity();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
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
