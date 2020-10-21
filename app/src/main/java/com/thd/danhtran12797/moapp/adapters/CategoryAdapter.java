package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.AddCategoryRowBinding;
import com.thd.danhtran12797.moapp.databinding.CategoryRowBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.utils.ScreenUtils;

public class CategoryAdapter extends ListAdapter<Category, RecyclerView.ViewHolder> {

    private static final int CATEGORY_TYPE = 1;
    private static final int ADD_CATE_TYPE = 2;

    private CategoryInterface categoryInterface;
    private String groupId;

    public void setGroupId(String groupId){
        this.groupId = groupId;
    }

    public CategoryAdapter(CategoryInterface categoryInterface) {
        super(Category.itemCallback);
        this.categoryInterface = categoryInterface;
    }

    // submit()

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ADD_CATE_TYPE: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                AddCategoryRowBinding addCategoryRowBinding = AddCategoryRowBinding.inflate(layoutInflater, parent, false);
                return new AddCateViewModel(addCategoryRowBinding);
            }
            case CATEGORY_TYPE: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                CategoryRowBinding categoryRowBinding = CategoryRowBinding.inflate(layoutInflater, parent, false);
                return new CategoryViewHolder(categoryRowBinding);
            }
            default: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                CategoryRowBinding categoryRowBinding = CategoryRowBinding.inflate(layoutInflater, parent, false);
                return new CategoryViewHolder(categoryRowBinding);
            }
        }
    }

        @Override
        public void onBindViewHolder (@NonNull RecyclerView.ViewHolder viewHolder, int position){
            if (getItemViewType(position) != ADD_CATE_TYPE){
                ((CategoryViewHolder)viewHolder).bind(getItem(position));
            }
        }

        @Override
        public int getItemViewType ( int position){
            if (getItem(position).isAddCate())
                return ADD_CATE_TYPE;
            return CATEGORY_TYPE;
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder {

            CategoryRowBinding categoryRowBinding;

            public CategoryViewHolder(CategoryRowBinding categoryRowBinding) {
                super(categoryRowBinding.getRoot());

                this.categoryRowBinding = categoryRowBinding;

                int size = ScreenUtils.getInstance().getWidth() / 3;
                itemView.setLayoutParams(new CardView.LayoutParams(size, size));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        categoryInterface.onItemClick(getItem(getAdapterPosition()), groupId);
                    }
                });
            }

            public void bind(Category category) {
                categoryRowBinding.setCategory(category);
            }
        }

        public class AddCateViewModel extends RecyclerView.ViewHolder {
            AddCategoryRowBinding addCategoryRowBinding;

            public AddCateViewModel(AddCategoryRowBinding addCategoryRowBinding) {
                super(addCategoryRowBinding.getRoot());
                this.addCategoryRowBinding = addCategoryRowBinding;

//                int size = ScreenUtils.getInstance().getWidth() / 3;
//                itemView.setLayoutParams(new CardView.LayoutParams(size, size));

                addCategoryRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        categoryInterface.onAddItemClick(groupId);
                    }
                });
            }
        }

        public interface CategoryInterface {
            void onItemClick(Category category, String groupId);
            void onAddItemClick(String groupId);
        }

    }
