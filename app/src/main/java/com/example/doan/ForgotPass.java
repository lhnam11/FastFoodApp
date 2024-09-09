package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPass extends AppCompatActivity {

    EditText txtEmail;
    Button btnCheckEmail;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        txtEmail = findViewById(R.id.txtLayGmail);
        btnCheckEmail = findViewById(R.id.btnCheckEmail);
        databaseHelper = new DatabaseHelper(this);

        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPass.this, "Vui lòng nhập email của bạn.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = databaseHelper.getUserByEmail(email);

                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex("username");
                    if (columnIndex != -1 && cursor.moveToFirst()) {
                        String username = cursor.getString(columnIndex);
                        cursor.close();

                        Intent intent = new Intent(ForgotPass.this, ConfirmPass.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        cursor.close();
                        Toast.makeText(ForgotPass.this, "Email không tồn tại.", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                } else {
                    Toast.makeText(ForgotPass.this, "Lỗi truy vấn cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });
    }

    private void clearFields() {
        txtEmail.setText("");
    }
}