package com.project.dailykids.activities;

import android.content.Context;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.dailykids.R;
import com.project.dailykids.models.Post;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WritePostActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private EditText edtTitle, edtContent;
    private Button btnWrite;
    private Post post;
    private String uid = "", nickname = "";
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_write_post);

        setToolbar();
        initView();
        initData();
        setButtonToWritePost();
    }

    private void setToolbar() {
        mView = findViewById(R.id.write_post_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("학부모 게시판");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        edtTitle = findViewById(R.id.post_edtTitle);
        edtContent = findViewById(R.id.post_edtContent);
        btnWrite = findViewById(R.id.post_btnWrite);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        nickname = getIntent().getStringExtra("nickname");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonToWritePost() {
        btnWrite.setOnClickListener(view -> {
            long now = System.currentTimeMillis();
            long timestampForSorting = now / 1000;
            /*LocalDateTime localDateTime = LocalDateTime.now();
            String sDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
            String sTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));*/
            String postKey = mDbRef.child("Post").push().getKey();
            post = new Post(uid, nickname, edtTitle.getText().toString(), edtContent.getText().toString(), now, -timestampForSorting, postKey);
            mDbRef.child("Post").child(postKey).setValue(post);
            Toast.makeText(WritePostActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
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
