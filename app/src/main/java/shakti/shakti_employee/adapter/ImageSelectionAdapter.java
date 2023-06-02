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

import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.ImageModel;

public class ImageSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageModel> imageModelList;
    Context mcontext;
    private ImageSelectionListener docSelectionListener;

    public ImageSelectionAdapter(Context contact_, List<ImageModel> listdata) {
        this.imageModelList = listdata;
        this.mcontext = contact_;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.imagelistitem, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder
            , @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            final ImageModel imageModel = imageModelList.get(position);

            ((ViewHolder) holder).title.setText(imageModel.getName());

            if(imageModel.isImageSelected()){
                ((ViewHolder) holder).image.setImageResource(R.drawable.right_mark_icn_green);
            }else {
                ((ViewHolder) holder).image.setImageResource(R.drawable.red_icn);
            }

            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    docSelectionListener.ImageSelectionListener(imageModel,position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return imageModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
    public void ImageSelection(ImageSelectionListener actDocList) {
        try {
            docSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface ImageSelectionListener {
        void ImageSelectionListener(ImageModel imageModelList,int position);
    }
}
