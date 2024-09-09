package com.example.doan;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment_dish extends Fragment {
    private ArrayList<Dish> dishes;
    private DishAdapter adapter;
    private EditText editTextDishId;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextImage;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_dish() {
        // Required empty public constructor
    }

    public static Fragment_dish newInstance(String param1, String param2) {
        Fragment_dish fragment = new Fragment_dish();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish, container, false);

        ListView listView = view.findViewById(R.id.listView);
        dishes = new ArrayList<>();
        adapter = new DishAdapter(getActivity(), dishes);
        listView.setAdapter(adapter);

        loadDishesFromDatabase();

        editTextDishId = view.findViewById(R.id.editTextDishId);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);
        editTextImage = view.findViewById(R.id.editTextImage);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        Button buttonAddDish = view.findViewById(R.id.buttonAddDish);
        Button buttonUpdateDish = view.findViewById(R.id.buttonEdit);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idToDelete = editTextDishId.getText().toString();
                deleteDishFromDatabase(idToDelete);
            }
        });

        buttonAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDish();
            }
        });

        buttonUpdateDish.setOnClickListener(new View.OnClickListener() { // Sự kiện cho nút cập nhật
            @Override
            public void onClick(View v) {
                updateDish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish selectedDish = dishes.get(position);
                loadDishToForm(selectedDish);
            }
        });

        return view;
    }

    private void loadDishesFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
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

    private void deleteDishFromDatabase(String dishId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
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

    private void addNewDish() {
        // Thêm một món ăn mới vào cơ sở dữ liệu
        String dishId = editTextDishId.getText().toString(); // Lấy dishId từ EditText
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        String image = editTextImage.getText().toString();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dishid", dishId); // Sử dụng dishId nhập từ người dùng
        values.put("nameD", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantityD", quantity);
        values.put("imageD", image);

        long newRowId = database.insert("Dishes", null, values);

        if (newRowId != -1) {
            Toast.makeText(getActivity(), "New dish added successfully!", Toast.LENGTH_SHORT).show();
            loadDishesFromDatabase(); // Cập nhật danh sách món ăn trong ListView
        } else {
            Toast.makeText(getActivity(), "Failed to add new dish!", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

    private void updateDish() {
        // Cập nhật món ăn trong cơ sở dữ liệu
        String dishId = editTextDishId.getText().toString();
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        String image = editTextImage.getText().toString();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nameD", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantityD", quantity);
        values.put("imageD", image);

        int rowsUpdated = database.update("Dishes", values, "dishid = ?", new String[]{dishId});

        if (rowsUpdated > 0) {
            Toast.makeText(getActivity(), "Dish updated successfully!", Toast.LENGTH_SHORT).show();
            loadDishesFromDatabase(); // Cập nhật danh sách món ăn trong ListView
            clearForm();
        } else {
            Toast.makeText(getActivity(), "Failed to update dish!", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }
    private void clearForm() {
        editTextDishId.setText("");
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextImage.setText("");
    }
    private void loadDishToForm(Dish dish) {
        editTextDishId.setText(dish.getDishId());
        editTextName.setText(dish.getName());
        editTextDescription.setText(dish.getDescription());
        editTextPrice.setText(String.valueOf(dish.getPrice()));
        editTextQuantity.setText(String.valueOf(dish.getQuantityD()));
        editTextImage.setText(dish.getImage());
    }
}
