package adapter;

import android.app.Activity;
import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import shakti.shakti_employee.fragment.HODTripApprovedFragment;
import shakti.shakti_employee.fragment.HODTripPendingFragment;

/**
 * Created by abdalla on 2/18/18.
 */

public class PageAdapter1 extends FragmentPagerAdapter {

    Context context;
    private int numOfTabs;


    public PageAdapter1(FragmentManager fm, Context context, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.context = context;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ((Activity) context).setTitle("Pending Report");
                return new HODTripPendingFragment();
            case 1:
                ((Activity) context).setTitle("Approved Report");
                return new HODTripApprovedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
