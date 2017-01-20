package my.org.haluan.tarbiyahdzatiyah.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ismi on 1/20/2017.*/

@IgnoreExtraProperties
public class Amals implements Serializable{
    public String uid;
    public List<Amal> amalList;
    public String date;
    public String dateCreated;

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("amalList", amalList);
        result.put("date", date);
        result.put("dateCreated",dateCreated);
        return result;
    }
}
