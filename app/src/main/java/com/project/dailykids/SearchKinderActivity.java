package com.project.dailykids;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchKinderActivity extends AppCompatActivity implements GetJsonObject, View.OnClickListener {
    private static final String SCHOOL_URL = "https://e-childschoolinfo.moe.go.kr/api/notice/basicInfo2.do?";
    private static final String API_KEY = "key=" + BuildConfig.KINDER_API_KEY;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvSido, tvSgg;
    private LinearLayout selectSido, selectSgg, typeLinear;
    private Button btnSearch, btnSearchByName;
    private EditText edtSearchKinder;
    private RecyclerView recyclerKinder;
    private JSONObject obj;
    private SearchKinderDTO searchKinderDTO;
    private SearchKinderAdapter searchKinderAdapter;
    private ArrayList<SearchKinderDTO> mList = new ArrayList<SearchKinderDTO>();
    private ArrayList<SearchKinderDTO> mSearchList = new ArrayList<SearchKinderDTO>();
    private DatabaseReference mDbRef;
    private String sidoCode;
    private String sggCode;
    private String kinderName, kinderAddress, kinderTel;
    private String[] arraySido, arraySgg;
    private String[] arraySidoCode, arraySggCode;
    private String uid = "", nickname = "", who = "";
    private AlertDialog.Builder dlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.search_kinder);

        setToolbar();
        initView();
        initData();
        setClickListener();
    }

    private void setToolbar() {
        mView = findViewById(R.id.join3_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("유치원 검색");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvSido = findViewById(R.id.search_tvSido);
        tvSgg = findViewById(R.id.search_tvSgg);
        selectSido = findViewById(R.id.search_selectSido);
        selectSgg = findViewById(R.id.search_selectSgg);
        typeLinear = findViewById(R.id.select_typeLinear);
        btnSearch = findViewById(R.id.search_btnSearch);
        btnSearchByName = findViewById(R.id.search_btnSearchByName);
        edtSearchKinder = findViewById(R.id.search_edtSearchKinder);
        recyclerKinder = findViewById(R.id.search_recyclerKinder);
        recyclerKinder.addItemDecoration(new DividerItemDecoration(recyclerKinder.getContext(), 1));
        recyclerKinder.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        who = intent.getStringExtra("who");
        uid = FirebaseAuth.getInstance().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        arraySido = getResources().getStringArray(R.array.array_sido);
        arraySidoCode = getResources().getStringArray(R.array.array_sidoCode);
        dlg = new AlertDialog.Builder(this);
    }

    private void setClickListener() {
        selectSido.setOnClickListener(this);
        selectSgg.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnSearchByName.setOnClickListener(this);
    }

    private void setWindowToSelectSido() {
        dlg.setTitle("시/도 선택");
        dlg.setItems(arraySido, (dialogInterface, i) -> {
            tvSido.setText(arraySido[i]);
            tvSgg.setText("지역 선택");
            switch (i) {
                case 0:
                    arraySgg = getResources().getStringArray(R.array.array_seoul);
                    arraySggCode = getResources().getStringArray(R.array.array_seoul_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 1:
                    arraySgg = getResources().getStringArray(R.array.array_busan);
                    arraySggCode = getResources().getStringArray(R.array.array_busan_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 2:
                    arraySgg = getResources().getStringArray(R.array.array_daegu);
                    arraySggCode = getResources().getStringArray(R.array.array_daegu_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 3:
                    arraySgg = getResources().getStringArray(R.array.array_incheon);
                    arraySggCode = getResources().getStringArray(R.array.array_incheon_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 4:
                    arraySgg = getResources().getStringArray(R.array.array_gwangju);
                    arraySggCode = getResources().getStringArray(R.array.array_gwangju_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 5:
                    arraySgg = getResources().getStringArray(R.array.array_daejeon);
                    arraySggCode = getResources().getStringArray(R.array.array_daejeon_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 6:
                    arraySgg = getResources().getStringArray(R.array.array_ulsan);
                    arraySggCode = getResources().getStringArray(R.array.array_ulsan_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 7:
                    arraySgg = getResources().getStringArray(R.array.array_sejong);
                    arraySggCode = getResources().getStringArray(R.array.array_sejong_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 8:
                    arraySgg = getResources().getStringArray(R.array.array_gyeonggi);
                    arraySggCode = getResources().getStringArray(R.array.array_gyeonggi_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 9:
                    arraySgg = getResources().getStringArray(R.array.array_gangwon);
                    arraySggCode = getResources().getStringArray(R.array.array_gangwon_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 10:
                    arraySgg = getResources().getStringArray(R.array.array_chungbuk);
                    arraySggCode = getResources().getStringArray(R.array.array_chungbuk_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 11:
                    arraySgg = getResources().getStringArray(R.array.array_chungnam);
                    arraySggCode = getResources().getStringArray(R.array.array_chungnam_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 12:
                    arraySgg = getResources().getStringArray(R.array.array_jeonbuk);
                    arraySggCode = getResources().getStringArray(R.array.array_jeonbuk_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 13:
                    arraySgg = getResources().getStringArray(R.array.array_jeonnam);
                    arraySggCode = getResources().getStringArray(R.array.array_jeonnam_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 14:
                    arraySgg = getResources().getStringArray(R.array.array_gyeongbuk);
                    arraySggCode = getResources().getStringArray(R.array.array_gyeongbuk_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 15:
                    arraySgg = getResources().getStringArray(R.array.array_gyeongnam);
                    arraySggCode = getResources().getStringArray(R.array.array_gyeongnam_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
                case 16:
                    arraySgg = getResources().getStringArray(R.array.array_jeju);
                    arraySggCode = getResources().getStringArray(R.array.array_jeju_code);
                    sidoCode = "&sidoCode=" + arraySido[i];
                    break;
            }
        });
        dlg.show();
    }

    private void setWindowToSelectSgg() {
        dlg.setTitle("지역 선택");
        dlg.setItems(arraySgg, (dialogInterface, i) -> {
           tvSgg.setText(arraySgg[i]);
           sggCode = "&sggCode=" + arraySggCode[i];
        });
        dlg.show();
    }

    private void setButtonToSearchKinder() {

    }

    private void setButtonToSearchKinderByName() {

    }

    @Override
    public JSONObject getJsonObject() {
        return obj;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_selectSido:
                setWindowToSelectSido();
                break;
            case R.id.search_selectSgg:
                setWindowToSelectSgg();
                break;
            case R.id.search_btnSearch:
                setButtonToSearchKinder();
                break;
            case R.id.search_btnSearchByName:
                setButtonToSearchKinderByName();
                break;
        }
    }
}
