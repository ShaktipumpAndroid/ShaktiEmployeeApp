package adapter;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import shakti.shakti_employee.fragment.TripApprovedFragment;
import shakti.shakti_employee.fragment.TripCompltedFragment;
import shakti.shakti_employee.fragment.TripRequestEnteredFragment;

/**
 * Created by abdalla on 2/18/18.
 */

public class PageAdapter extends FragmentPagerAdapter {

    Context context;
    private int numOfTabs;

    public PageAdapter(FragmentManager fm, Context context, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new TripRequestEnteredFragment();
            case 1:

                return new TripCompltedFragment();
            case 2:

                return new TripApprovedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
