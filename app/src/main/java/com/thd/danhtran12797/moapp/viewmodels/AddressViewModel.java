package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.AddressDetail;
import com.thd.danhtran12797.moapp.models.ListProvin;
import com.thd.danhtran12797.moapp.repositories.AddressRepository;

import java.util.List;

public class AddressViewModel extends ViewModel {
    private AddressRepository addressRepository;

    public AddressViewModel() {
        addressRepository=new AddressRepository();
    }

    public LiveData<ListProvin> getAllProvin() {
        return addressRepository.getAllProvin();
    }

    public LiveData<List<AddressDetail>> getAllDistrict(int id) {
        return addressRepository.getAllDistrict(id);
    }

    public LiveData<List<AddressDetail>> getAllWard(int id) {
        return addressRepository.getAllWard(id);
    }
}
