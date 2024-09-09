package com.example.doan;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_dish_admin extends Fragment {
    private ArrayList<Dish> dishes;
    private DishAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_dish_admin() {
        // Required empty public constructor
    }

    public static Fragment_dish_admin newInstance(String param1, String param2) {
        Fragment_dish_admin fragment = new Fragment_dish_admin();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dish_admin, container, false);
        ListView lsHome = view.findViewById(R.id.lvHome);
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);

        dishes = new ArrayList<>();
        adapter = new DishAdapter(getActivity(), dishes);
        lsHome.setAdapter(adapter);

        // Khởi tạo Spinner và cài đặt sự kiện chọn sắp xếp
        String[] sortOptions = {"Tên món", "Số lượng", "Giá"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sortDishesByName();
                        break;
                    case 1:
                        sortDishesByQuantity();
                        break;
                    case 2:
                        sortDishesByPrice();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có hành động khi không chọn gì
            }
        });

        addEvents(view); // Xử lý sự kiện cho các thành phần giao diện

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDishesFromDatabase(); // Load lại dữ liệu từ cơ sở dữ liệu khi Fragment được hiển thị lại
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
                filterDishes(newText); // Filter danh sách món ăn khi nhập liệu vào SearchView
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            // Xử lý khi người dùng nhấn vào icon search
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterDishes(String query) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM Dishes WHERE nameD LIKE ?", new String[]{"%" + query + "%"});

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

    private void addEvents(View view) {
        ListView lsHome = view.findViewById(R.id.lvHome);

        // Xử lý sự kiện khi chọn một item trong ListView
        lsHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy món ăn được chọn
                Dish selectedDish = dishes.get(position);

                // Hiển thị hộp thoại xác nhận xóa
                showDeleteConfirmationDialog(selectedDish);
            }
        });

        ImageView addDishImageView = view.findViewById(R.id.addDishImageView);
        addDishImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Dishadmin.class);
                startActivity(intent); // Chuyển sang Activity để thêm món ăn mới
            }
        });
    }

    private void showDeleteConfirmationDialog(final Dish dishToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa món này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDish(dishToDelete);
                showToast("Xóa thành công!");
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteDish(Dish dishToDelete) {
        deleteDishFromDatabase(dishToDelete.getDishId());
        dishes.remove(dishToDelete);
        adapter.notifyDataSetChanged();
    }

    private void deleteDishFromDatabase(String dishId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete("Dishes", "dishid = ?", new String[]{dishId});
        database.close();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void sortDishesByName() {
        Collections.sort(dishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish dish1, Dish dish2) {
                return dish1.getName().compareToIgnoreCase(dish2.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortDishesByQuantity() {
        Collections.sort(dishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish dish1, Dish dish2) {
                return dish1.getQuantityD() - dish2.getQuantityD();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortDishesByPrice() {
        Collections.sort(dishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish dish1, Dish dish2) {
                return dish1.getPrice() - dish2.getPrice();
            }
        });
        adapter.notifyDataSetChanged();
    }
}
