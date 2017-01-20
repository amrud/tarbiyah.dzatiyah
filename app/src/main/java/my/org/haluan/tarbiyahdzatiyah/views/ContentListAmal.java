package my.org.haluan.tarbiyahdzatiyah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import my.org.haluan.tarbiyahdzatiyah.R;

/**
 * Created by Ismi on 1/21/2017.
 */

public class ContentListAmal extends RelativeLayout {

    private TextView tvTitle;
    private ImageView imageViewIcon;

    public ContentListAmal(Context context) {
        super(context);
    }

    public ContentListAmal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTitle = (TextView) findViewById(R.id.cla_tvTitle);
        imageViewIcon = (ImageView)findViewById(R.id.cla_checkbox);
        imageViewIcon.setImageResource(R.drawable.checkboxblankcircleoutline);
    }

    public void setTitle(String title){
        if(title != null )this.tvTitle.setText(title);
    }

    public void setImage(int resId){
        this.imageViewIcon.setImageResource(resId);
    }
}
