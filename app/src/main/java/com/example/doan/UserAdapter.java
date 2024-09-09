package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        User currentUser = users.get(position);

        ImageView imgUser = convertView.findViewById(R.id.imgUser);
        // Assuming you have a method to load images, e.g., using Glide or Picasso
        // Glide.with(context).load(currentUser.getImgUser()).into(imgUser);
        imgUser.setImageResource(getDrawableResIdByName(currentUser.getImgUser()));

        TextView tvUserId = convertView.findViewById(R.id.tvUserId);
        tvUserId.setText("ID: " + currentUser.getCustomerId());

        TextView tvNameC = convertView.findViewById(R.id.tvNameC);
        tvNameC.setText("Name: " + currentUser.getNameC());

        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        tvEmail.setText("Email: " + currentUser.getEmail());

        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        tvPhone.setText("Phone: " + currentUser.getPhone());

        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        tvUserName.setText("User name: " + currentUser.getUsername());

        return convertView;
    }

    public int getDrawableResIdByName(String resName) {
        String pkgName = context.getPackageName();
        return context.getResources().getIdentifier(resName, "drawable", pkgName);
    }

    public void updateUsers(ArrayList<User> filteredUsers) {
        users.clear();
        users.addAll(filteredUsers);
        notifyDataSetChanged();
    }

}
