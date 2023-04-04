package shakti.shakti_employee.fragment;

/**
 * Created by shakti on 10/3/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import adapter.Adapter_report_list;
import adapter.PageAdapter1;
import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;


public class HODTripApprovalFragment extends Fragment implements View.OnClickListener {

    Context context;
    RecyclerView recyclerView;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    Adapter_report_list adapterEmployeeList;
    TabLayout tabLayout;
    //Toolbar toolbar;
    ViewPager viewPager;
    PageAdapter1 pageAdapter;
    TabItem tabApproved;
    TabItem tabComplete;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManagerSubCategory;


    public HODTripApprovalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        onclick = this;


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dom_report1, container, false);

        DatabaseHelper db = new DatabaseHelper(getActivity());

        tabLayout = rootView.findViewById(R.id.tablayout);
        tabComplete = rootView.findViewById(R.id.tabComplete);
        tabApproved = rootView.findViewById(R.id.tabApproved);
        viewPager = rootView.findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter1(getFragmentManager(), context, tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    tabLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((Activity) context).getWindow().setStatusBarColor(ContextCompat.getColor(context,
                                R.color.colorAccent));
                    }
                } else {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((Activity) context).getWindow().setStatusBarColor(ContextCompat.getColor(context,
                                R.color.colorPrimaryDark));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {

    }

}