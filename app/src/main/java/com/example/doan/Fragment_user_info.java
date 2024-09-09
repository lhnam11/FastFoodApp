package com.example.doan;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_user_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_user_info extends Fragment {
    private TextView nameCTextView, phoneTextView, emailTextView, usernameTextView, passwordTextView;
    private ImageView profileImageView;

    public Fragment_user_info() {
        // Required empty public constructor
    }

    public static Fragment_user_info newInstance(String nameC, String phone, String email, String username, String password, String imageResName, int customerId) {
        Fragment_user_info fragment = new Fragment_user_info();
        Bundle args = new Bundle();
        args.putString("nameC", nameC);
        args.putString("phone", phone);
        args.putString("email", email);
        args.putString("username", username);
        args.putString("password", password);
        args.putString("imageResName", imageResName); // Thêm tên hình ảnh
        args.putInt("customerid", customerId); // Thêm customerid
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        nameCTextView = view.findViewById(R.id.tvFullNameinf);
        phoneTextView = view.findViewById(R.id.tvPhoneinf);
        emailTextView = view.findViewById(R.id.tvEmailinf);
        usernameTextView = view.findViewById(R.id.tvUsernameinf);
        passwordTextView = view.findViewById(R.id.tvPassWordinf);
        profileImageView = view.findViewById(R.id.imageHinhAnhinf);

        if (getArguments() != null) {
            String nameC = getArguments().getString("nameC");
            String phone = getArguments().getString("phone");
            String email = getArguments().getString("email");
            String username = getArguments().getString("username");
            String password = getArguments().getString("password");
            String imageResName = getArguments().getString("imageResName");
            int customerId = getArguments().getInt("customerid"); // Nhận customerid

            nameCTextView.setText(nameC);
            phoneTextView.setText(phone);
            emailTextView.setText(email);
            usernameTextView.setText(username);
            passwordTextView.setText(password);

            // Lấy mã tài nguyên hình ảnh sử dụng tên hình ảnh
            int imageResId = getResources().getIdentifier(imageResName, "drawable", getActivity().getPackageName());
            profileImageView.setImageResource(imageResId);
        }

        return view;
    }
}
