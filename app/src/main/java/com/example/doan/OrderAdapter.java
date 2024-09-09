package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private ArrayList<Order> orders;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        }

        Order currentOrder = orders.get(position);

        TextView orderID = convertView.findViewById(R.id.tvOrderId);
        orderID.setText(currentOrder.getOrderid());

        TextView customerID = convertView.findViewById(R.id.tvCustomerID_O);
        customerID.setText(String.valueOf(currentOrder.getCustomerid()));

        TextView dishID = convertView.findViewById(R.id.tvDishID_O);
        dishID.setText(currentOrder.getDishid());

        TextView quantity = convertView.findViewById(R.id.tvquatity_O);
        quantity.setText(String.valueOf(currentOrder.getQuantity()));

        TextView date = convertView.findViewById(R.id.tvDate_O);
        date.setText(currentOrder.getOrderdate());

        TextView price = convertView.findViewById(R.id.tvprice_O);
        price.setText(String.valueOf(currentOrder.getTotal_price()));

        return convertView;
    }
}
