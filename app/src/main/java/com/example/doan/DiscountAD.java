package com.example.doan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscountAD extends AppCompatActivity {

    private ListView listViewGiamGia;
    private List<Discount> discountList;
    private DiscountAdapter discountAdapter;

    private EditText etMaGiam, etThongTinGiam, etPhanTramGiam, etSoLuongMa, etHinhAnh;
    private Button btnThem, btnSua, btnXoa,btnBack;

    private DatabaseReference discountsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_ad);

        listViewGiamGia = findViewById(R.id.lsGiamGia);
        etMaGiam = findViewById(R.id.etMaGiam);
        etThongTinGiam = findViewById(R.id.etThongTinGiam);
        etPhanTramGiam = findViewById(R.id.etPhanTramGiam);
        etSoLuongMa = findViewById(R.id.etSoLuongMa);
        etHinhAnh = findViewById(R.id.etHinhAnh);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnBack = findViewById(R.id.btnBack);
        discountList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(this, R.layout.discount_item, discountList);
        listViewGiamGia.setAdapter(discountAdapter);

        discountsRef = FirebaseDatabase.getInstance().getReference("Discount");
        loadDiscountsFromFirebase();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiscount();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDiscount();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDiscount();
            }
        });

        listViewGiamGia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Discount discount = discountList.get(position);
                etMaGiam.setText(discount.getMagiam());
                etThongTinGiam.setText(discount.getThongtinmagiam());
                etPhanTramGiam.setText(String.valueOf(discount.getPhantramgiam()));
                etSoLuongMa.setText(String.valueOf(discount.getSoluongma()));
                etHinhAnh.setText(discount.getHinhanh());
            }
        });
    }

    private void loadDiscountsFromFirebase() {
        discountsRef.addValueEventListener(new ValueEventListener() {
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
                Log.e("DiscountAD", "Failed to read discounts from Firebase.", error.toException());
            }
        });
    }

    private void addDiscount() {
        String maGiam = etMaGiam.getText().toString().trim();
        String thongTinGiam = etThongTinGiam.getText().toString().trim();
        String strPhanTramGiam = etPhanTramGiam.getText().toString().trim();
        String strSoLuongMa = etSoLuongMa.getText().toString().trim();
        String hinhAnh = etHinhAnh.getText().toString().trim();

        if (TextUtils.isEmpty(maGiam) || TextUtils.isEmpty(thongTinGiam) ||
                TextUtils.isEmpty(strPhanTramGiam) || TextUtils.isEmpty(strSoLuongMa)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int phanTramGiam = Integer.parseInt(strPhanTramGiam);
        int soLuongMa = Integer.parseInt(strSoLuongMa);

        Discount discount = new Discount(maGiam, thongTinGiam, phanTramGiam, soLuongMa, hinhAnh);
        String key = discountsRef.push().getKey();
        discountsRef.child(key).setValue(discount);

        clearEditTextFields();
        Toast.makeText(this, "Đã thêm giảm giá mới", Toast.LENGTH_SHORT).show();
    }

    private void updateDiscount() {
        String maGiam = etMaGiam.getText().toString().trim();
        String thongTinGiam = etThongTinGiam.getText().toString().trim();
        String strPhanTramGiam = etPhanTramGiam.getText().toString().trim();
        String strSoLuongMa = etSoLuongMa.getText().toString().trim();
        String hinhAnh = etHinhAnh.getText().toString().trim();

        if (TextUtils.isEmpty(maGiam) || TextUtils.isEmpty(thongTinGiam) ||
                TextUtils.isEmpty(strPhanTramGiam) || TextUtils.isEmpty(strSoLuongMa)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int phanTramGiam = Integer.parseInt(strPhanTramGiam);
        int soLuongMa = Integer.parseInt(strSoLuongMa);

        Discount discount = new Discount(maGiam, thongTinGiam, phanTramGiam, soLuongMa, hinhAnh);
        String key = discountsRef.push().getKey();
        discountsRef.child(key).setValue(discount);

        clearEditTextFields();
        Toast.makeText(this, "Đã cập nhật giảm giá", Toast.LENGTH_SHORT).show();
    }

    private void deleteDiscount() {
        String maGiam = etMaGiam.getText().toString().trim();

        if (TextUtils.isEmpty(maGiam)) {
            Toast.makeText(this, "Vui lòng nhập Mã giảm giá để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        discountsRef.orderByChild("magiam").equalTo(maGiam).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                clearEditTextFields();
                Toast.makeText(DiscountAD.this, "Đã xóa giảm giá", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DiscountAD", "Failed to delete discount from Firebase.", error.toException());
            }
        });
    }

    private void clearEditTextFields() {
        etMaGiam.setText("");
        etThongTinGiam.setText("");
        etPhanTramGiam.setText("");
        etSoLuongMa.setText("");
        etHinhAnh.setText("");
    }

    private void confirmDeleteDiscount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa mã giảm giá này?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDiscount();
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDiscounts(newText);
                return true;
            }
        });
        return true;
    }

    private void filterDiscounts(String query) {
        List<Discount> filteredList = new ArrayList<>();
        for (Discount discount : discountList) {
            if (discount.getMagiam().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(discount);
            }
        }
        discountAdapter.updateList(filteredList);
    }
}
