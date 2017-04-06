package com.guoyi.gyvideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Credit on 2017/1/15.
 */

public class ViewPageFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> fragments = new ArrayList();

    public ViewPageFragmentAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addFragments(ArrayList<T> fragments) {
        this.fragments.addAll(fragments);
    }

    public void changeFragments(ArrayList<T> fragments) {
        this.fragments.addAll(fragments);
        this.notifyDataSetChanged();
    }

    public void addFragment(T fragment) {
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public T getItem(int paramInt) {
        return this.fragments.get(paramInt);
    }

}
