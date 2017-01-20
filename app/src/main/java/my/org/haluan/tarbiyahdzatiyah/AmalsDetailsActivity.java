package my.org.haluan.tarbiyahdzatiyah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import my.org.haluan.tarbiyahdzatiyah.adapters.AmalsDetailsAdapter;
import my.org.haluan.tarbiyahdzatiyah.models.Amals;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AmalsDetailsActivity extends BaseActivity {

    private Amals mCurrentAmals;
    private ListView lvAmalList;
    private AmalsDetailsAdapter adapter;
    private View lvHeader;
    private TextView tvDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mCurrentAmals = (Amals) savedInstanceState.get("amals");
        }else{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mCurrentAmals = (Amals) bundle.get("amals");
        }

        initWidgets();
        initDetails();
    }

    private void initDetails() {
        tvDate.setText(mCurrentAmals.date);
        adapter.updateList(mCurrentAmals.amalList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("amals", mCurrentAmals);
        super.onSaveInstanceState(outState);
    }

    private void initWidgets(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fabSave = (FloatingActionButton)findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance(mApp).getReference().child("AmalList").child(getUid()).child(mCurrentAmals.uid).setValue(mCurrentAmals).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Log.d(ConstVar.TAG, "Failed to save");
                        }else{
                            AmalsDetailsActivity.this.finish();
                        }
                    }
                });
            }
        });

        lvHeader = getLayoutInflater().inflate(R.layout.header_amalsdetails, null);
        tvDate = (TextView)lvHeader.findViewById(R.id.amaldetails_date);

        lvAmalList = (ListView) findViewById(R.id.amalsdetails_listView);
        adapter = new AmalsDetailsAdapter(this);

        lvAmalList.addHeaderView(lvHeader);
        lvAmalList.setAdapter(adapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_amalsdetails;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
