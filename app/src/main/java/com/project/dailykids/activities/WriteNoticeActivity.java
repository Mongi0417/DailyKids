package com.project.dailykids.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.project.dailykids.models.Notice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WriteNoticeActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private EditText edtTitle, edtContent;
    private CheckBox chkNotice;
    private Button btnWrite;
    private DatabaseReference mDbRef;
    private Notice notice;
    private String uid, nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_write_notice);

        setToolbar();
        initView();
        initData();
        setButtonToWriteNotice();
    }

    private void setToolbar() {
        mView = findViewById(R.id.write_notice_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("알림장 작성");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        edtTitle = findViewById(R.id.notice_edtTitle);
        edtContent = findViewById(R.id.notice_edtContent);
        chkNotice = findViewById(R.id.notice_chkNotice);
        btnWrite = findViewById(R.id.notice_btnWrite);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        nickname = getIntent().getStringExtra("nickname");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonToWriteNotice() {
        btnWrite.setOnClickListener(view -> {
            int isNotice = 0;
            long now = System.currentTimeMillis();
            long timestampForSorting = now / 1000;
            /*LocalDateTime localDateTime = LocalDateTime.now();
            String temp = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss.SSS"));
            String year = String.valueOf(localDateTime.getYear());
            String month = String.format("%02d", localDateTime.getMonthValue());
            String date = String.format("%02d", localDateTime.getDayOfMonth());*/
            if (chkNotice.isChecked())
                isNotice = 1;
            notice = new Notice(uid, nickname, edtTitle.getText().toString(), edtContent.getText().toString(), isNotice, now, -timestampForSorting);
            mDbRef.child("Notice").push().setValue(notice);
            Toast.makeText(WriteNoticeActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout);
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
