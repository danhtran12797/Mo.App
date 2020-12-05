package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.AddProductRowBinding;
import com.thd.danhtran12797.moapp.databinding.ProductRowBinding;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.utils.ImageResizer;
import com.thd.danhtran12797.moapp.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ListAdapter<Product, RecyclerView.ViewHolder> {

    private static final int PRODUCT_TYPE = 1;
    private static final int ADD_PRODUCT_TYPE = 2;

    private ProductInterface productInterface;
    private String cateId;

    public void setCategoryId(String cateId) {
        this.cateId = cateId;
    }

    public ProductAdapter(ProductInterface productInterface) {
        super(Product.itemCallback);
        this.productInterface = productInterface;
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

    // submit()

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ADD_PRODUCT_TYPE: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                AddProductRowBinding addProductRowBinding = AddProductRowBinding.inflate(layoutInflater, parent, false);
                return new AddProductViewModel(addProductRowBinding);
            }
            case PRODUCT_TYPE: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ProductRowBinding productRowBinding = ProductRowBinding.inflate(layoutInflater, parent, false);
                return new ProductViewHolder(productRowBinding);
            }
            default: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ProductRowBinding productRowBinding = ProductRowBinding.inflate(layoutInflater, parent, false);
                return new ProductViewHolder(productRowBinding);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) != ADD_PRODUCT_TYPE) {
            ((ProductViewHolder) viewHolder).bind(getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isAddPro())
            return ADD_PRODUCT_TYPE;
        return PRODUCT_TYPE;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductRowBinding productRowBinding;

        public ProductViewHolder(ProductRowBinding productRowBinding) {
            super(productRowBinding.getRoot());

            this.productRowBinding = productRowBinding;

            int size = ScreenUtils.getInstance().getWidth() / 3;
            itemView.setLayoutParams(new CardView.LayoutParams(size, size + ScreenUtils.getInstance().dpToPx(30)));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productInterface.onItemClick(getItem(getAdapterPosition()).getId());
                }
            });
        }

        public void bind(Product product) {
            productRowBinding.setProduct(product);
        }
    }

    public class AddProductViewModel extends RecyclerView.ViewHolder {
        AddProductRowBinding addProductRowBinding;

        public AddProductViewModel(AddProductRowBinding addProductRowBinding) {
            super(addProductRowBinding.getRoot());
            this.addProductRowBinding = addProductRowBinding;

//                int size = ScreenUtils.getInstance().getWidth() / 3;
//                itemView.setLayoutParams(new CardView.LayoutParams(size, size));

            addProductRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productInterface.onAddItemClick(cateId);
                }
            });
        }
    }

    public interface ProductInterface {
        void onItemClick(String id_pro);

        void onAddItemClick(String id_cate);
    }

}
