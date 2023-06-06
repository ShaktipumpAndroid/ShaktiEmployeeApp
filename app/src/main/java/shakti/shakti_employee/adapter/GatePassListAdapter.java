package shakti.shakti_employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import shakti.shakti_employee.R;
import shakti.shakti_employee.model.GatePassModel;

public class GatePassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GatePassModel.Response> gatePassList;
    Context mcontext;

    private int prevSelection = -1;
    private GatePassSelectionListener gatePassSelectionListener;

    public GatePassListAdapter(Context contact_, List<GatePassModel.Response> listdata) {
        this.gatePassList = listdata;
        this.mcontext = contact_;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.gatepasslistitem, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder
            , @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof GatePassListAdapter.ViewHolder) {
            GatePassListAdapter.ViewHolder viewHolder = (GatePassListAdapter.ViewHolder) holder;

            final GatePassModel.Response gatePasModel = gatePassList.get(position);

            viewHolder.vendorCode.setText(gatePasModel.getDocno());

            viewHolder.vendorName.setText(gatePasModel.getNameVisitor());

            if (gatePasModel.isChecked()) {
                viewHolder.checkbox.setChecked(true);
                prevSelection = position;
            } else {
                viewHolder.checkbox.setChecked(false);
            }

          //  viewHolder.cardView.setOnClickListener(v -> gatePassSelectionListener.GatePassSelecListener(gatePasModel,position));

            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        gatePasModel.setChecked(true);
                        if (prevSelection >= 0) {
                            gatePassList.get(prevSelection).setChecked(false);
                            notifyItemChanged(prevSelection);
                            gatePassSelectionListener.GatePassSelecListener(gatePasModel);
                        }
                        prevSelection = position;
                    } else {
                        gatePasModel.setChecked(false);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return gatePassList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView vendorCode,vendorName;
        CardView cardView;
        AppCompatCheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);

            vendorCode = itemView.findViewById(R.id.vendorCode);
            vendorName = itemView.findViewById(R.id.vendorName);
            cardView = itemView.findViewById(R.id.vendorCard);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
    public void GatePassSelection(GatePassSelectionListener actDocList) {
        try {
            gatePassSelectionListener = actDocList;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface GatePassSelectionListener {
        void GatePassSelecListener(GatePassModel.Response gatePassList);
    }
}
