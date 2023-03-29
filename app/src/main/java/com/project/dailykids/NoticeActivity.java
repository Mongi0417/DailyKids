package com.project.dailykids;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private RecyclerView recyclerNotice;
    private ExtendedFloatingActionButton fabWrite;
    private String uid, nickname, who;
    private DatabaseReference mDbRef;
    private NoticeDTO noticeDTO;
    private NoticeAdapter noticeAdapter;
    private ArrayList<NoticeDTO> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.notice_layout);

        setToolbar();
        initView();
        initData();
        loadNotice();

        if (who.equals("교사")) {
            fabWrite.setVisibility(View.VISIBLE);
            setButtonToWriteNotice();
        }
    }

    private void setToolbar() {
        mView = findViewById(R.id.notice_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("알림장");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        recyclerNotice = findViewById(R.id.notice_recycler);
        fabWrite = findViewById(R.id.notice_fabWrite);
        recyclerNotice.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        nickname = getIntent().getStringExtra("nickname");
        who = getIntent().getStringExtra("who");
        mDbRef = FirebaseDatabase.getInstance().getReference();
        noticeAdapter = new NoticeAdapter(mList);
        recyclerNotice.setAdapter(noticeAdapter);
    }

    private void setButtonToWriteNotice() {
        fabWrite.setOnClickListener(view -> {
            Intent intent = new Intent(NoticeActivity.this, NoticeWriteActivity.class);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
        });
    }

    private void loadNotice() {
        mDbRef.child("Notice").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    noticeDTO = item.getValue(NoticeDTO.class);
                    mList.add(noticeDTO);
                }
                noticeAdapter.notifyItemRangeChanged(0, noticeAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
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