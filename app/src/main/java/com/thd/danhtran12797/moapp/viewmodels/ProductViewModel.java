package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.repositories.ProductRepository;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;

    public ProductViewModel(){
        productRepository = new ProductRepository();
    }

    public LiveData<Product> getProduct(String id){
        return productRepository.getProduct(id);
    }

    public LiveData<String> updateProduct(String id_pro, String name_pro, String image_pro, String price,
                                          String quantity, String spec, String material, String thickness,
                                          String width, String length, String color, String adh_force, String elas,
                                          String charac, String unit, String bearing, String exp_date){

        return productRepository.updateProduct(id_pro, name_pro, image_pro, price, quantity, spec,
                material, thickness, width, length, color,
                adh_force, elas, charac, unit, bearing, exp_date);
    }

    public LiveData<String> deleteProduct(String productId){
        return productRepository.deleteProduct(productId);
    }

}
