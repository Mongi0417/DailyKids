package com.project.dailykids;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvCheckPassword;
    private EditText edtCheckPassword;
    private Button btnCheckPassword, btnCancelDelete, btnDelete;
    private String uid, simpleDTOKey;
    private FirebaseUser user;
    private DatabaseReference mDbRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.delete_account_layout);

        setToolbar();
        initView();
        initData();
        setClickListener();
    }

    private void setToolbar() {
        mView = findViewById(R.id.profile_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("프로필");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvCheckPassword = findViewById(R.id.delete_tvCheckPassword);
        edtCheckPassword = findViewById(R.id.delete_edtCheckPassword);
        btnCheckPassword = findViewById(R.id.delete_btnCheckPassword);
        btnCancelDelete = findViewById(R.id.delete_btnCancelDelete);
        btnDelete = findViewById(R.id.delete_btnDelete);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDbRef.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO userDTO = snapshot.getValue(UserDTO.class);
                simpleDTOKey = userDTO.getSimpleDTOKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setClickListener() {
        btnCheckPassword.setOnClickListener(this);
        btnCancelDelete.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_btnCheckPassword:
                checkPassword(edtCheckPassword.getText().toString());
                break;
            case R.id.delete_btnCancelDelete:
                finish();
                break;
            case R.id.delete_btnDelete:
                floatDeleteDialog();
                break;
        }
    }

    private void checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            tvCheckPassword.setTextColor(Color.RED);
            tvCheckPassword.setText("비밀번호를 입력해 주세요,");
        } else {
            if (user != null) {
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), password);
                user.reauthenticate(authCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvCheckPassword.setTextColor(Color.BLUE);
                        tvCheckPassword.setText("현재 비밀번호와 일치합니다.");
                        edtCheckPassword.setFocusable(false);
                        btnCheckPassword.setClickable(false);
                        btnCheckPassword.setBackgroundResource(R.drawable.btn_yellow_light);
                        btnDelete.setBackgroundResource(R.drawable.btn_yellow);
                        btnDelete.setClickable(true);
                        HideKeyboard hk = new HideKeyboard();
                        hk.hideKeyboard();
                    } else {
                        tvCheckPassword.setTextColor(Color.RED);
                        tvCheckPassword.setText("현재 비밀번호와 일치하지 않습니다.");
                    }
                });
            }
        }
    }

    private void floatDeleteDialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(DeleteAccountActivity.this);
        dlg.setTitle("정말로 탈퇴하시겠습니까?");
        dlg.setPositiveButton("회원 탈퇴", (dialogInterface, i) -> deleteAccount());
        dlg.setNegativeButton("닫기", (dialogInterface, i) -> dialogInterface.dismiss());
        dlg.show();
    }

    private void deleteAccount() {
        if (user != null) {
            mDbRef.child("SimpleUserData").child(simpleDTOKey).removeValue().addOnCompleteListener(task -> {
               if (task.isSuccessful())
                   Log.d("TAG", "계정 삭제 완료");
            });
            mDbRef.child("UserData").child(uid).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d("TAG", "계정 삭제 완료");
            });
            mStorageRef.child("profile_img/").child(uid + ".jpg").delete().addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d("TAG", "프로필 사진 삭제 완료");
            });
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Auth 계정 삭제 완료");
                    FirebaseAuth.getInstance().signOut();
                    finishAffinity();
                    Intent intent = new Intent(DeleteAccountActivity.this, LoginActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
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
