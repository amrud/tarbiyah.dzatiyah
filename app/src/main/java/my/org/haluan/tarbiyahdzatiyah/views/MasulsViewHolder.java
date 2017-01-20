package my.org.haluan.tarbiyahdzatiyah.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import my.org.haluan.tarbiyahdzatiyah.R;

/**
 * Created by Ismi on 1/21/2017.
 */

public class MasulsViewHolder extends RecyclerView.ViewHolder{

    private View mView;
    private TextView tvTitle, tvStatus;

    public MasulsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvTitle = (TextView)mView.findViewById(R.id.masul_name);
        tvStatus = (TextView)mView.findViewById(R.id.masul_status);
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

    public void setStatusVisibility(int visibility){
        tvStatus.setVisibility(visibility);
    }
}
