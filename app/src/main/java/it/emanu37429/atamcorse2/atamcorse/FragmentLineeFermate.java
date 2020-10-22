package it.emanu37429.atamcorse2.atamcorse;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import it.emanu37429.atamcorse2.R;

public class FragmentLineeFermate extends Fragment {


    public FragmentLineeFermate() {    }

    private static final int NUM_PAGES = 2;
    String[] title = new String[]{"Linee", "Fermate"};
    Fragment[] fr = new Fragment[] {new FragmentLinee(), new FragmentPaline()};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_linee_fermate, container, false);
        ViewPager mPager = v.findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new LFSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = v.findViewById(R.id.sliding_tabs_tr);
        tabLayout.setupWithViewPager(mPager);
        return v;
    }

    private class LFSlidePagerAdapter extends FragmentPagerAdapter {
        public LFSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fr[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }
}
