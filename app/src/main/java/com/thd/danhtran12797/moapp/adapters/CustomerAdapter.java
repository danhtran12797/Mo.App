package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.CustomerRowBinding;
import com.thd.danhtran12797.moapp.models.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ListAdapter<Customer, CustomerAdapter.ViewHolder> implements Filterable {

    List<Customer> customersListAll;
    CustomerAllInterface customerAllInterface;
    OnItemClickListener itemClickListener;

    public CustomerAdapter(List<Customer> customersListAll, CustomerAllInterface customerAllInterface) {
        super(Customer.itemCallback);
        this.customerAllInterface = customerAllInterface;
        this.customersListAll = new ArrayList<>(customersListAll);
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CustomerRowBinding customerRowBinding=CustomerRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(customerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = getItem(position);
        holder.rowCustomerBinding.setCustomer(customer);
        holder.rowCustomerBinding.executePendingBindings();
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
            customerAllInterface.update((List<Customer>) filterResults.values);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomerRowBinding rowCustomerBinding;

        public ViewHolder(@NonNull CustomerRowBinding  binding) {
            super(binding.getRoot());

            this.rowCustomerBinding=binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =getAdapterPosition();
                    if(itemClickListener!=null&&position!=RecyclerView.NO_POSITION){
                        itemClickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface CustomerAllInterface {
        void update(List<Customer> customers);
    }
}
