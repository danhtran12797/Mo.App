package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.databinding.ImageDetail2RowBinding;
import com.thd.danhtran12797.moapp.models.ImageDetail;
import com.thd.danhtran12797.moapp.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.BASE_IMAGE_PRO_URL;

public class ImageDetail2Adapter extends RecyclerView.Adapter<ImageDetail2Adapter.ViewHolder> {

    private List<ImageDetail> lstImageDetail;
    private LayoutInflater layoutInflater;
    private boolean type = false;

    public ImageDetail2Adapter(boolean type) {
        this.lstImageDetail = new ArrayList<>();
        this.type = type;
    }

    public void setLstImageDetail(List<ImageDetail> lstImageDetail) {
        this.lstImageDetail = lstImageDetail;
    }

    @NonNull
    @Override
    public ImageDetail2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        ImageDetail2RowBinding imageDetail2RowBinding = ImageDetail2RowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(imageDetail2RowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDetail2Adapter.ViewHolder holder, int position) {
        holder.bind(lstImageDetail.get(position));
    }

    @Override
    public int getItemCount() {
        return lstImageDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageDetail2RowBinding imageDetail2RowBinding;

        public ViewHolder(ImageDetail2RowBinding imageDetail2RowBinding) {
            super(imageDetail2RowBinding.getRoot());
            this.imageDetail2RowBinding = imageDetail2RowBinding;

            if (type) {
                int size = ScreenUtils.getInstance().dpToPx(30);
                itemView.setLayoutParams(new CardView.LayoutParams(size, size));
            }
        }

        public void bind(ImageDetail imageDetail) {
            imageDetail2RowBinding.setImageDetail(imageDetail);
        }
    }
}
