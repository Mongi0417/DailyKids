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
import com.project.dailykids.adapter.ChatAdapter;
import com.project.dailykids.models.Chat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private ArrayList<Chat> chatList = new ArrayList<>();
    private EditText edtMessage;
    private Button btnSend;
    private RecyclerView chatView;
    private Chat chatDTO;
    private ChatAdapter chatAdapter;
    private DatabaseReference mRef;
    private String uid, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_chat);

        setToolbar();
        initView();
        initData();
        loadChatData();
        setButtonToSendMessage();
    }

    private void setToolbar() {
        mView = findViewById(R.id.chat_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("키즈톡");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        edtMessage = findViewById(R.id.chat_edtMessage);
        btnSend = findViewById(R.id.chat_btnSend);
        chatView = findViewById(R.id.chat_chatView);
    }

    private void initData() {
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        uid = FirebaseAuth.getInstance().getUid();
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void loadChatData() {
        mRef.child("Chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatDTO = snapshot.getValue(Chat.class);
                chatList.add(chatDTO);
                chatAdapter.notifyDataSetChanged();
                chatView.scrollToPosition(chatList.size() - 1);
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
        chatAdapter = new ChatAdapter(chatList, uid);
        chatView.setAdapter(chatAdapter);
        chatView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setButtonToSendMessage() {
        btnSend.setOnClickListener(view -> {
            LocalDateTime localDateTime = LocalDateTime.now();
            String timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
            chatDTO = new Chat(uid, nickname, edtMessage.getText().toString(), timestamp);
            mRef.child("Chat").push().setValue(chatDTO);
            edtMessage.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
