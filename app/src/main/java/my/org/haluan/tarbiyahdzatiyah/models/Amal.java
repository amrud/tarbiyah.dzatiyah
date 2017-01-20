package my.org.haluan.tarbiyahdzatiyah.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ismi on 1/20/2017.
 */
@IgnoreExtraProperties
public class Amal implements Serializable{

    public String title;
    public String type;
    public String description;
    public String details;
    public String dateCreated;
    public String uid;


    public Amal(){}

    public Amal(String title, String type, String description, String details, String dateCreated){
        this.title = title;
        this.type = type;
        this.description = description;
        this.details = details;
        this.dateCreated = dateCreated;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("type", description);
        result.put("details", details);
        result.put("dateCreated",dateCreated);
        return result;
    }

}
