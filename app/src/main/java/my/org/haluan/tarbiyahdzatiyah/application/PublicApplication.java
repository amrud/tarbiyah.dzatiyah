package my.org.haluan.tarbiyahdzatiyah.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import my.org.haluan.tarbiyahdzatiyah.models.User;

/**
 * Created by Ismi on 1/20/2017.
 */

public class PublicApplication extends Application{

    public User user;

    @Override
    public void onCreate(){
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
