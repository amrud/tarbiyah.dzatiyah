package my.org.haluan.tarbiyahdzatiyah.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.models.Amal;

/**
 * Created by Ismi on 1/20/2017.
 */

public class PredefinedAmalsViewHolder extends RecyclerView.ViewHolder{

    private View mView;
    private TextView mtvTitle, mtvDesc, mtvType;
    public CheckBox mcbCheck;

    public PredefinedAmalsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mtvTitle = (TextView)itemView.findViewById(R.id.preamal_title);
        //mtvDesc = (TextView)itemView.findViewById(R.id.preamal_description);
        //mtvType = (TextView)itemView.findViewById(R.id.preamal_type);
        mcbCheck = (CheckBox)itemView.findViewById(R.id.preamal_checkbox);
        this.setIsRecyclable(false);
    }

    public void setAmalDetails(String title, String desc, String type){
        this.mtvTitle.setText(title);
        //this.mtvDesc.setText(desc);
       // this.mtvType.setText(type);
    }
}
