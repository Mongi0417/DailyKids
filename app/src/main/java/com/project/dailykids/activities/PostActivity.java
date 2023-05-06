package com.project.dailykids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.dailykids.R;
import com.project.dailykids.adapter.PostAdapter;
import com.project.dailykids.models.Post;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private RecyclerView postView;
    private Post post;
    private PostAdapter postAdapter;
    private ArrayList<Post> mList = new ArrayList<>();
    private ExtendedFloatingActionButton fabWrite;
    private DatabaseReference mDbRef;
    private String uid = "", nickname = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_post);

        setToolbar();
        initView();
        initData();
        setButtonToWritePost();
        setItemClickListener();
        loadPost();
    }

    private void setToolbar() {
        mView = findViewById(R.id.post_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("학부모 게시판");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        fabWrite = findViewById(R.id.post_fabWrite);
        postView = findViewById(R.id.post_recyclerView);
        postAdapter = new PostAdapter(mList);
        postView.setAdapter(postAdapter);
        postView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonToWritePost() {
        fabWrite.setOnClickListener(view -> {
            Intent intent = new Intent(PostActivity.this, WritePostActivity.class);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
        });
    }

    private void setItemClickListener() {
        postAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(PostActivity.this, PostContentActivity.class);
            intent.putExtra("nickname", nickname);
            intent.putExtra("writer", mList.get(position).getName());
            intent.putExtra("title", mList.get(position).getTitle());
            intent.putExtra("content", mList.get(position).getContent());
            intent.putExtra("postedDateAndTime", mList.get(position).postedDateAndTimeForComment());
            intent.putExtra("postKey", mList.get(position).getPostKey());
            startActivity(intent);
        });
    }

    private void loadPost() {
        mDbRef.child("Post").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    post = item.getValue(Post.class);
                    mList.add(post);
                }
                postAdapter.notifyItemRangeChanged(0, postAdapter.getItemCount());
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
