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
import com.project.dailykids.adapter.BoardAdapter;
import com.project.dailykids.models.Board;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private RecyclerView boardView;
    private Board board;
    private BoardAdapter boardAdapter;
    private ArrayList<Board> mList = new ArrayList<>();
    private ExtendedFloatingActionButton fabWrite;
    private DatabaseReference mDbRef;
    private String uid = "", nickname = "", who = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_board);

        setToolbar();
        initView();
        initData();
        setButtonToWriteBoard();
        setItemClickListener();
        loadBoard();
    }

    private void setToolbar() {
        mView = findViewById(R.id.home_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("학부모 게시판");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        fabWrite = findViewById(R.id.board_fabWrite);
        boardView = findViewById(R.id.board_recyclerView);
        boardAdapter = new BoardAdapter(mList);
        boardView.setAdapter(boardAdapter);
        boardView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        nickname = getIntent().getStringExtra("nickname");
        who = getIntent().getStringExtra("who");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setButtonToWriteBoard() {
        fabWrite.setOnClickListener(view -> {
            Intent intent = new Intent(BoardActivity.this, WriteBoardActivity.class);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
        });
    }

    private void setItemClickListener() {
        boardAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(BoardActivity.this, BoardContentActivity.class);
        });
    }

    private void loadBoard() {
        mDbRef.child("Board").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    board = item.getValue(Board.class);
                    mList.add(board);
                }
                boardAdapter.notifyItemRangeChanged(0, boardAdapter.getItemCount());
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
