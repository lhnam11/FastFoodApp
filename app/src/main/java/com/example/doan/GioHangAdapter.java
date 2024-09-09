package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.GioHangViewHolder> {
    private List<GioHang> magiohang;
    private Context context;
    private GioHangActivity gioHangActivity;
    private double discountPercentage; // Thêm trường discountPercentage

    public GioHangAdapter(Context context, List<GioHang> magiohang, GioHangActivity gioHangActivity) {
        this.context = context;
        this.magiohang = magiohang;
        this.gioHangActivity = gioHangActivity;
        this.discountPercentage = 0.0; // Khởi tạo ban đầu discountPercentage = 0
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        final GioHang gioHang = magiohang.get(position);

        holder.tenSP.setText(gioHang.getTengh());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String formattedPrice = decimalFormat.format(gioHang.getGiagh());
        holder.giaSP.setText(formattedPrice + " đ");

        holder.soLuong.setText(String.valueOf(gioHang.getSoluonggh()));

        long totalPrice = gioHang.getSoluonggh() * gioHang.getGiagh();
        long discountedPrice = (long) (totalPrice * (1 - discountPercentage)); // Tính toán giá giảm giá
        String formattedTotalPrice = decimalFormat.format(discountedPrice);
        holder.giaSP2.setText(formattedTotalPrice + " đ");

        int imageResId = getDrawableResIdByName(gioHang.getHinhgh());
        if (imageResId != 0) {
            holder.imageSP.setImageResource(imageResId);
        }

        holder.itemView.findViewById(R.id.item_giohang_tru).setOnClickListener(v -> {
            if (gioHang.getSoluonggh() > 1) {
                gioHang.setSoluonggh(gioHang.getSoluonggh() - 1);
                notifyItemChanged(holder.getAdapterPosition());
                gioHangActivity.updateCart();
            }
        });

        holder.itemView.findViewById(R.id.item_giohang_cong).setOnClickListener(v -> {
            gioHang.setSoluonggh(gioHang.getSoluonggh() + 1);
            notifyItemChanged(holder.getAdapterPosition());
            gioHangActivity.updateCart();
        });
    }

    @Override
    public int getItemCount() {
        return magiohang.size();
    }

    static class GioHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSP;
        TextView tenSP, giaSP, soLuong, giaSP2;

        public GioHangViewHolder(View itemView) {
            super(itemView);
            imageSP = itemView.findViewById(R.id.item_giohang_image);
            tenSP = itemView.findViewById(R.id.item_giohang_tensp);
            giaSP = itemView.findViewById(R.id.item_giohang_gia);
            soLuong = itemView.findViewById(R.id.item_giohang_soluong);
            giaSP2 = itemView.findViewById(R.id.item_giohang_gia2);
        }
    }

    private int getDrawableResIdByName(String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }
    // giảm giá
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        notifyDataSetChanged(); // Cập nhật lại toàn bộ danh sách khi giảm giá thay đổi
    }
    public long getDiscountedPrice(GioHang gioHang) {
        long totalPrice = gioHang.getSoluonggh() * gioHang.getGiagh();
        return (long) (totalPrice * (1 - discountPercentage));
    }
    public long getTotalDiscountedPrice() {
        long totalPrice = 0;
        for (GioHang gioHang : magiohang) {
            totalPrice += getDiscountedPrice(gioHang);
        }
        return totalPrice;
    }
}
