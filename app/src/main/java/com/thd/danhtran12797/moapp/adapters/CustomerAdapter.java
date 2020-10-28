package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.CustomerRowBinding;
import com.thd.danhtran12797.moapp.models.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ListAdapter<Customer, CustomerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private CustomerInterface customerInterface;

    public CustomerAdapter(CustomerInterface customerInterface) {
        super(Customer.itemCallback);
        this.customerInterface = customerInterface;
    }

    public void addAll(List<Customer> lstCustomer) {
        ArrayList<Customer> arrCustomer = new ArrayList<>(getCurrentList());
        arrCustomer.addAll(lstCustomer);
        submitList(arrCustomer);
    }

    public void resetListCustomer() {
        List<Customer> lst = new ArrayList<>();
        submitList(lst);
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        CustomerRowBinding customerRowBinding = CustomerRowBinding.inflate(layoutInflater, parent, false);
        customerRowBinding.setCustomerInterface(customerInterface);
        return new ViewHolder(customerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomerRowBinding rowCustomerBinding;

        public ViewHolder(@NonNull CustomerRowBinding binding) {
            super(binding.getRoot());

            this.rowCustomerBinding = binding;
        }

        public void bind(Customer customer) {
            rowCustomerBinding.setCustomer(customer);
            rowCustomerBinding.executePendingBindings();
        }
    }

    public interface CustomerInterface {
        void onItemClick(Customer customer);
    }
}
