package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DiscountAdapter extends ArrayAdapter<Discount> {

    private Context context;
    private int resource;
    private List<Discount> discountList;

    public DiscountAdapter(@NonNull Context context, int resource, @NonNull List<Discount> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.discountList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Discount discount = discountList.get(position);

        TextView tvMaGiam = convertView.findViewById(R.id.tvMaGiam);
        TextView tvThongTinMaGiam = convertView.findViewById(R.id.tvThongTinMaGiam);
        TextView tvPhanTramGiam = convertView.findViewById(R.id.tvPhanTramGiam);
        ImageView ivHinhAnh = convertView.findViewById(R.id.ivHinhAnh);

        tvMaGiam.setText(discount.getMagiam());
        tvThongTinMaGiam.setText(discount.getThongtinmagiam());
        tvPhanTramGiam.setText(discount.getPhantramgiam() + "%");

        // Lấy hình ảnh từ drawable
        int resId = getDrawableResIdByName(discount.getHinhanh());
        ivHinhAnh.setImageResource(resId);

        return convertView;
    }

    private int getDrawableResIdByName(String resName) {
        String pkgName = context.getPackageName();
        return context.getResources().getIdentifier(resName, "drawable", pkgName);
    }

    // Phương thức cập nhật danh sách
    public void updateList(List<Discount> newDiscountList) {
        discountList.clear();
        discountList.addAll(newDiscountList);
        notifyDataSetChanged();
    }
}
