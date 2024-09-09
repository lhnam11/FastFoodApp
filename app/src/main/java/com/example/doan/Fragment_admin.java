package com.example.doan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class Fragment_admin extends Fragment {

    private TextView nameCTextView;

    public Fragment_admin() {
        // Required empty public constructor
    }

    public static Fragment_admin newInstance(String nameC) {
        Fragment_admin fragment = new Fragment_admin();
        Bundle args = new Bundle();
        args.putString("nameC", nameC); // Đặt nameC vào Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String nameC = getArguments().getString("nameC");
            updateName(nameC); // Cập nhật tên người dùng vào TextView
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        nameCTextView = view.findViewById(R.id.nameC_admin);
        if (getArguments() != null) {
            String nameC = getArguments().getString("nameC");
            nameCTextView.setText(nameC); // Hiển thị nameC trong TextView
        }
        return view;
    }

    public void updateName(String nameC) {
        if (nameCTextView != null && nameC != null) {
            nameCTextView.setText(nameC);
        }
    }
}
