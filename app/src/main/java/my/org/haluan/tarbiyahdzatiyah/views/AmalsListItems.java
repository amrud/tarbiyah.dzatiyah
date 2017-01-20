package my.org.haluan.tarbiyahdzatiyah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AmalsListItems extends LinearLayout {

    private LinearLayout llNumeric, llText;
    private RelativeLayout rlBoolean;

    public TextView tvBoolTitle, tvNumTitle, tvTextTitle;
    public EditText etNum, etText;
    public CheckBox cbBool;

    public AmalsListItems(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llNumeric = (LinearLayout)findViewById(R.id.liad_llNumericType);
        llText = (LinearLayout)findViewById(R.id.liad_llTextType);
        rlBoolean = (RelativeLayout)findViewById(R.id.liad_rlBooleanType);

        tvBoolTitle = (TextView)findViewById(R.id.liad_boolTitle);
        tvNumTitle = (TextView)findViewById(R.id.liad_numTitle);
        tvTextTitle = (TextView )findViewById(R.id.liad_textTitle);

        cbBool = (CheckBox) findViewById(R.id.liad_cbFlag);
        etNum = (EditText) findViewById(R.id.liad_etNumber);
        etText = (EditText)findViewById(R.id.liad_etText);

        llNumeric.setVisibility(View.GONE);
        llText.setVisibility(View.GONE);
        rlBoolean.setVisibility(View.GONE);
    }

    public void setType(String type){
        if (type.equals(ConstVar.AMAL_TYPE_TEXT)){
            llText.setVisibility(View.VISIBLE);
        }else if (type.equals(ConstVar.AMAL_TYPE_NUMBER)){
            llNumeric.setVisibility(View.VISIBLE);
        }else if (type.equals(ConstVar.AMAL_TYPE_BOOLEAN)){
            rlBoolean.setVisibility(View.VISIBLE);
        }
    }

}
