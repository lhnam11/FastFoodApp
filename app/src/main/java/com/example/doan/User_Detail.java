package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class User_Detail extends AppCompatActivity {
    TextView tvfullname, tvusername, tvemail, tvphone, tvpass;
    Button btnBack;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        addControls();
        addData();
        addEvents();
    }

    void addControls() {
        tvfullname = findViewById(R.id.tvFullNameDetail);
        tvemail = findViewById(R.id.tvEmailDetail);
        tvphone = findViewById(R.id.tvPhoneDetail);
        tvusername = findViewById(R.id.tvUsernameDetail);
        tvpass = findViewById(R.id.tvPassWordDetail);
        btnBack = findViewById(R.id.btnBack);
        imageView = findViewById(R.id.imageHinhAnhDetail);
    }

    void addData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String fullname = bundle.getString("nameC");
            String email = bundle.getString("email");
            String phone = bundle.getString("phone");
            String username = bundle.getString("username");
            String pass = bundle.getString(("password"));
            String hinhanh = bundle.getString("img_User");

            // Hiển thị thông tin lên các TextView và ImageView tương ứng
            tvfullname.setText(fullname);
            tvemail.setText(email);
            tvphone.setText(phone);
            tvusername.setText(username);
            tvpass.setText(pass);

            int resID = getDrawableResIdByName(hinhanh);
            if (resID != 0) {
                imageView.setImageResource(resID);
            }
        }
    }

    void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public int getDrawableResIdByName(String resName) {
        String pkgName = getPackageName();
        return getResources().getIdentifier(resName, "drawable", pkgName);
    }
}