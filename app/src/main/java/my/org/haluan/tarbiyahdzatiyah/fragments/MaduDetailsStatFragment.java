package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.org.haluan.tarbiyahdzatiyah.MainActivity;
import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.models.Amal;
import my.org.haluan.tarbiyahdzatiyah.models.Amals;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.AmalsViewHolder;
import my.org.haluan.tarbiyahdzatiyah.views.ContentListAmal;

/**
 * Created by Ismi on 1/20/2017.
 */

public class MaduDetailsStatFragment extends BaseFragment implements ValueEventListener {

    private String mMaduUid;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Amals, AmalsViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mAmalsRefs;
    private Query mAmalsQueury;

    public static MaduDetailsStatFragment newInstance(String uid){
        MaduDetailsStatFragment fragment = new MaduDetailsStatFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mMaduUid = getArguments().getString("uid");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.madudetailstat_listRecyclerView);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getAmals();

        return view;
    }

    private void getAmals(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mAmalsRefs = rootRef.child("AmalList").child(mMaduUid);
        mAmalsRefs.addValueEventListener(this);
        mAmalsQueury = mAmalsRefs.limitToLast(50);
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
        mAdapter = new FirebaseRecyclerAdapter<Amals, AmalsViewHolder>(
                Amals.class, R.layout.amals_list_item, AmalsViewHolder.class, mAmalsQueury) {

            @Override
            public void populateViewHolder(AmalsViewHolder amalsView, final Amals amals, final int position) {
                amalsView.setTitle(amals.date);

                List<ContentListAmal> tvAmals = new ArrayList<>();
                for (Amal amal: amals.amalList) {
                    ContentListAmal tvAmal = (ContentListAmal) View.inflate(getActivity(), R.layout.content_listamal, null);
                    tvAmal.setTitle(amal.title);
                    if (amal.type.equals(ConstVar.AMAL_TYPE_BOOLEAN)) {
                        boolean flag = amal.details != null && !amal.details.isEmpty() && amal.details.equals("true") ? true : false;
                        if (flag) {
                            tvAmal.setImage(R.drawable.checkcircle);
                        }else{
                            tvAmal.setImage(R.drawable.checkboxblankcircleoutline);
                        }
                    }
                    tvAmals.add(tvAmal);
                }
                amalsView.setAmalList(tvAmals);

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
        return R.layout.fragment_madudetailsstat;
    }

    @Override
    public int getName() {
        return R.string.madudetails_amal;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
