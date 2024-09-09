package com.example.doan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Fragment_nv extends Fragment {
    private ArrayList<User> users;
    private UserAdapter userAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_nv() {
        // Required empty public constructor
    }

    public static Fragment_nv newInstance(String param1, String param2) {
        Fragment_nv fragment = new Fragment_nv();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsersFromDatabase(); // Load lại dữ liệu từ cơ sở dữ liệu khi Fragment được hiển thị lại
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText); // Filter danh sách user khi nhập liệu vào SearchView
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nv, container, false);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setText("Quản lý nhân viên");

        ListView listViewUsers = view.findViewById(R.id.listViewUsers);
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), users);
        listViewUsers.setAdapter(userAdapter);

        loadUsersFromDatabase();

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
                showDeleteConfirmationDialog(position);
                return true;
            }
        });

        // Handle ImageView click to navigate to another activity
        ImageView addUserImageView = view.findViewById(R.id.addAdminImageView);
        addUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomerAD.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadUsersFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // Query chỉ lấy các user có quyền admin (is_admin = 0)
        Cursor cursor = database.rawQuery("SELECT * FROM Users WHERE is_admin = 0", null);
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

    private void filterUsers(String query) {
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getNameC().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                    user.getPhone().contains(query) ||
                    user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        userAdapter.updateUsers(filteredUsers);
    }

    private void loadUserDataToEditTexts(User user) {
        // Implement your logic to load data into EditTexts
        // This is just a placeholder method
        // Modify according to your actual EditTexts and logic
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn xóa người dùng này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User userToDelete = users.get(position);
                        deleteUser(userToDelete);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser(User userToDelete) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // Delete user from database
        int rowsDeleted = database.delete("Users", "customerId=?", new String[]{String.valueOf(userToDelete.getCustomerId())});
        if (rowsDeleted > 0) {
            Toast.makeText(getActivity(), "Xóa người dùng thành công", Toast.LENGTH_SHORT).show();
            users.remove(userToDelete);
            userAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Xóa người dùng thất bại", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
