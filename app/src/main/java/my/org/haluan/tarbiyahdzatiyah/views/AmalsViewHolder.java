package my.org.haluan.tarbiyahdzatiyah.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AmalsViewHolder extends RecyclerView.ViewHolder{

    private View mView;
    private TextView tvTitle;
    private LinearLayout llAmalList;

    public AmalsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvTitle = (TextView)mView.findViewById(R.id.amallist_Title);
        llAmalList = (LinearLayout)mView.findViewById(R.id.amallist_ListAmals);
    }

    public void setTitle(String title){
        if (ConstVar.getToday().equals(title)){
            tvTitle.setText("Today");
        }else if(ConstVar.getYesterday().equals(title)){
            tvTitle.setText("Yesterday");
        }else {
            tvTitle.setText(title);
        }
    }

    public void setAmalList(List<ContentListAmal> amals){
        for (ContentListAmal tvAmal: amals) {
            llAmalList.addView(tvAmal);
        }
    }
}
