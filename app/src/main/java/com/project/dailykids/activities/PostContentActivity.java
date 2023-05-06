package com.project.dailykids.activities;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.dailykids.R;
import com.project.dailykids.adapter.PostCommentAdapter;
import com.project.dailykids.models.Post;

import java.util.ArrayList;

public class PostContentActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvWriter, tvDate, tvTitle, tvContent;
    private RecyclerView postCommentView;
    private EditText edtComment;
    private Button btnSendComment;
    private Post post;
    private PostCommentAdapter postCommentAdapter;
    private ArrayList<Post> mList = new ArrayList<>();
    private String uid = "", nickname = "", writer = "", title = "", content = "", postedDateAndTime = "", postKey = "";
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_content);
        overridePendingTransition(R.anim.fadein, R.anim.none);

        setToolbar();
        initView();
        initData();
        setContent();
        setButtonToWriteComment();
        loadComment();
    }

    private void setToolbar() {
        mView = findViewById(R.id.post_content_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("학부모 게시판");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvWriter = findViewById(R.id.post_content_tvWriter);
        tvDate = findViewById(R.id.post_content_tvDate);
        tvTitle = findViewById(R.id.post_content_tvTitle);
        tvContent = findViewById(R.id.post_content_tvContent);
        postCommentView = findViewById(R.id.post_content_recyclerView);
        postCommentAdapter = new PostCommentAdapter(mList);
        postCommentView.setAdapter(postCommentAdapter);
        postCommentView.setLayoutManager(new LinearLayoutManager(this));
        edtComment = findViewById(R.id.post_content_edtComment);
        btnSendComment = findViewById(R.id.post_content_btnSendComment);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        writer = intent.getStringExtra("writer");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        postedDateAndTime = intent.getStringExtra("postedDateAndTime");
        postKey = intent.getStringExtra("postKey");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setContent() {
        tvWriter.setText(writer);
        tvDate.setText(postedDateAndTime);
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    private void setButtonToWriteComment() {
        btnSendComment.setOnClickListener(view -> {
            long now = System.currentTimeMillis();
            String commentKey = mDbRef.child("Post-Comments").child(postKey).push().getKey();
            post = new Post(uid, nickname, edtComment.getText().toString(), now, postKey, commentKey);
            mDbRef.child("Post-Comments").child(postKey).child(commentKey).setValue(post);
            edtComment.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            Toast.makeText(PostContentActivity.this, "댓글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadComment() {
        mDbRef.child("Post-Comments").child(postKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                post = snapshot.getValue(Post.class);
                mList.add(post);
                postCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
