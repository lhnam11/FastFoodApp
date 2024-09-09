package com.example.doan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Order_Admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Order_Admin extends Fragment {

    private ArrayList<Order_User> orders;
    private OrderAdapter_User adapter;
    private String loggedInUsername;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public static Fragment_Order_Admin newInstance(String username) {
        Fragment_Order_Admin fragment = new Fragment_Order_Admin();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedInUsername = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__order__admin, container, false);

        ListView listView = view.findViewById(R.id.lvOrderad);
        orders = new ArrayList<>();
        adapter = new OrderAdapter_User(getActivity(), orders);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        loadOrders();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (database != null) {
            database.close();
        }
    }

    private void loadOrders() {
        databaseHelper = new DatabaseHelper(getActivity());
        database = databaseHelper.getReadableDatabase();

        // Câu truy vấn SQL
        String query = "SELECT Orders.orderid, Users.nameC, Dishes.nameD, Orders.quantity, Orders.orderdate, Orders.total_price, Dishes.imageD " +
                "FROM Orders " +
                "INNER JOIN Users ON Users.customerid = Orders.customerid " +
                "INNER JOIN Dishes ON Dishes.dishid = Orders.dishid";

        Cursor cursor = database.rawQuery(query, null);

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

                    // Định dạng lại ngày tháng
                    SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    sdfInput.setTimeZone(TimeZone.getTimeZone("GMT")); // Set time zone to GMT
                    Date dateTime;
                    try {
                        dateTime = sdfInput.parse(date);
                        date = sdfOutput.format(dateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Order_User order = new Order_User(orderId, nameC, nameD, quantity, date, price, imgName);
                    orders.add(order);
                } while (cursor.moveToNext());

                cursor.close();
                adapter.notifyDataSetChanged();
            }
        }

        database.close();
    }

}