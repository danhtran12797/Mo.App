package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.repositories.GroupDetailRepository;

import java.util.List;

public class GroupDetailViewModel extends ViewModel {
    private GroupDetailRepository groupDetailRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public GroupDetailViewModel() {
        groupDetailRepository = new GroupDetailRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<List<Category>> getCategories(String groupId) {
        return groupDetailRepository.getCategories(groupId);
    }

    public LiveData<String> updateGroup(String idGroup, String nameGroup) {
        return groupDetailRepository.updateGroup(idGroup, nameGroup);
    }

    public LiveData<String> deleteGroup(String idGroup) {
        return groupDetailRepository.deleteGroup(idGroup);
    }
}
