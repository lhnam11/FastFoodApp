package com.example.doan;

import static android.app.PendingIntent.getActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerAD extends AppCompatActivity {
    private ArrayList<User> users;
    private UserAdapter userAdapter;
    private EditText editTextUserId, editTextNameC, editTextEmail, editTextPhone, editTextUsername, editTextPassword, editTextImage, editTextIsAdmin;
    private Button buttonAddUser, buttonEditUser, buttonDeleteUser, btnBack ;
    private ListView listViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ad);

        // Initialize views
        editTextUserId = findViewById(R.id.editTextUserId);
        editTextNameC = findViewById(R.id.editTextNameC);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextImage = findViewById(R.id.editTextImage);
        editTextIsAdmin = findViewById(R.id.editTextIsAdmin);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        buttonEditUser = findViewById(R.id.buttonEditUser);
        buttonDeleteUser = findViewById(R.id.buttonDeleteUser);
        listViewUsers = findViewById(R.id.listViewUsers);
        btnBack = findViewById(R.id.buttonBack);

        users = new ArrayList<>();
        userAdapter = new UserAdapter(this, users);
        listViewUsers.setAdapter(userAdapter);

        loadUsersFromDatabase();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại khi nút Back được nhấn
            }
        });
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUser();
            }
        });

        buttonEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });

        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idToDelete = editTextUserId.getText().toString();
                deleteUserFromDatabase(idToDelete);
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);
                loadUserDataToEditTexts(selectedUser);
            }
        });

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);
                Intent intent = new Intent(CustomerAD.this, User_Detail.class);
                intent.putExtra("nameC", selectedUser.getNameC());
                intent.putExtra("email", selectedUser.getEmail());
                intent.putExtra("phone", selectedUser.getPhone());
                intent.putExtra("username", selectedUser.getUsername());
                intent.putExtra("password", selectedUser.getPassword());
                intent.putExtra("img_User", selectedUser.getImgUser());
                startActivity(intent);
                return true;
            }
        });
    }

    private void loadUsersFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM Users WHERE is_admin = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            users.clear();
            do {
                int customerId = cursor.getInt(0);
                String nameC = cursor.getString(1);
                String email = cursor.getString(2);
                String phone = cursor.getString(3);
                String username = cursor.getString(4);
                String password = cursor.getString(5);
                String imgUser = cursor.getString(6);
                int isAdmin = cursor.getInt(7);
                User user = new User(customerId, nameC, email, phone, username, password, imgUser, isAdmin);
                users.add(user);
            } while (cursor.moveToNext());

            cursor.close();
            database.close();
            userAdapter.notifyDataSetChanged();
        }
    }

    private void deleteUserFromDatabase(String userId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.delete("Users", "customerid = ?", new String[]{userId});
        database.close();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCustomerId() == Integer.parseInt(userId)) {
                users.remove(i);
                break;
            }
        }

        userAdapter.notifyDataSetChanged();
        clearEditTexts();
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com");
    }

    private boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addNewUser() {
        String userId = editTextUserId.getText().toString();
        String nameC = editTextNameC.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String imgUser = editTextImage.getText().toString();
        int isAdmin = Integer.parseInt(editTextIsAdmin.getText().toString());

        if (!isValidPhone(phone)) {
            showToast("Phone number must be 10 digits and contain no letters or special characters.");
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Email must end with @gmail.com.");
            return;
        }

        if (isUsernameTaken(username)) {
            showToast("Username is already taken. Please choose another one.");
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nameC", nameC);
        values.put("email", email);
        values.put("phone", phone);
        values.put("username", username);
        values.put("password", password);
        values.put("img_User", imgUser);
        values.put("is_admin", isAdmin);

        long newRowId = database.insert("Users", null, values);
        database.close();

        if (newRowId != -1) {
            User newUser = new User((int) newRowId, nameC, email, phone, username, password, imgUser, isAdmin);
            users.add(newUser);
            userAdapter.notifyDataSetChanged();
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            clearEditTexts();
        } else {
            Toast.makeText(this, "Error adding user", Toast.LENGTH_SHORT).show();
        }
    }

    private void editUser() {
        String userId = editTextUserId.getText().toString();
        String nameC = editTextNameC.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String imgUser = editTextImage.getText().toString();
        int isAdmin = Integer.parseInt(editTextIsAdmin.getText().toString());

        if (!isValidPhone(phone)) {
            showToast("Phone number must be 10 digits and contain no letters or special characters.");
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Email must end with @gmail.com.");
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nameC", nameC);
        values.put("email", email);
        values.put("phone", phone);
        values.put("username", username);
        values.put("password", password);
        values.put("img_User", imgUser);
        values.put("is_admin", isAdmin);

        int rowsAffected = database.update("Users", values, "customerid = ?", new String[]{userId});
        database.close();

        if (rowsAffected > 0) {
            for (User user : users) {
                if (user.getCustomerId() == Integer.parseInt(userId)) {
                    user.setNameC(nameC);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setImgUser(imgUser);
                    user.setIsAdmin(isAdmin);
                    break;
                }
            }
            userAdapter.notifyDataSetChanged();
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            clearEditTexts();
        } else {
            Toast.makeText(this, "Error updating user", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserDataToEditTexts(User user) {
        editTextUserId.setText(String.valueOf(user.getCustomerId()));
        editTextNameC.setText(user.getNameC());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhone());
        editTextUsername.setText(user.getUsername());
        editTextPassword.setText(user.getPassword());
        editTextImage.setText(user.getImgUser());
        editTextIsAdmin.setText(String.valueOf(user.getIsAdmin()));
    }

    private void clearEditTexts() {
        editTextUserId.setText("");
        editTextNameC.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
        editTextUsername.setText("");
        editTextPassword.setText("");
        editTextImage.setText("");
        editTextIsAdmin.setText("");
    }
}
