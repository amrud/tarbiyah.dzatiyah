package my.org.haluan.tarbiyahdzatiyah.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.fragments.BaseFragment;

/**
 * Created by Ismi on 1/21/2017.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<BaseFragment> fragments;

    public SectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        fragments = new ArrayList<>();
    }

    public void addFragment(BaseFragment fragment){
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mContext.getString(fragments.get(position).getName());
    }
}
