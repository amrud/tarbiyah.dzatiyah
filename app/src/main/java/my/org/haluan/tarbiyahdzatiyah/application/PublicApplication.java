package my.org.haluan.tarbiyahdzatiyah.application;

import android.app.Application;

import my.org.haluan.tarbiyahdzatiyah.models.User;

/**
 * Created by Ismi on 1/20/2017.
 */

public class PublicApplication extends Application{

    public User user;

    @Override
    public void onCreate(){
        super.onCreate();
    }
}
