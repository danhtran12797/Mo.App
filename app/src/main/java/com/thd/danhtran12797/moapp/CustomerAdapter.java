package com.thd.danhtran12797.moapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ListAdapter<Customer, CustomerAdapter.ViewHolder> implements Filterable {

    List<Customer> customersListAll;
    CustomerInterface customerInterface;

    public CustomerAdapter(List<Customer> customersListAll, CustomerInterface customerInterface) {
        super(Customer.itemCallback);
        this.customerInterface = customerInterface;
        this.customersListAll = new ArrayList<>(customersListAll);
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = getItem(position);
        holder.bind(customer);
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Customer> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(customersListAll);
            } else {
                for (Customer customer : customersListAll) {
                    if (customer.getName().toLowerCase().contains(charSequence.toString().toLowerCase().trim())) {
                        filteredList.add(customer);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            customerInterface.update((List<Customer>) filterResults.values);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView addressTextView;
        TextView phoneTextView;
        TextView kilometTextView;
        TextView classifyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            addressTextView = itemView.findViewById(R.id.address_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
            classifyTextView = itemView.findViewById(R.id.cassify_text_view);
            kilometTextView = itemView.findViewById(R.id.kilomet_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), InforCustomerActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void bind(Customer customer) {
            nameTextView.setText(customer.getName());
            phoneTextView.setText(customer.getPhone());
            addressTextView.setText(customer.getAddress());
            classifyTextView.setText(customer.getClassify());
            kilometTextView.setText("("+customer.getKilomet() +" km)");
        }
    }

    interface CustomerInterface {
        void update(List<Customer> customers);
    }
}
