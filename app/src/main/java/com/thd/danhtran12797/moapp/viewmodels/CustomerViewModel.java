package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Customer;
import com.thd.danhtran12797.moapp.repositories.CustomerRepository;

import java.util.List;

public class CustomerViewModel extends ViewModel {
    private CustomerRepository customerRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public CustomerViewModel() {
        customerRepository = new CustomerRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<String> getTotalPageCus(String search_name){
        return customerRepository.getTotalPageCus(search_name);
    }

    public LiveData<List<Customer>> getCustomers(String search_name, int page) {
        return customerRepository.getCustomers(search_name, page);
    }

    public LiveData<String> insertCustomer(String name, String phone, String address, String classCus){
        return customerRepository.insertCustomer(name, phone, address, classCus);
    }

    public LiveData<String> updateCustomer(String id, String name, String phone, String address, String classCus){
        return customerRepository.updateCustomer(id , name, phone, address, classCus);
    }

    public LiveData<String> deleteCustomer(String id) {
        return customerRepository.deleteCustomer(id);
    }
}
