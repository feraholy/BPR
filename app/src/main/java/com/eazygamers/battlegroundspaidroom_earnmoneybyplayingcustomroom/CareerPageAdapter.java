package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CareerPageAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;
    CareerPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
        {
            return new UpcomingFragment();
        }
        else if(i==1)
        {
            return new PlayedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
