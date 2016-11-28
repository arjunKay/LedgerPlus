package com.svco.ledgerplus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragment=new ArrayList<>();
    private ArrayList<String> tabtitle=new ArrayList<>();

    void addFragments(Fragment fragments, String tabtitles){
        this.fragment.add(fragments);
        this.tabtitle.add(tabtitles);
    }

    ViewPagerAdapter(android.support.v4.app.FragmentManager fm){

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return fragment.get(position);

    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle.get(position);
    }
}
