package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Ismi on 1/20/2017.
 */

public abstract class BaseFragment extends Fragment {

    public FirebaseApp mApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(getLayout(), container, false);

        mApp = FirebaseApp.getInstance();
        return view;
    }

    protected abstract @LayoutRes
    int getLayout();

    public abstract int getName();

    public String getUid(){
        return FirebaseAuth.getInstance(mApp).getCurrentUser().getUid();
    }
}
