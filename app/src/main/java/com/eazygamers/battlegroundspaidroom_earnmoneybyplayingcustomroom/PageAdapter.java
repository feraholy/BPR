package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    PageAdapter(FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
        {
            return new PlayFragment();
        }
        else if(i==1)
        {
            return new LiveFragment();
        }
        else if(i==2)
        {
            return new ResultFragment();
        }return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
