package com.thd.danhtran12797.moapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.ProductRowBinding;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.utils.ScreenUtils;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductViewHolder> {

    private LayoutInflater layoutInflater;
    private ProductInterface productInterface;

    public ProductAdapter(ProductInterface productInterface) {
        super(Product.itemCallback);
        this.productInterface = productInterface;

        Log.d("BBB", "ProductAdapter: ");
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        ProductRowBinding productRowBinding=ProductRowBinding.inflate(layoutInflater,parent, false);
        productRowBinding.setProductInterface(productInterface);

        Log.d("BBB", "onCreateViewHolder: ");
        return new ProductViewHolder(productRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        holder.bind(getItem(position));
        Log.d("BBB", "onBindViewHolder: ");
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductRowBinding productRowBinding;

        public ProductViewHolder(ProductRowBinding productRowBinding) {
            super(productRowBinding.getRoot());

            this.productRowBinding=productRowBinding;

            int size = ScreenUtils.getInstance().getWidth() / 2;
            productRowBinding.getRoot().setLayoutParams(new CardView.LayoutParams(size, size));
            Log.d("BBB", "ProductViewHolder: ");
        }

        public void bind(Product product) {
            Log.d("BBB", "bind: ");
            productRowBinding.setProduct(product);
            productRowBinding.executePendingBindings();
        }
    }

    public interface ProductInterface {
        void onItemClick(Product product);
    }
}
