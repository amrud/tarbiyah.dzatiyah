package my.org.haluan.tarbiyahdzatiyah;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.org.haluan.tarbiyahdzatiyah.models.Amal;
import my.org.haluan.tarbiyahdzatiyah.models.Amals;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.PredefinedAmalsViewHolder;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AddAmalActivity extends BaseActivity implements ValueEventListener {

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Amal, PredefinedAmalsViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mPredefinedAmalsRefs;
    private Query mPredefinedAmalsQueury;

    private EditText etDate;
    private Button btnToday;
    private Calendar myCalendar = Calendar.getInstance();

    private HashMap<String, Amal> selectedAmals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWidgets();
        getPredefinedAmals();
    }

    private void initWidgets(){
        selectedAmals = new HashMap<>();

        etDate = (EditText)findViewById(R.id.addamal_date);
        btnToday = (Button)findViewById(R.id.addamal_btnDateToday);
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDate.setText(ConstVar.getToday());
            }
        });

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                etDate.setText(dayOfMonth+"-"+monthOfYear+1+"-"+year);
            }

        };
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAmalActivity.this, dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView)findViewById(R.id.addamal_PredefinedAmalList);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.amal_details, menu);
        return true;
    }

    private void getPredefinedAmals(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mPredefinedAmalsRefs = rootRef.child("PredefinedAmals");
        mPredefinedAmalsRefs.addValueEventListener(this);
        mPredefinedAmalsQueury = mPredefinedAmalsRefs.limitToLast(50);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAdapter != null){
            mAdapter.cleanup();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
    }

    private void attachRecyclerViewAdapter() {
        mAdapter = new FirebaseRecyclerAdapter<Amal, PredefinedAmalsViewHolder>(
                Amal.class, R.layout.predefined_amal_list_item, PredefinedAmalsViewHolder.class, mPredefinedAmalsQueury) {
            private ArrayList<Integer> checkedIndices = new ArrayList<Integer>();


            @Override
            public void populateViewHolder(final PredefinedAmalsViewHolder viewHolder, final Amal amal, final int position) {
                viewHolder.setAmalDetails(amal.title, amal.description, amal.type);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.mcbCheck.toggle();
                    }
                });
                viewHolder.mcbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            checkedIndices.add(position);
                            if (!selectedAmals.containsKey(amal.uid))selectedAmals.put(amal.uid, amal);
                        }else{
                            checkedIndices.remove(position);
                            if(selectedAmals.containsKey(amal.uid))selectedAmals.remove(amal.uid);
                        }
                    }
                });

                if (checkedIndices.contains(position)){
                    viewHolder.mcbCheck.setChecked(true);
                }else{
                    viewHolder.mcbCheck.setChecked(false);
                }
            }
        };

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
//                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.amal_save:
                addAmaltoCloud();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_addamal;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void addAmaltoCloud(){

        final Amals amals = new Amals();
        amals.dateCreated = ConstVar.getNowDateTime();
        amals.date = etDate.getText().toString();
        amals.uid = etDate.getText().toString();
        amals.amalList = new ArrayList<>(selectedAmals.values());

        FirebaseDatabase.getInstance(mApp).getReference().child("AmalList").child(getUid()).child(amals.uid).setValue(amals).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    AddAmalActivity.this.finish();
                }else{
                    Toast.makeText(AddAmalActivity.this, "Error Adding Amal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
