package my.org.haluan.tarbiyahdzatiyah.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.R;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AddMaduViewHolder extends RecyclerView.ViewHolder{

    private View mView;
    private TextView tvTitle, tvStatus;

    public AddMaduViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvTitle = (TextView)mView.findViewById(R.id.madu_name);
        tvStatus = (TextView)mView.findViewById(R.id.madu_status);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setStatus(String status){
        tvStatus.setText(status);
    }

    public void setStatusStyle(int textColor, int backgroundColor){
        tvStatus.setTextColor(textColor);
        tvStatus.setBackgroundColor(backgroundColor);
    }

    public void setStatusOnClickListener(View.OnClickListener listener){
        this.tvStatus.setOnClickListener(listener);
    }
    public void setStatusVisibility(int visibility){
        tvStatus.setVisibility(visibility);
    }
}
