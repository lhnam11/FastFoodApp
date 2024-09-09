package com.example.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GioHangActivity extends AppCompatActivity {

    private TextView gioHangTrong, tongTien;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GioHangAdapter adapter;
    private Button btnMuaHang;
    private List<GioHang> magiohang;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private int customerId;
    private ListView listViewGiamGia;
    private List<Discount> discountList;
    private DiscountAdapter discountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        databaseHelper = new DatabaseHelper(this);
        initView();
        initControl();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customer_id", -1);

        if (customerId == -1) {
            Log.e("GioHangActivity", "Failed to retrieve customerId from SharedPreferences.");
            finish();
        }

        loadDiscountsFromFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        magiohang = Dish_Detail.magiohang;
        updateCart();
    }

    private void initView() {
        gioHangTrong = findViewById(R.id.txtGiohangtrong);
        tongTien = findViewById(R.id.txtTongTien);
        recyclerView = findViewById(R.id.recycleviewGioHang);
        toolbar = findViewById(R.id.toolbar);
        btnMuaHang = findViewById(R.id.btnmuahang);
        listViewGiamGia = findViewById(R.id.lsGiamGia);
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnMuaHang.setOnClickListener(view -> handleOrder());
    }

    private void loadDiscountsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Discount");

        discountList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(this, R.layout.discount_item, discountList);
        listViewGiamGia.setAdapter(discountAdapter);
        listViewGiamGia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Discount được click
                Discount discount = discountList.get(position);

                // Áp dụng giảm giá
                applyDiscount(discount);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discountList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Discount discount = snapshot.getValue(Discount.class);
                    discountList.add(discount);
                }
                discountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GioHangActivity", "Failed to read discounts from Firebase.", error.toException());
            }
        });
    }

    private void applyDiscount(Discount discount) {
        double discountPercentage = discount.getPhantramgiam() * 0.01;

        // Set discount percentage to GioHangAdapter
        adapter.setDiscountPercentage(discountPercentage);

        long totalPrice = 0;
        for (GioHang gioHang : magiohang) {
            // Tính toán giá tiền sau khi áp dụng giảm giá cho từng sản phẩm
            long discountedPrice = (long) (gioHang.getGiagh() * gioHang.getSoluonggh() * (1 - discountPercentage));
            totalPrice += discountedPrice;
        }

        tongTien.setText(String.format("%,d đ", totalPrice));
    }

    public void updateCart() {
        if (magiohang != null && !magiohang.isEmpty()) {
            adapter = new GioHangAdapter(this, magiohang, this);
            recyclerView.setAdapter(adapter);
            gioHangTrong.setVisibility(View.GONE);

            long totalPrice = 0;
            for (GioHang gioHang : magiohang) {
                totalPrice += gioHang.getSoluonggh() * gioHang.getGiagh();
            }
            tongTien.setText(String.format("%,d đ", totalPrice));
        } else {
            gioHangTrong.setVisibility(View.VISIBLE);
            tongTien.setText("0 đ");
        }
    }
    // bổ sung mã giảm
    public void handleOrder() {
        db = databaseHelper.getWritableDatabase();

        if (db == null) {
            Log.e("Database Error", "Unable to get writable database.");
            Toast.makeText(this, "Database error. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerId != -1 && magiohang != null && !magiohang.isEmpty()) {
            db.beginTransaction();
            try {
                for (GioHang gioHang : magiohang) {
                    String orderId = UUID.randomUUID().toString();
                    String dishId = gioHang.getIdgh();
                    int quantity = gioHang.getSoluonggh();
                    long timestamp = System.currentTimeMillis();

                    // Lấy giá đã giảm từ Adapter
                    long discountedPrice = adapter.getDiscountedPrice(gioHang);

                    // Insert order với giá đã giảm
                    boolean success = databaseHelper.insertOrder(orderId, customerId, dishId, quantity, timestamp, discountedPrice);

                    if (!success) {
                        db.endTransaction();
                        Toast.makeText(this, "Failed to place order for dish: " + dishId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                db.setTransactionSuccessful();
                magiohang.clear();
                updateCart();

                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GioHangActivity.this, Fragment_trangchu.class);
                startActivity(intent);

            } catch (Exception e) {
                db.endTransaction();
                Log.e("OrderPlacement", "Error placing order: " + e.getMessage());
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
            } finally {
                db.endTransaction();
            }
        } else {
            Toast.makeText(this, "Failed to place order, invalid customer or dish", Toast.LENGTH_SHORT).show();
        }
    }


    private double getSelectedDiscountPercentage() {
        double discountPercentage = 0.0;
        for (Discount discount : discountList) {
            // Assuming isSelected() method exists and correctly identifies selected discount
            if (discount.isSelected()) {
                discountPercentage = discount.getPhantramgiam() * 0.01;
                break;
            }
        }
        return discountPercentage;
    }
}
