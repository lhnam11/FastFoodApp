package com.example.doan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Fragment_discount extends Fragment {
    private ListView listViewGiamGia;
    private List<Discount> discountList;
    private DiscountAdapter discountAdapter;

    private DatabaseReference discountsRef;


    public Fragment_discount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable menu for this fragment

        // Connect to Firebase Realtime Database
        discountsRef = FirebaseDatabase.getInstance().getReference("Discount");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        listViewGiamGia = view.findViewById(R.id.lsGiamGia);
        ImageView addDisImageView = view.findViewById(R.id.addDisImageView);
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);

        discountList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(getActivity(), R.layout.discount_item, discountList);
        listViewGiamGia.setAdapter(discountAdapter);

        loadDiscountsFromFirebase();

        // Setup spinner for sorting options
        setupSortSpinner(sortSpinner);

        // Handle add discount event
        addDisImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to another activity
                Intent intent = new Intent(getActivity(), DiscountAD.class);
                startActivity(intent);
            }
        });

        // Handle long click event on ListView item
        listViewGiamGia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Discount selectedDiscount = discountList.get(position);
                showDeleteConfirmationDialog(selectedDiscount);
                return true;
            }
        });

        return view;
    }

    private void setupSortSpinner(Spinner sortSpinner) {
        // Array of sorting options
        String[] sortOptions = {"Tên", "Phần trăm giảm", "Số lượng"};

        // Create ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);

        // Set listener for item selection
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sort discounts based on selected option
                sortDiscounts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void sortDiscounts(int position) {
        switch (position) {
            case 0: // Sort by name
                Collections.sort(discountList, new Comparator<Discount>() {
                    @Override
                    public int compare(Discount d1, Discount d2) {
                        return d1.getMagiam().compareToIgnoreCase(d2.getMagiam());
                    }
                });
                break;
            case 1: // Sort by percentage
                Collections.sort(discountList, new Comparator<Discount>() {
                    @Override
                    public int compare(Discount d1, Discount d2) {
                        return Integer.compare(d1.getPhantramgiam(), d2.getPhantramgiam());
                    }
                });
                break;
            case 2: // Sort by quantity
                Collections.sort(discountList, new Comparator<Discount>() {
                    @Override
                    public int compare(Discount d1, Discount d2) {
                        return Integer.compare(d1.getSoluongma(), d2.getSoluongma());
                    }
                });
                break;
            default:
                break;
        }
        discountAdapter.notifyDataSetChanged();
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
                Log.e("Fragment_discount", "Failed to read discounts from Firebase.", error.toException());
            }
        });
    }

    private void showDeleteConfirmationDialog(final Discount discount) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xóa mã giảm giá")
                .setMessage("Bạn có chắc chắn muốn xóa mã giảm giá này không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDiscount(discount);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteDiscount(Discount discount) {
        DatabaseReference discountRef = discountsRef.child(discount.getMagiam());
        discountRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Xóa mã giảm giá thành công", Toast.LENGTH_SHORT).show();
                discountList.remove(discount);
                discountAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Xóa mã giảm giá thất bại", Toast.LENGTH_SHORT).show();
            }
        });
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
                filterDiscounts(newText);
                return true;
            }
        });
    }

    private void filterDiscounts(String query) {
        ArrayList<Discount> filteredDiscounts = new ArrayList<>();
        for (Discount discount : discountList) {
            // Filter by discount code or description, depending on the requirement
            if (discount.getMagiam().toLowerCase().contains(query.toLowerCase()) ||
                    discount.getThongtinmagiam().toLowerCase().contains(query.toLowerCase())) {
                filteredDiscounts.add(discount);
            }
        }
        discountAdapter.updateList(filteredDiscounts);
    }
}
