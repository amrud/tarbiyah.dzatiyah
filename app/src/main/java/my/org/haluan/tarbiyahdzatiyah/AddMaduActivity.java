package my.org.haluan.tarbiyahdzatiyah;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.models.Amal;
import my.org.haluan.tarbiyahdzatiyah.models.Amals;
import my.org.haluan.tarbiyahdzatiyah.models.Madu;
import my.org.haluan.tarbiyahdzatiyah.models.Masul;
import my.org.haluan.tarbiyahdzatiyah.models.User;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.AddMaduViewHolder;
import my.org.haluan.tarbiyahdzatiyah.views.AmalsViewHolder;

/**
 * Created by Ismi on 1/20/2017.
 */

public class AddMaduActivity extends BaseActivity implements ValueEventListener{

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<User, AddMaduViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mUserRefs;
    private Query mUserQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidgets();

        getUsers();
    }

    private void initWidgets(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.addmadu_listRecyclerView);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getUsers(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mUserRefs = rootRef.child("users");
        mUserRefs.keepSynced(true);
        mUserRefs.addValueEventListener(this);
        mUserQuery = mUserRefs.limitToLast(50);
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
        mAdapter = new FirebaseRecyclerAdapter<User, AddMaduViewHolder>(
                User.class, R.layout.addmadu_list_item, AddMaduViewHolder.class, mUserQuery) {

            @Override
            public void populateViewHolder(final AddMaduViewHolder viewHolder, final User user, final int position) {
                viewHolder.setTitle(user.name);

                if(user.uid.equals(getUid())){

                }
                viewHolder.setStatus("Add");
                viewHolder.setStatusStyle(Color.WHITE, Color.GREEN);
                viewHolder.setStatusOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setStatus("Requesting");
                        viewHolder.setStatusStyle(Color.BLACK, Color.YELLOW);

                        Madu madu = new Madu();
                        madu.madu = user;
                        madu.status = ConstVar.MADU_STATUS_PENDING;
                        FirebaseDatabase.getInstance(mApp).getReference().child("MaduList").child(getUid()).child("maduList").child(madu.madu.uid).setValue(madu).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    viewHolder.setStatus(ConstVar.MADU_STATUS_PENDING);
                                }
                            }
                        });

                        String masulKey = FirebaseDatabase.getInstance(mApp).getReference().child("MasulList").child(madu.madu.uid).child("masulList").push().getKey();
                        Masul masul = new Masul();
                        masul.masul = mPublicApplication.user;
                        masul.status = ConstVar.MASUL_STATUS_REQUESTED;
                        masul.uid = masulKey;
                        FirebaseDatabase.getInstance(mApp).getReference().child("MasulList").child(madu.madu.uid).child("masulList").child(masulKey).setValue(masul).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });

                    }
                });
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
    protected int getLayout() {
        return R.layout.activity_addmadu;
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
