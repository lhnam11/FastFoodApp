package com.example.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Fragment_trangchu extends Fragment {

    private ArrayList<Dish> dishes;
    private DishAdapter adapter;
    private ViewFlipper viewFlipper;
    private Toolbar toolbar;
    private Spinner spinner;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_trangchu() {
        // Required empty public constructor
    }

    public static Fragment_trangchu newInstance(String param1, String param2) {
        Fragment_trangchu fragment = new Fragment_trangchu();
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
        View view = inflater.inflate(R.layout.fragment_trangchu, container, false);

        ListView lsHome = view.findViewById(R.id.lvHome);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        toolbar = view.findViewById(R.id.toolbar);
        spinner = view.findViewById(R.id.spinner2);

        // Toolbar setup
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());

        // ViewFlipper setup
        ActionViewFlipper();

        // Spinner setup
        setupSpinner();

        // Initialize dishes list and adapter
        dishes = new ArrayList<>();
        adapter = new DishAdapter(getActivity(), dishes);
        lsHome.setAdapter(adapter);

        // Load dishes from the database
        loadDishesFromDatabase();

        // Handle item click on ListView
        addEvents(lsHome);

        return view;
    }

    private void setupSpinner() {
        // Create an array of sorting options
        String[] sortOptions = {"Theo tên", "Theo giá", "Theo số lượng"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, sortOptions);

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);

        // Set a listener to handle spinner item selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perform sorting based on selected item
                switch (position) {
                    case 0:
                        // Sort by name
                        Collections.sort(dishes, new Comparator<Dish>() {
                            @Override
                            public int compare(Dish dish1, Dish dish2) {
                                return dish1.getName().compareTo(dish2.getName());
                            }
                        });
                        break;
                    case 1:
                        // Sort by price
                        Collections.sort(dishes, new Comparator<Dish>() {
                            @Override
                            public int compare(Dish dish1, Dish dish2) {
                                return dish1.getPrice() - dish2.getPrice();
                            }
                        });
                        break;
                    case 2:
                        // Sort by quantity
                        Collections.sort(dishes, new Comparator<Dish>() {
                            @Override
                            public int compare(Dish dish1, Dish dish2) {
                                return dish1.getQuantityD() - dish2.getQuantityD();
                            }
                        });
                        break;
                }

                // Notify adapter of data change after sorting
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });
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
        }

        database.close();
        adapter.notifyDataSetChanged();
    }

    private void addEvents(ListView lsHome) {
        lsHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish selectedDish = dishes.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("dish_id", selectedDish.getDishId());
                bundle.putString("dish_name", selectedDish.getName());
                bundle.putString("dish_mota", selectedDish.getDescription());
                bundle.putInt("dish_gia", selectedDish.getPrice());
                bundle.putString("dish_image", selectedDish.getImage());
                bundle.putInt("dish_quantity", selectedDish.getQuantityD());

                Intent intent = new Intent(getActivity(), Dish_Detail.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void ActionViewFlipper() {
        List<String> maquangcao = new ArrayList<>();
        maquangcao.add("image1");
        maquangcao.add("image4");
        maquangcao.add("image5");

        for (String imageName : maquangcao) {
            int resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
            flipperImages(resID);
        }

        // Display the first image by default
        viewFlipper.setDisplayedChild(0);

        // Set flip interval and auto start
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        // Use right to left animation effect
        Animation slideInRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        slideInRight.setDuration(500);
        viewFlipper.setInAnimation(slideInRight);

        Animation slideOutLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        slideOutLeft.setDuration(500);
        viewFlipper.setOutAnimation(slideOutLeft);

        viewFlipper.startFlipping();
    }

    private void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
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
                filterDishes(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            // Handle search item selection
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterDishes(String query) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM Dishes WHERE nameD LIKE ?", new String[]{"%" + query
                + "%"});

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

    @Override
    public void onResume() {
        super.onResume();
        // Reload dishes from database when fragment resumes
        loadDishesFromDatabase();
    }
}
