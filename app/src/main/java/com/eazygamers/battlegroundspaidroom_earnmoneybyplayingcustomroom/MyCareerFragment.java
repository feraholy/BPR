package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCareerFragment extends Fragment {
    private TabLayout careerTab;
    private ViewPager viewPagerCareer;

    private int[] tabIcons = {
            R.drawable.ic_upcoming,
            R.drawable.ic_played,

    };

    public MyCareerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_my_career, container, false);
        careerTab=view.findViewById(R.id.careerTab);
        viewPagerCareer=view.findViewById(R.id.viewPagerCareer);
        initialize();
        return view;
    }
    private void initialize() {
        CareerPageAdapter pageAdapter = new CareerPageAdapter(getFragmentManager(), careerTab.getTabCount());
        viewPagerCareer.setAdapter(pageAdapter);
        setUpTabIcon();
        careerTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerCareer.setCurrentItem(tab.getPosition());
                System.out.println(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPagerCareer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(careerTab));
    }
    private void setUpTabIcon()
    {
        Objects.requireNonNull(careerTab.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(careerTab.getTabAt(1)).setIcon(tabIcons[1]);
    }

}
