package com.project.dailykids.activities;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.project.dailykids.BuildConfig;
import com.project.dailykids.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class KinderInformationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvKinderName, tvKinderAddress, tvKinderTel, tvKinderClCnt, tvKinderFpCnt;
    private String establish, addr, telno, clcnt3, clcnt4, clcnt5, mixclcnt, ag3fpcnt, ag4fpcnt, ag5fpcnt, mixfpcnt, ppcnt3, ppcnt4, ppcnt5, mixppcnt;
    private String uid, nickname, who, kinderName;
    private Button btnApply;
    private JSONObject obj;
    private JSONArray kinderArray;
    private FusedLocationSource locationSource;
    private String latitude, longitude;
    private Double doubleLatitude, doubleLongitude;
    private Marker marker;
    private MyThread myThread;
    private NaverMap naverMap;
    //private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_kinder_information);

        setToolbar();
        initView();
        initData();
        loadKinderInformation();
        setMapFragment();
    }

    private void setToolbar() {
        mView = findViewById(R.id.information_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("유치원 정보");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvKinderName = findViewById(R.id.information_tvKinderName);
        tvKinderAddress = findViewById(R.id.information_tvKinderAddress);
        tvKinderTel = findViewById(R.id.information_tvKinderTel);
        tvKinderClCnt = findViewById(R.id.information_tvKinderClCnt);
        tvKinderFpCnt = findViewById(R.id.information_tvKinderFpCnt);
        btnApply = findViewById(R.id.information_btnApply);
    }

    private void initData() {
        nickname = getIntent().getStringExtra("nickname");
        who = getIntent().getStringExtra("who");
        uid = FirebaseAuth.getInstance().getUid();
        kinderName = getIntent().getStringExtra("kinderName");
        try {
            obj = new JSONObject(getIntent().getStringExtra("kinderInfo"));
            kinderArray = obj.getJSONArray("kinderInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadKinderInformation() {
        for (int i = 0; i < kinderArray.length(); i++) {
            try {
                JSONObject kinderObj = kinderArray.getJSONObject(i);
                if (kinderObj.getString("kindername").equals(kinderName)) {
                    establish = kinderObj.isNull("establish") ? "-" : kinderObj.getString("establish");
                    addr = kinderObj.isNull("addr") ? "-" : kinderObj.getString("addr");
                    telno = kinderObj.isNull("telno") ? "-" : kinderObj.getString("telno");
                    clcnt3 = kinderObj.isNull("clcnt3") ? "0" : kinderObj.getString("clcnt3");
                    clcnt4 = kinderObj.isNull("clcnt4") ? "0" : kinderObj.getString("clcnt4");
                    clcnt5 = kinderObj.isNull("clcnt5") ? "0" : kinderObj.getString("clcnt5");
                    mixclcnt = kinderObj.isNull("mixclcnt") ? "0" : kinderObj.getString("mixclcnt");
                    ag3fpcnt = kinderObj.isNull("ag3fpcnt") ? "0" : kinderObj.getString("ag3fpcnt");
                    ag4fpcnt = kinderObj.isNull("ag4fpcnt") ? "0" : kinderObj.getString("ag4fpcnt");
                    ag5fpcnt = kinderObj.isNull("ag5fpcnt") ? "0" : kinderObj.getString("ag5fpcnt");
                    mixfpcnt = kinderObj.isNull("mixfpcnt") ? "0" : kinderObj.getString("mixfpcnt");
                    ppcnt3 = kinderObj.isNull("ppcnt3") ? "0" : kinderObj.getString("ppcnt3");
                    ppcnt4 = kinderObj.isNull("ppcnt4") ? "0" : kinderObj.getString("ppcnt4");
                    ppcnt5 = kinderObj.isNull("ppcnt5") ? "0" : kinderObj.getString("ppcnt5");
                    mixppcnt = kinderObj.isNull("mixppcnt") ? "0" : kinderObj.getString("mixppcnt");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ag3fpcnt = String.valueOf(Integer.parseInt(ag3fpcnt) - Integer.parseInt(ppcnt3));
        if (Integer.parseInt(ag3fpcnt) < 0)
            ag3fpcnt = "0";
        ag4fpcnt = String.valueOf(Integer.parseInt(ag4fpcnt) - Integer.parseInt(ppcnt4));
        if (Integer.parseInt(ag4fpcnt) < 0)
            ag4fpcnt = "0";
        ag5fpcnt = String.valueOf(Integer.parseInt(ag5fpcnt) - Integer.parseInt(ppcnt5));
        if (Integer.parseInt(ag5fpcnt) < 0)
            ag5fpcnt = "0";
        mixfpcnt = String.valueOf(Integer.parseInt(mixfpcnt) - Integer.parseInt(mixppcnt));
        if (Integer.parseInt(mixfpcnt) < 0)
            mixfpcnt = "0";
        setTextKinderInformation();
    }

    private void setTextKinderInformation() {
        tvKinderName.setText(kinderName + "(" + establish + ")");
        tvKinderAddress.setText(addr);
        tvKinderTel.setText(telno);
        tvKinderClCnt.setText("3세반 " + clcnt3 + "개 | " + "4세반 " + clcnt4 + "개 | " + "5세반 " + clcnt5 + "개\n" + "혼합반 " + mixclcnt + "개");
        tvKinderFpCnt.setText("3세반 " + ag3fpcnt + "명 | " + "4세반 " + ag4fpcnt + "명 | " + "5세반 " + ag5fpcnt + "명\n" + "혼합반 " + mixfpcnt + "명");
    }

    private void setMapFragment() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.information_map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.information_map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        myThread = new MyThread();
        myThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated())
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            else
                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
            return;
        }
    }

    private void requestGeocode() {
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(addr, "UTF-8");
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_API_KEY_ID);
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_API_KEY);
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200)
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                else
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                String line = null;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line + "\n");

                int indexFirst, indexLast;

                indexFirst = stringBuilder.indexOf("\"x\":\"");
                indexLast = stringBuilder.indexOf("\",\"y\":");
                longitude = stringBuilder.substring(indexFirst + 5, indexLast);

                indexFirst = stringBuilder.indexOf("\"y\":\"");
                indexLast = stringBuilder.indexOf("\",\"distance\":");
                latitude = stringBuilder.substring(indexFirst + 5, indexLast);

                bufferedReader.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class MyThread extends Thread {
        @Override
        public void run() {
            requestGeocode();
            doubleLatitude = Double.parseDouble(latitude);
            doubleLongitude = Double.parseDouble(longitude);
            runOnUiThread(() -> {
                if (naverMap != null) {
                    marker = new Marker();
                    marker.setPosition(new LatLng(doubleLatitude, doubleLongitude));
                    marker.setMap(naverMap);
                    CameraUpdate cameraupdate = CameraUpdate.scrollAndZoomTo(new LatLng(doubleLatitude, doubleLongitude), 16);
                    naverMap.moveCamera(cameraupdate);
                }
            });
        }
    }
}
