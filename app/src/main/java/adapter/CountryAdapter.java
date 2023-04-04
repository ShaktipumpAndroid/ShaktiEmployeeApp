package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import shakti.shakti_employee.R;


public class CountryAdapter extends BaseAdapter {
    Context mcontext;
    ArrayList<String> CountryName;


    public CountryAdapter(Context context, ArrayList<String> countryName) {
        this.mcontext = context;
        this.CountryName = countryName;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return CountryName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            ViewHolder vh = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(mcontext);
            rowView = inflator.inflate(R.layout.city_state_listitem, parent, false);
            vh.txtview = (TextView) rowView.findViewById(R.id.textview);

            rowView.setTag(vh);


        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.txtview.setText(CountryName.get(position));

        return rowView;
    }


    public static class ViewHolder {
        TextView txtview;
    }
}
