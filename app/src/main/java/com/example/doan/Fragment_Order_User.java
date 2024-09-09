package com.example.doan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment_Order_User extends Fragment {
    private ArrayList<Order_User> orders;
    private OrderAdapter_User adapter;
    private String loggedInNameC;
    private DatabaseHelper databaseHelper; // Đối tượng quản lý cơ sở dữ liệu
    private SQLiteDatabase database; // Đối tượng SQLliteDatabase

    public static Fragment_Order_User newInstance(String nameC) {
        Fragment_Order_User fragment = new Fragment_Order_User();
        Bundle args = new Bundle();
        args.putString("nameC", nameC);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedInNameC = getArguments().getString("nameC");
            Log.d("Fragment_Order_User", "loggedInNameC: " + loggedInNameC); // Thêm log
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__order__user, container, false);

        ListView listView = view.findViewById(R.id.lvOrderU);
        orders = new ArrayList<>();
        adapter = new OrderAdapter_User(getActivity(), orders);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);  // Đăng ký ContextMenu

        loadOrders(); // Load dữ liệu từ cơ sở dữ liệu

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi không còn sử dụng nữa
        if (database != null) {
            database.close();
        }
    }

    private void loadOrders() {
        databaseHelper = new DatabaseHelper(getActivity());
        database = databaseHelper.getReadableDatabase();

        if (loggedInNameC == null || loggedInNameC.isEmpty()) {
            Toast.makeText(getActivity(), "Không thể tải đơn hàng: nameC không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String query = "SELECT Orders.orderid, Users.nameC, Dishes.nameD, Orders.quantity, Orders.orderdate, Orders.total_price, Dishes.imageD " +
                "FROM Orders " +
                "INNER JOIN Users ON Users.customerid = Orders.customerid " +
                "INNER JOIN Dishes ON Dishes.dishid = Orders.dishid " +
                "WHERE Users.nameC = ?";

        Cursor cursor = database.rawQuery(query, new String[]{loggedInNameC});

        if (cursor != null) {
            orders.clear();
            if (cursor.moveToFirst()) {
                do {
                    String orderId = cursor.getString(cursor.getColumnIndexOrThrow("orderid"));
                    String nameC = cursor.getString(cursor.getColumnIndexOrThrow("nameC"));
                    String nameD = cursor.getString(cursor.getColumnIndexOrThrow("nameD"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("orderdate"));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow("total_price"));
                    String imgName = cursor.getString(cursor.getColumnIndexOrThrow("imageD"));

                    Order_User order = new Order_User(orderId, nameC, nameD, quantity, date, price, imgName);
                    orders.add(order);
                } while (cursor.moveToNext());

                Log.d("Fragment_Order_User", "Orders loaded: " + orders.size()); // Thêm log

                cursor.close();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Không có đơn hàng nào được tìm thấy", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

}
