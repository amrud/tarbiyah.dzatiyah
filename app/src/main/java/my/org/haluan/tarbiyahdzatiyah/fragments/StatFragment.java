package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.org.haluan.tarbiyahdzatiyah.R;

/**
 * Created by Ismi on 1/20/2017.
 */

public class StatFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =super.onCreateView(inflater, container, savedInstanceState);

        return v;
    }

    public static StatFragment newInstance(){
        return new StatFragment();
    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_stat;
    }

    @Override
    public int getName() {
        return R.string.user_stat;
    }
}
