package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.CategoryRowBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.adapters.ProductAdapter.ProductInterface;
import com.thd.danhtran12797.moapp.models.Product;

import java.util.List;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    private LayoutInflater layoutInflater;
    private CategoryInterface categoryInterface;
    private ProductInterface productInterface;

    public CategoryAdapter(CategoryInterface categoryInterface, ProductInterface productInterface) {
        super(Category.itemCallback);
        this.categoryInterface = categoryInterface;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        CategoryRowBinding categoryRowBinding = CategoryRowBinding.inflate(layoutInflater, parent, false);
        categoryRowBinding.setCategoryInterface(categoryInterface);
        return new CategoryViewHolder(categoryRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        CategoryRowBinding categoryRowBinding;

        public CategoryViewHolder(CategoryRowBinding categoryRowBinding) {
            super(categoryRowBinding.getRoot());

            this.categoryRowBinding = categoryRowBinding;
        }

        public void bind(Category category) {
            if (category.getProducts().size() >= 6)
                categoryRowBinding.seeMoreImageView.setVisibility(View.VISIBLE);
            categoryRowBinding.setCategory(category);
            setProductItemRecycler(categoryRowBinding.productRecyclerView, category.getProducts());
            categoryRowBinding.executePendingBindings();
        }

        public void setProductItemRecycler(RecyclerView recyclerView, List<Product> productsItemList) {
            ProductAdapter productAdapter = new ProductAdapter(productInterface);
            productAdapter.setCategoryId(getItem(getAdapterPosition()).getId());
            recyclerView.setAdapter(productAdapter);
            if (productsItemList.size() < 6) {
                Product product = new Product();
                product.setAddPro(true);
                productsItemList.add(product);
            }
            productAdapter.submitList(productsItemList);
        }
    }

    public interface CategoryInterface {
        void onItemClick(Category category);
    }
}
