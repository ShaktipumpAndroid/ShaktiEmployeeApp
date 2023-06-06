package shakti.shakti_employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.VendorListModel;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.ImageModel;

public class VendorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VendorListModel.Response> vendorList;
    Context mcontext;
    private VendorListAdapter.ImageSelectionListener vendorSelectionListener;

    public VendorListAdapter(Context contact_, List<VendorListModel.Response> listdata) {
        this.vendorList = listdata;
        this.mcontext = contact_;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.vendorlistitem, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder
            , @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof VendorListAdapter.ViewHolder) {
            VendorListAdapter.ViewHolder viewHolder = (VendorListAdapter.ViewHolder) holder;

            final VendorListModel.Response imageModel = vendorList.get(position);

            viewHolder.vendorCode.setText(imageModel.getLifnr());

            viewHolder.vendorName.setText(imageModel.getName1());

            viewHolder.cardView.setOnClickListener(v -> vendorSelectionListener.VendorSelectionListener(imageModel,position));
        }
    }


    @Override
    public int getItemCount() {
        return vendorList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView vendorCode,vendorName;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            vendorCode = itemView.findViewById(R.id.vendorCode);
            vendorName = itemView.findViewById(R.id.vendorName);
            cardView = itemView.findViewById(R.id.vendorCard);
        }
    }
    public void VendorSelection(VendorListAdapter.ImageSelectionListener actDocList) {
        try {
            vendorSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface ImageSelectionListener {
        void VendorSelectionListener(VendorListModel.Response vendorList,int position);
    }
}
