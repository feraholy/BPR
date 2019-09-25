package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BottomPageAdapter extends FragmentPagerAdapter {
    private int numOfTab;

    BottomPageAdapter(FragmentManager fm,int numOfTab) {
        super(fm);
        this.numOfTab=numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
        {
            return new MyCareerFragment();
        }
        else if(i==1)
        {
            return new HomeFragment();
        }
        else if(i==2)
        {
            return new ProfileFragment();
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
