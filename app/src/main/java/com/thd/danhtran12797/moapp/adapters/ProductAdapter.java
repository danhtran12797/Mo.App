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

import java.util.ArrayList;
import java.util.List;

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

    public void addAll(List<Product> lstProduct) {
        ArrayList<Product> arrProduct=new ArrayList<>(getCurrentList());
        arrProduct.addAll(lstProduct);
        submitList(arrProduct);
    }

    public void resetListProduct() {
        List<Product> lst = new ArrayList<>();
        submitList(lst);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductRowBinding productRowBinding;

        public ProductViewHolder(ProductRowBinding productRowBinding) {
            super(productRowBinding.getRoot());

            this.productRowBinding=productRowBinding;

            int size = ScreenUtils.getInstance().getWidth() / 2;
            productRowBinding.getRoot().setLayoutParams(new CardView.LayoutParams(size, size));
        }

        public void bind(Product product) {
            Log.d("AAA", "bind: SIZE: "+product.getImageDetail().size());
            productRowBinding.setProduct(product);
            productRowBinding.executePendingBindings();
        }
    }

    public interface ProductInterface {
        void onItemClick(Product product);
    }
}
