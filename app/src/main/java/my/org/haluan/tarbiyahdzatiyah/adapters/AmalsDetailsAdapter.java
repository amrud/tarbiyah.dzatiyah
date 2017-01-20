package my.org.haluan.tarbiyahdzatiyah.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.models.Amal;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.AmalsListItems;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AmalsDetailsAdapter extends BaseAdapter {

    private List<Amal> mAmalList = new ArrayList<>();
    private Context context;

    public AmalsDetailsAdapter(Context ctx){
        this.context = ctx;
    }

    public void updateList(List<Amal> newList) {
        this.mAmalList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAmalList.size();
    }

    @Override
    public Amal getItem(int position) {
        return mAmalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AmalsListItems ali;

        if (convertView == null){
            ali = (AmalsListItems)View.inflate(context, R.layout.listitems_amaldetails, null);
        }else{
            ali = (AmalsListItems) convertView;
        }

        final Amal amal = getItem(position);

        ali.setType(amal.type);
         String title = amal.title;
        if (amal.type.equals(ConstVar.AMAL_TYPE_BOOLEAN)){
            ali.tvBoolTitle.setText(title);
            String flag = amal.details == null || amal.details.isEmpty() ? "false" : amal.details;
            if (flag.equals("true")){
                ali.cbBool.setChecked(true);
            }else{
                ali.cbBool.setChecked(false);
            }
        }else if (amal.type.equals(ConstVar.AMAL_TYPE_NUMBER)){
            ali.tvNumTitle.setText(title);
        }else if(amal.type.equals(ConstVar.AMAL_TYPE_TEXT)){
            ali.tvTextTitle.setText(title);
        }

        ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ali.cbBool.toggle();
                if (ali.cbBool.isChecked()){
                    amal.details = "true";
                }else{
                    amal.details = "false";
                }
            }
        });

        ali.cbBool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ali.cbBool.isChecked()){
                    amal.details = "true";
                }else{
                    amal.details = "false";
                }
            }
        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                amal.details = s.toString();
            }
        };

        ali.etText.addTextChangedListener(tw);
        ali.etNum.addTextChangedListener(tw);

        return ali;
    }
}
