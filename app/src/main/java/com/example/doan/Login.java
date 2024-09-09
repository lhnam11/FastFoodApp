package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button btnLogin, btnRegister, btnForgotPass;
    private DatabaseHelper databaseHelper;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        btnLogin = findViewById(R.id.btnsignin1);
        btnRegister = findViewById(R.id.btnRe);
        btnForgotPass = findViewById(R.id.btnQuenPass);
        databaseHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int isAdmin = databaseHelper.checkIsAdmin(user, pass);
            if (isAdmin == -1) {
                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                String nameC = databaseHelper.getNameCByUsername(user);
                int customerid = databaseHelper.getCustomerIdByUsername(user);
                String imageResName = databaseHelper.getImageResNameByUsername(user);
                String phone=databaseHelper.getPhoneByUsername(user);
                String email=databaseHelper.getEmailByUsername(user);

                // Use SharedPreferences to store the customer_id
                getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        .edit()
                        .putInt("customer_id", customerid)
                        .apply();

                Intent intent = new Intent(Login.this, MainActivity2.class);
                if (isAdmin == 1) {
                    intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("nameC", nameC);
                    intent.putExtra("imageResName", imageResName);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("username", user);
                    intent.putExtra("password", pass);
                }
                intent.putExtra("customer_id", customerid);

                Toast.makeText(Login.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                clearFields();
            }
        });

        btnRegister.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Register.class)));
        btnForgotPass.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPass.class)));
    }

    private void clearFields() {
        username.setText("");
        password.setText("");
    }
}
