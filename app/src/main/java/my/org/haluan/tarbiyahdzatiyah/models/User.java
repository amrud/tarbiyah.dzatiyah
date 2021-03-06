package my.org.haluan.tarbiyahdzatiyah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Ismi on 1/20/2017.
 */
@IgnoreExtraProperties
public class User implements Serializable{
    public String name;
    public String mobile;
    public String email;
    public String uid;

    public User(){
        this.mobile = null;
    }

    public User(String uid, String name, String mobile, String email){
        this.uid = uid;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }
}
