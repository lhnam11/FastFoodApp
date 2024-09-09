package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dishadmin extends AppCompatActivity {

    private ArrayList<Dish> dishes;
    private DishAdapter adapter;
    private EditText editTextDishId;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishadmin);

        // Khởi tạo danh sách món ăn và adapter
        dishes = new ArrayList<>();
        adapter = new DishAdapter(this, dishes);

        // Liên kết ListView và Adapter
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Khởi tạo các EditText và Button
        editTextDishId = findViewById(R.id.editTextDishId);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextImage = findViewById(R.id.editTextImage);
        Button btnBack = findViewById(R.id.btn_back);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        Button buttonAddDish = findViewById(R.id.buttonAddDish);
        Button buttonUpdateDish = findViewById(R.id.buttonEdit);

        // Xử lý sự kiện khi click nút Xóa
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idToDelete = editTextDishId.getText().toString();
                deleteDishFromDatabase(idToDelete);
            }
        });

        // Xử lý sự kiện khi click nút Thêm món ăn mới
        buttonAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDish();
            }
        });

        // Xử lý sự kiện khi click nút Cập nhật món ăn
        buttonUpdateDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại khi nút Back được nhấn
            }
        });
        // Xử lý sự kiện khi click vào một món ăn trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish selectedDish = dishes.get(position);
                loadDishToForm(selectedDish);
            }
        });

        // Load danh sách món ăn từ cơ sở dữ liệu
        loadDishesFromDatabase();
    }

    // Phương thức để load danh sách món ăn từ cơ sở dữ liệu vào ListView
    private void loadDishesFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM Dishes", null);
        if (cursor != null && cursor.moveToFirst()) {
            dishes.clear();
            do {
                String dishId = cursor.getString(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                int price = cursor.getInt(3);
                int quantity = cursor.getInt(4);
                String image = cursor.getString(5);

                Dish dish = new Dish(dishId, name, description, price, quantity, image);
                dishes.add(dish);
            } while (cursor.moveToNext());

            cursor.close();
            database.close();
            adapter.notifyDataSetChanged();
        }
    }

    // Phương thức để xóa một món ăn từ cơ sở dữ liệu và cập nhật ListView
    private void deleteDishFromDatabase(String dishId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // Thực hiện xóa dữ liệu từ cơ sở dữ liệu dựa trên ID
        database.delete("Dishes", "dishid = ?", new String[]{dishId});

        // Đóng kết nối cơ sở dữ liệu
        database.close();

        // Xóa món ăn đã bị xóa khỏi danh sách
        for (int i = 0; i < dishes.size(); i++) {
            if (dishes.get(i).getDishId().equals(dishId)) {
                dishes.remove(i);
                break;
            }
        }

        // Cập nhật ListView
        adapter.notifyDataSetChanged();
    }

    // Phương thức để thêm một món ăn mới vào cơ sở dữ liệu và cập nhật ListView
    private void addNewDish() {
        // Lấy thông tin từ EditText
        String dishId = editTextDishId.getText().toString();
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        String image = editTextImage.getText().toString();

        // Tạo ContentValues để chứa dữ liệu cần thêm
        ContentValues values = new ContentValues();
        values.put("dishid", dishId);
        values.put("nameD", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantityD", quantity);
        values.put("imageD", image);

        // Thêm vào cơ sở dữ liệu
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long newRowId = database.insert("Dishes", null, values);

        // Kiểm tra kết quả thêm và thông báo cho người dùng
        if (newRowId != -1) {
            Toast.makeText(this, "New dish added successfully!", Toast.LENGTH_SHORT).show();
            loadDishesFromDatabase(); // Cập nhật danh sách món ăn trong ListView
        } else {
            Toast.makeText(this, "Failed to add new dish!", Toast.LENGTH_SHORT).show();
        }

        // Đóng kết nối cơ sở dữ liệu
        database.close();
    }

    // Phương thức để cập nhật thông tin một món ăn trong cơ sở dữ liệu và cập nhật ListView
    private void updateDish() {
        // Lấy thông tin từ EditText
        String dishId = editTextDishId.getText().toString();
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        String image = editTextImage.getText().toString();

        // Tạo ContentValues để chứa dữ liệu cần cập nhật
        ContentValues values = new ContentValues();
        values.put("nameD", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantityD", quantity);
        values.put("imageD", image);

        // Cập nhật vào cơ sở dữ liệu
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated = database.update("Dishes", values, "dishid = ?", new String[]{dishId});

        // Kiểm tra kết quả cập nhật và thông báo cho người dùng
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Dish updated successfully!", Toast.LENGTH_SHORT).show();
            loadDishesFromDatabase(); // Cập nhật danh sách món ăn trong ListView
            clearForm();
        } else {
            Toast.makeText(this, "Failed to update dish!", Toast.LENGTH_SHORT).show();
        }

        // Đóng kết nối cơ sở dữ liệu
        database.close();
    }

    // Phương thức để xóa nội dung trong các EditText
    private void clearForm() {
        editTextDishId.setText("");
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextImage.setText("");
    }

    // Phương thức để load thông tin món ăn được chọn từ ListView lên các EditText
    private void loadDishToForm(Dish dish) {
        editTextDishId.setText(dish.getDishId());
        editTextName.setText(dish.getName());
        editTextDescription.setText(dish.getDescription());
        editTextPrice.setText(String.valueOf(dish.getPrice()));
        editTextQuantity.setText(String.valueOf(dish.getQuantityD()));
        editTextImage.setText(dish.getImage());
    }
}
