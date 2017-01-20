package my.org.haluan.tarbiyahdzatiyah;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import my.org.haluan.tarbiyahdzatiyah.application.PublicApplication;

/**
 * Created by Ismi on 1/20/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public PublicApplication mPublicApplication;
    public FirebaseApp mApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        mPublicApplication = (PublicApplication) getApplication();
        mApp = FirebaseApp.getInstance();
    }

    protected abstract @LayoutRes
    int getLayout();

    public String getUid(){
        return FirebaseAuth.getInstance(mApp).getCurrentUser().getUid();
    }
}
