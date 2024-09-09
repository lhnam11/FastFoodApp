package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmPass extends AppCompatActivity {
    EditText txtEmail, txtNewPass, txtConfirmNewPass;
    Button btnUpdatePassword,btnQuayLaiLogin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass);

        txtEmail = findViewById(R.id.txtrefreshPass);
        txtNewPass = findViewById(R.id.txtConfirmRefreshPass);
        txtConfirmNewPass = findViewById(R.id.txtConfirmRefreshPass);
        btnUpdatePassword = findViewById(R.id.btnCheckEmail);
        btnQuayLaiLogin = findViewById(R.id.btnQuayLaiLogin);
        databaseHelper = new DatabaseHelper(this);

        // Xử lý quay lại login
        btnQuayLaiLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        // Xử lý cập nhật mật khẩu
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getIntent().getStringExtra("email");
                String newPass = txtNewPass.getText().toString().trim();
                String confirmNewPass = txtConfirmNewPass.getText().toString().trim();

                if (email.isEmpty() || newPass.isEmpty() || confirmNewPass.isEmpty()) {
                    Toast.makeText(ConfirmPass.this, "Tất cả các trường đều cần thiết.", Toast.LENGTH_SHORT).show();
                    clearFields();
                    return;
                }

                if (!newPass.equals(confirmNewPass)) {
                    Toast.makeText(ConfirmPass.this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (databaseHelper.updatePassword(email, newPass)) {
                    Toast.makeText(ConfirmPass.this, "Cập nhật mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConfirmPass.this, "Không thể cập nhật mật khẩu. Hãy thử lại.", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });
    }

    private void clearFields() {
        txtNewPass.setText("");
        txtConfirmNewPass.setText("");
    }

    private void clearPasswordFields() {
        txtConfirmNewPass.setText("");
    }
}