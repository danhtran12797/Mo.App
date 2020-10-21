package com.thd.danhtran12797.moapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Group;
import com.thd.danhtran12797.moapp.repositories.GroupRepository;

import java.util.ArrayList;
import java.util.List;

public class GroupViewModel extends ViewModel {

    private GroupRepository groupRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public GroupViewModel() {
        Log.d("AAA", "Constructor GroupViewModel: ");
        groupRepository = new GroupRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

//    public void loadGroups() {
//        List<Group> lstGroup = groupRepository.getGroups().getValue();
//        Log.d("AAA", "loadGroups: HELLO");
//        List<Group> customerList = new ArrayList<>();
//        groupMutableLiveData.setValue(customerList);
//    }

    public LiveData<List<Group>> getGroups() {
        return groupRepository.getGroups();
    }

    public LiveData<String> insertGroup(String nameGroup) {
        return groupRepository.insertGroup(nameGroup);
    }

}
