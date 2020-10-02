package com.thd.danhtran12797.moapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    ArrayList<Customer> customerArrayList;

    public CustomerAdapter(ArrayList<Customer> customerArrayList) {
        this.customerArrayList = customerArrayList;
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_customer, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = customerArrayList.get(position);

//        holder.kilomet.setText("("+customer.getKilomet()+" km)");
        holder.name.setText(customer.getName());
        holder.phone.setText(customer.getPhone());
        holder.classify.setText(customer.getClassify());
        holder.address.setText(customer.getAddress());
    }

    @Override
    public int getItemCount() {
        return customerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView phone;
        TextView kilomet;
        TextView classify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_text_view);
            address = itemView.findViewById(R.id.address_text_view);
            phone = itemView.findViewById(R.id.phone_text_view);
            classify = itemView.findViewById(R.id.cassify_text_view);
            kilomet = itemView.findViewById(R.id.kilomet_text_view);
        }
    }
}
