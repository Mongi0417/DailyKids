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

import java.time.LocalDate;
import java.util.ArrayList;

public class WriteNoticeActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private EditText edtTitle, edtContent;
    private CheckBox chkNotice;
    private Button btnPost;
    private DatabaseReference mDbRef;
    private Notice notice;
    private ArrayList<Notice> mList = new ArrayList<>();
    private String uid, nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_write_notice);

        setToolbar();
        initView();
        initData();
        setButtonToPostNotice();
    }

    private void setToolbar() {
        mView = findViewById(R.id.write_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("알림장 작성");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        edtTitle = findViewById(R.id.write_edtTitle);
        edtContent = findViewById(R.id.write_edtContent);
        chkNotice = findViewById(R.id.write_chkNotice);
        btnPost = findViewById(R.id.write_btnPost);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        nickname = getIntent().getStringExtra("nickname");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonToPostNotice() {
        btnPost.setOnClickListener(view -> {
            int notice = 0;
            LocalDate localDate = LocalDate.now();
            String year = String.valueOf(localDate.getYear());
            String month = String.format("%02d", localDate.getMonthValue());
            String date = String.format("%02d", localDate.getDayOfMonth());
            long timestamp = System.currentTimeMillis() / 1000;
            if (chkNotice.isChecked())
                notice = 1;
            String noticeKey = mDbRef.child("Notice").push().getKey();
            this.notice = new Notice(uid, nickname, edtTitle.getText().toString(), edtContent.getText().toString(), year + "년", month + "월", date  + "일", noticeKey, notice, -timestamp);
            mDbRef.child("Notice").child(noticeKey).setValue(this.notice);
            Toast.makeText(WriteNoticeActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout);
        });
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
