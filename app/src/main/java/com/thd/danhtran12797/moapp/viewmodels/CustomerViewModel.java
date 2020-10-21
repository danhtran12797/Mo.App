package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerViewModel extends ViewModel {
    private MutableLiveData<List<Customer>> mutableLiveData;

    public LiveData<List<Customer>> getCustomerList() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            initCustomerList();
        }
        return mutableLiveData;
    }

    private void initCustomerList() {
        List<Customer> arrayList = new ArrayList<>();
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Bùi Thị P", "0325907305", "12/7 đường 32 phường Lê Duẩn", "Lao", "199"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Nguyễn Văn D", "0328367312", "99/100 đường 32 phường Không Tên", "Indonesia", "54"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Đặng Công L", "0328367312", "324/11 đường 32 phường Bình Chánh", "Đông Timor", "96"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Trần Hùng Danh", "0328363212", "44/09 đường 32 phường 32", "Đông Timor", "26"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Trần Thị N", "0334367362", "345 đường 32 phường 21", "Singapore", "100"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Đặng Văn B", "096355398", "56/1 đường Số 6 phường Hiệp Bình", "Campuchia", "40"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer(UUID.randomUUID().toString(), "Nguyễn thị O", "0368367392", "231 đường 32 phường Hiệp Bình", "Philippines", "56"));
        mutableLiveData.setValue(arrayList);
    }

    public void addCustomer(Customer customer) {
        if (mutableLiveData != null) {
            List<Customer> customerList = new ArrayList<>(mutableLiveData.getValue());
            customerList.add(customer);
            mutableLiveData.setValue(customerList);
        }
    }

    public void update(Customer customer, int position) {
        if (mutableLiveData != null) {
            List<Customer> customerList = new ArrayList<>(mutableLiveData.getValue());
            customerList.remove(position);
            customerList.add(position, customer);
            mutableLiveData.setValue(customerList);
        }
    }

    public void update(List<Customer> lst) {
        if (mutableLiveData != null) {
            List<Customer> customerList = new ArrayList<>(lst);
            mutableLiveData.setValue(customerList);
        }
    }
}
