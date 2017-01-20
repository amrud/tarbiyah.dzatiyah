package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.org.haluan.tarbiyahdzatiyah.MaduDetails;
import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.models.Madu;
import my.org.haluan.tarbiyahdzatiyah.models.User;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.MadusViewHolder;
import my.org.haluan.tarbiyahdzatiyah.views.MasulsViewHolder;

/**
 * Created by Ismi on 1/20/2017.
 */

public class MaduFragment extends BaseFragment implements ValueEventListener {

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Madu, MadusViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mMaduRefs;
    private Query mMaduQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragmentmadu_listRecyclerView);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getMadu();

        return v;
    }

    private void getMadu(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mMaduRefs = rootRef.child("MaduList").child(getUid()).child("maduList");
        mMaduRefs.addValueEventListener(this);
        mMaduQuery = mMaduRefs.limitToLast(50);
    }

    public static MaduFragment newInstance(){
        return new MaduFragment();
    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_madu;
    }

    @Override
    public int getName() {
        return R.string.user_madu;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
        mAdapter = new FirebaseRecyclerAdapter<Madu, MadusViewHolder>(
                Madu.class, R.layout.madus_list_item, MadusViewHolder.class, mMaduQuery) {

            @Override
            public void populateViewHolder(MadusViewHolder viewHolder, final Madu madu, final int position) {
                viewHolder.setTitle(madu.madu.name);
                viewHolder.setStatus(madu.status);
                if (madu.status.equals(ConstVar.MADU_STATUS_PENDING)){
                    viewHolder.setStatusStyle(Color.BLACK, Color.YELLOW);
                }else if (madu.status.equals(ConstVar.MADU_STATUS_REJECTED)){
                    viewHolder.setStatusStyle(Color.WHITE, Color.RED);
                }else{
                    viewHolder.setStatus(madu.madu.email);
                    viewHolder.setStatusStyle(Color.BLACK, Color.WHITE);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (madu.status.equals(ConstVar.MADU_STATUS_ACCEPTED)){
                            Intent maduDetails = new Intent(getActivity(), MaduDetails.class);
                            maduDetails.putExtra("madu", mAdapter.getItem(position));
                            startActivity(maduDetails);
                        }else{
                            Toast.makeText(getActivity(), "Unable to view mad'u profile", Toast.LENGTH_SHORT).show();
                        }
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
}
