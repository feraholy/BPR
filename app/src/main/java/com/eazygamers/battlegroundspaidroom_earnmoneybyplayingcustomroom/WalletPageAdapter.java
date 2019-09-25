package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WalletPageAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    WalletPageAdapter(@NonNull FragmentManager fm,int numOfTabs)
    {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            return new DepositFragment();
        }
        else
        {
            return new WithdrawFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
