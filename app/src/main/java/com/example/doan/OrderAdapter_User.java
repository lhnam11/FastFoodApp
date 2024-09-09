package com.example.doan;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter_User extends ArrayAdapter<Order_User> {
    private Context context;
    private ArrayList<Order_User> orders;

    public OrderAdapter_User(Context context, ArrayList<Order_User> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_order_user, parent, false);
        }

        Order_User currentOrder = orders.get(position);

        ImageView img = convertView.findViewById(R.id.anh_OU);
        // Cập nhật hình ảnh từ tên resource trong đối tượng Order_User
        int imageResId = getDrawableResIdByName(currentOrder.getImageD());
        img.setImageResource(imageResId);

        TextView orderID = convertView.findViewById(R.id.tvOrderId_OU);
        orderID.setText(currentOrder.getOrderid());

        TextView nameC = convertView.findViewById(R.id.tvCustomerName_OU);
        nameC.setText(currentOrder.getNameC());

        TextView nameD = convertView.findViewById(R.id.tvDishName_OU);
        nameD.setText(currentOrder.getNameD());

        TextView quantity = convertView.findViewById(R.id.tvquatity_OU);
        quantity.setText(String.valueOf(currentOrder.getQuantity()));

        TextView date = convertView.findViewById(R.id.tvDate_OU);
        date.setText(currentOrder.getOrderdate());

        TextView price = convertView.findViewById(R.id.tvprice_OU);
        price.setText(String.valueOf(currentOrder.getTotal_price()));

        convertView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Bạn có chắc muốn xóa đơn hàng này không?")
                    .setPositiveButton("Xác nhận", (dialog, id) -> removeItem(position))
                    .setNegativeButton("Hủy bỏ", (dialog, id) -> dialog.dismiss())
                    .create()
                    .show();
        });

        return convertView;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < orders.size()) {
            orders.remove(position); // Xóa item khỏi danh sách
            notifyDataSetChanged(); // Cập nhật lại ListView
        }
    }

    public int getDrawableResIdByName(String resName) {
        String pkgName = context.getPackageName();
        return context.getResources().getIdentifier(resName, "drawable", pkgName);
    }
}
