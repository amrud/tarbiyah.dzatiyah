package my.org.haluan.tarbiyahdzatiyah.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.org.haluan.tarbiyahdzatiyah.MaduDetails;
import my.org.haluan.tarbiyahdzatiyah.R;
import my.org.haluan.tarbiyahdzatiyah.models.Masul;
import my.org.haluan.tarbiyahdzatiyah.models.User;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.MasulsViewHolder;

/**
 * Created by Ismi on 1/20/2017.
 */

public class MasulFragment extends BaseFragment implements ValueEventListener {

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Masul, MasulsViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mMasulRefs;
    private Query mMasulQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragmentmasul_listRecyclerView);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getMasuls();

        return v;
    }

    private void getMasuls(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mMasulRefs = rootRef.child("MasulList").child(getUid()).child("masulList");
        mMasulRefs.addValueEventListener(this);
        mMasulQuery = mMasulRefs.limitToLast(50);
    }

    public static MasulFragment newInstance(){
        return  new MasulFragment();
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
        mAdapter = new FirebaseRecyclerAdapter<Masul, MasulsViewHolder>(
                Masul.class, R.layout.masuls_list_item, MasulsViewHolder.class, mMasulQuery) {

            @Override
            public void populateViewHolder(final MasulsViewHolder viewHolder, final Masul masul, final int position) {
                viewHolder.setTitle(masul.masul.name);
                viewHolder.setStatus(masul.status);
                if (masul.status.equals(ConstVar.MASUL_STATUS_REQUESTED)){
                    viewHolder.setStatusStyle(Color.BLACK, Color.YELLOW);
                }else if (masul.status.equals(ConstVar.MASUL_STATUS_REJECTED)){
                    viewHolder.setStatusStyle(Color.WHITE, Color.RED);
                }else{
                    viewHolder.setStatusVisibility(View.INVISIBLE);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (masul.status.equals(ConstVar.MASUL_STATUS_REQUESTED)){
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Accept Request");
                            alertDialog.setMessage(masul.masul.name + " has added you as mad\'u");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance(mApp).getReference().child("MasulList").child(getUid()).child("masulList").child(masul.uid).child("status").setValue(ConstVar.MASUL_STATUS_ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                viewHolder.setStatusVisibility(View.INVISIBLE);
                                                viewHolder.setStatus(ConstVar.MASUL_STATUS_ACCEPTED);
                                            }
                                        }
                                    });

                                    FirebaseDatabase.getInstance(mApp).getReference().child("MaduList").child(masul.masul.uid).child("maduList").child(getUid()).child("status").setValue(ConstVar.MADU_STATUS_ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance(mApp).getReference().child("MasulList").child(getUid()).child("masulList").child(masul.uid).child("status").setValue(ConstVar.MASUL_STATUS_REJECTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                viewHolder.setStatus(ConstVar.MASUL_STATUS_REJECTED);
                                                viewHolder.setStatusStyle(Color.WHITE, Color.RED);
                                            }
                                        }
                                    });

                                    FirebaseDatabase.getInstance(mApp).getReference().child("MaduList").child(masul.masul.uid).child("maduList").child(getUid()).child("status").setValue(ConstVar.MADU_STATUS_REJECTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
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

    private void showPromptToAccept(String name, final String uid){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Accept Request");
        alertDialog.setMessage(name + " has added you as mad\'u");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance(mApp).getReference().child("MasulList").child(getUid()).child("masulList").child(uid).child("status").setValue(ConstVar.MASUL_STATUS_ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_masul;
    }

    @Override
    public int getName() {
        return R.string.user_masul;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
