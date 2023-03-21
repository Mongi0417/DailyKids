package com.project.dailykids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.dailykids.ChatAdapter;
import com.project.dailykids.R;
import com.project.dailykids.ChatDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
    private EditText edtMessage;
    private Button btnSend;
    private RecyclerView chatView;
    private ChatDTO chatDTO;
    private ChatAdapter chatAdapter;
    private DatabaseReference mRef;
    private String uid, nickname, message, timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.chat_layout);
        // 파이어베이스 관련
        uid = FirebaseAuth.getInstance().getUid();
        mRef = FirebaseDatabase.getInstance().getReference();
        // 데이터 가져오기
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        // 위젯 연결
        edtMessage = findViewById(R.id.chat_edtMessage);
        btnSend = findViewById(R.id.chat_btnSend);
        chatView = findViewById(R.id.chat_chatView);
        // 메세지 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
                chatDTO = new ChatDTO(uid, nickname, edtMessage.getText().toString(), timestamp);
            }
        });
    }
}
