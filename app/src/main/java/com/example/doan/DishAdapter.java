package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;


public class DishAdapter extends ArrayAdapter<Dish> {
    private Context context;
    private ArrayList<Dish> dishes;

    public DishAdapter(Context context, ArrayList<Dish> dishes) {
        super(context, 0, dishes);
        this.context = context;
        this.dishes = dishes;
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_dish, parent, false);
        }

        Dish currentDish = dishes.get(position);

        ImageView img = convertView.findViewById(R.id.imageView);

        img.setImageResource(getDrawableResIdByName(currentDish.getImage()));

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        nameTextView.setText(currentDish.getName());

        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(currentDish.getDescription());

        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        priceTextView.setText(String.valueOf(currentDish.getPrice()));


        return convertView;
    }
    /*
    public int getDrawableResIdByName(String resName) {
        String pkgName = context.getPackageName();
        return context.getResources().getIdentifier(resName, "drawable", pkgName);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_dish, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imageView);
            viewHolder.nameTextView = convertView.findViewById(R.id.nameTextView);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            viewHolder.priceTextView = convertView.findViewById(R.id.priceTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Dish currentDish = dishes.get(position);

        viewHolder.img.setImageResource(getDrawableResIdByName(currentDish.getImage()));
        viewHolder.nameTextView.setText(currentDish.getName());
        viewHolder.descriptionTextView.setText(currentDish.getDescription());
        viewHolder.priceTextView.setText(String.valueOf(currentDish.getPrice()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof AppCompatActivity) {
                    Intent intent = new Intent(context, Dish_Detail.class);
                    intent.putExtra("dish_id", currentDish.getDishId());
                    intent.putExtra("dish_name", currentDish.getName());
                    intent.putExtra("dish_gia", currentDish.getPrice());
                    intent.putExtra("dish_mota", currentDish.getDescription());
                    intent.putExtra("dish_image", currentDish.getImage());

                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
    }*/
    public int getDrawableResIdByName(String resName) {
        String pkgName = context.getPackageName();
        return context.getResources().getIdentifier(resName, "drawable", pkgName);
    }

}
