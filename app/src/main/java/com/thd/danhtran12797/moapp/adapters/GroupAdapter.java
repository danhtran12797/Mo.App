package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.adapters.CategoryAdapter.CategoryInterface;
import com.thd.danhtran12797.moapp.databinding.GroupRowBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Group;

import java.util.List;

public class GroupAdapter extends ListAdapter<Group, GroupAdapter.GroupViewHolder> {

    private LayoutInflater layoutInflater;
    private GroupInterface groupInterface;
    private CategoryInterface categoryInterface;

    public GroupAdapter(GroupInterface groupInterface, CategoryInterface categoryInterface) {
        super(Group.itemCallback);
        this.groupInterface = groupInterface;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        GroupRowBinding groupRowBinding = GroupRowBinding.inflate(layoutInflater, parent, false);
        groupRowBinding.setGroupInterface(groupInterface);
        return new GroupViewHolder(groupRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        GroupRowBinding groupRowBinding;

        public GroupViewHolder(GroupRowBinding groupRowBinding) {
            super(groupRowBinding.getRoot());

            this.groupRowBinding = groupRowBinding;
        }

        public void bind(Group group) {
            if (group.getCategories().size() >= 6)
                groupRowBinding.seeMoreImageView.setVisibility(View.VISIBLE);
            groupRowBinding.setGroup(group);
            setCategoryItemRecycler(groupRowBinding.categoryRecyclerView, group.getCategories());
            groupRowBinding.executePendingBindings();
        }

        public void setCategoryItemRecycler(RecyclerView recyclerView, List<Category> categoryItemList) {
            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryInterface);
            categoryAdapter.setGroupId(getItem(getAdapterPosition()).getId());
            recyclerView.setAdapter(categoryAdapter);
            if (categoryItemList.size() < 6) {
                Category category = new Category();
                category.setAddCate(true);
                categoryItemList.add(category);
            }
            categoryAdapter.submitList(categoryItemList);
        }
    }

    public interface GroupInterface {
        void onItemClick(Group group);
    }
}
