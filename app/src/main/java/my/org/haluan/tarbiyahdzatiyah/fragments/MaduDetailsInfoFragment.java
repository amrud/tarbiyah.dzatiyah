package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.org.haluan.tarbiyahdzatiyah.R;

/**
 * Created by Ismi on 1/20/2017.
 */

public class MaduDetailsInfoFragment extends BaseFragment {
    private String mMaduUid;
    public static MaduDetailsInfoFragment newInstance(String uid){
        MaduDetailsInfoFragment fragment = new MaduDetailsInfoFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMaduUid = getArguments().getString("uid");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_madudetailsinfo;
    }

    @Override
    public int getName() {
        return R.string.madudetails_info;
    }
}
