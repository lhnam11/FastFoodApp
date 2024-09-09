package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText txtFullName, txtusername, txtpassword, txtrepassword, txtphone, txtemail, txtimageDK, txtAdminOrUser;
    Button btnsignup, btnsignin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFullName = findViewById(R.id.userDKFullName);
        txtemail = findViewById(R.id.userDKEmail);
        txtphone = findViewById(R.id.userDKPhone);
        txtusername = findViewById(R.id.userDKName);
        txtpassword = findViewById(R.id.DKpassword);
        txtrepassword = findViewById(R.id.DKrepassword);
        txtimageDK = findViewById(R.id.ImagDK);
        txtAdminOrUser = findViewById(R.id.txtAdminOrUser);
        btnsignup = findViewById(R.id.btnsignup);
        btnsignin = findViewById(R.id.btnsignin);
        databaseHelper = new DatabaseHelper(this);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = txtFullName.getText().toString();
                String email = txtemail.getText().toString();
                String phone = txtphone.getText().toString();
                String user = txtusername.getText().toString();
                String pass = txtpassword.getText().toString();
                String repass = txtrepassword.getText().toString();
                String imgDK = txtimageDK.getText().toString();
                String adminOrUserText = txtAdminOrUser.getText().toString();
                int adminOrUser = adminOrUserText.isEmpty() ? -1 : Integer.parseInt(adminOrUserText);

                if (fullname.isEmpty() || email.isEmpty() || phone.isEmpty() || user.isEmpty() || pass.isEmpty() || repass.isEmpty() || imgDK.isEmpty() || adminOrUserText.isEmpty()) {
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = databaseHelper.checkusername(user);
                        if (!checkuser) {
                            Boolean insert = databaseHelper.insertData(fullname, email, phone, user, pass, imgDK, adminOrUser);
                            if (insert) {
                                Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                        clearPasswordFields();
                    }
                }
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private void clearFields() {
        txtFullName.setText("");
        txtemail.setText("");
        txtphone.setText("");
        txtusername.setText("");
        txtpassword.setText("");
        txtrepassword.setText("");
        txtimageDK.setText("");
        txtAdminOrUser.setText("");
    }

    private void clearPasswordFields() {
        txtpassword.setText("");
        txtrepassword.setText("");
    }

}