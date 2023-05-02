package com.project.dailykids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.dailykids.R;
import com.project.dailykids.adapter.BoardCommentAdapter;
import com.project.dailykids.models.Board;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardContentActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvWriter, tvDate, tvTitle, tvContent;
    private RecyclerView boardCommentView;
    private EditText edtComment;
    private Button btnSendComment;
    private Board board;
    private BoardCommentAdapter boardCommentAdapter;
    private ArrayList<Board> mList = new ArrayList<>();
    private String uid = "", nickname = "", writer = "", date = "", title = "", content = "", time = "", boardKey = "";
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_board_content);
        overridePendingTransition(R.anim.fadein, R.anim.none);

        setToolbar();
        initView();
        initData();
        setContent();
    }

    private void setToolbar() {
        mView = findViewById(R.id.search_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("학부모 게시판");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvWriter = findViewById(R.id.board_content_tvWriter);
        tvDate = findViewById(R.id.board_content_tvDate);
        tvTitle = findViewById(R.id.board_content_tvTitle);
        tvContent = findViewById(R.id.board_content_tvContent);
        boardCommentView = findViewById(R.id.board_content_recyclerView);
        edtComment = findViewById(R.id.board_content_edtComment);
        btnSendComment = findViewById(R.id.board_content_btnSendComment);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        writer = intent.getStringExtra("writer");
        date = intent.getStringExtra("date");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        time = intent.getStringExtra("time");
        boardKey = intent.getStringExtra("boardKey");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setContent() {
        tvWriter.setText(writer);
        tvDate.setText(date + " " + time);
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    private void setButtonToSendComment() {
        btnSendComment.setOnClickListener(view -> {
            Date date = new Date(System.currentTimeMillis());
            String sDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(date);
            String sTime = new SimpleDateFormat("HH:mm").format(date);
            String commentKey = mDbRef.child("board-comments").child(boardKey).push().getKey();
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
