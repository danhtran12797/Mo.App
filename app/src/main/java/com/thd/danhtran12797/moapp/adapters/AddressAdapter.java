package com.thd.danhtran12797.moapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.databinding.SelectAddressRowBinding;
import com.thd.danhtran12797.moapp.models.AddressDetail;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends ListAdapter<AddressDetail, AddressAdapter.ViewHolder> implements Filterable {

    List<AddressDetail> addressListAll;
//    AddressAllInterface addressAllInterface;
    AddressInterface addressInterface;
    LayoutInflater layoutInflater;

    public AddressAdapter(AddressInterface addressInterface) {
        super(AddressDetail.itemCallback);
        this.addressInterface=addressInterface;
    }

    public void setAddressListAll(List<AddressDetail> addressListAll){
        this.addressListAll=new ArrayList<>(addressListAll);
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null)
            layoutInflater=LayoutInflater.from(parent.getContext());
        SelectAddressRowBinding addressRowBinding=SelectAddressRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(addressRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<AddressDetail> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(addressListAll);
            } else {
                for (AddressDetail addressDetail : addressListAll) {
                    if (addressDetail.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase().trim())) {
                        filteredList.add(addressDetail);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            addressAllInterface.update((List<AddressDetail>) filterResults.values);
            submitList((List<AddressDetail>)filterResults.values);
        }
    };


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectAddressRowBinding addressRowBinding;
        public ViewHolder(SelectAddressRowBinding addressRowBinding) {
            super(addressRowBinding.getRoot());

            this.addressRowBinding=addressRowBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressInterface.onItemClick(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(AddressDetail addressDetail) {
            addressRowBinding.txtItemNameAddressDetail.setText(addressDetail.getTitle());
        }

    }

    public interface AddressInterface{
        void onItemClick(AddressDetail addressDetail);
    }

    public interface AddressAllInterface {
        void update(List<AddressDetail> customers);
    }
}
