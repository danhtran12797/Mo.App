package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.databinding.ImageDetailRowBinding;
import com.thd.danhtran12797.moapp.models.ImageDetail;

import java.util.List;

public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ViewHolder> {

    private List<ImageDetail> lstImageDetail;
    private LayoutInflater layoutInflater;
    private ImageDetailInterface imageDetailInterface;
    private int posPress = 0;

    public ImageDetailAdapter(ImageDetailInterface imageDetailInterface) {
        this.imageDetailInterface = imageDetailInterface;
    }

    public void setLstImageDetail(List<ImageDetail> lstImageDetail) {
        this.lstImageDetail = lstImageDetail;
    }

    public String getIdImageDetail() {
        return lstImageDetail.get(posPress).getId();
    }

    public void setPosPress(int pos) {
        posPress = pos;
        notifyDataSetChanged();
    }

    public void setPosPress1(int pos) {
        posPress = pos;
    }

    @NonNull
    @Override
    public ImageDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        ImageDetailRowBinding imageDetailRowBinding = ImageDetailRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(imageDetailRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDetailAdapter.ViewHolder holder, int position) {
        holder.bind(lstImageDetail.get(position));
    }

    @Override
    public int getItemCount() {
        return lstImageDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageDetailRowBinding imageDetailRowBinding;

        public ViewHolder(ImageDetailRowBinding imageDetailRowBinding) {
            super(imageDetailRowBinding.getRoot());
            this.imageDetailRowBinding = imageDetailRowBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    posPress = getAdapterPosition();
                    imageDetailInterface.onItemClick(lstImageDetail.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        public void bind(ImageDetail imageDetail) {
            imageDetailRowBinding.setImageDetail(imageDetail);
            if (posPress != getAdapterPosition()) {
                imageDetailRowBinding.imageDetailLayout.setBackgroundResource(R.drawable.custome_background_image_detail);
            } else
                imageDetailRowBinding.imageDetailLayout.setBackgroundResource(R.drawable.custome_background_image_detail_press);
        }
    }

    public interface ImageDetailInterface {
        void onItemClick(ImageDetail imageDetail, int pos);
    }
}
