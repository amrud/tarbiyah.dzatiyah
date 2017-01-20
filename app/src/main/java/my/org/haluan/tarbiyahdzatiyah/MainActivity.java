package my.org.haluan.tarbiyahdzatiyah;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import my.org.haluan.tarbiyahdzatiyah.models.MaduList;
import my.org.haluan.tarbiyahdzatiyah.models.User;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;
import my.org.haluan.tarbiyahdzatiyah.views.AmalsViewHolder;
import my.org.haluan.tarbiyahdzatiyah.views.ContentListAmal;

public class MainActivity extends BaseActivity implements ValueEventListener{

    private final int MENU_PROFILE = 1;
    private final int MENU_SETTING = 2;
    private final int MENU_LOGOUT = 3;

    private MenuItem item;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Amals, AmalsViewHolder> mAdapter;
    private LinearLayoutManager mLayoutManager;

    /*Firebase Database*/
    private DatabaseReference mAmalsRefs;
    private Query mAmalsQueury;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getUpdatedProfile();

        initWidgets();
        getAmals();
    }

    private void initWidgets() {
        FloatingActionButton fabAddAmal = (FloatingActionButton)findViewById(R.id.fabAddAmal);
        fabAddAmal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddAmalActivity.class));
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.amals_listRecyclerView);
        //use this setting to improve performance. Currently content doesn't change the layout size of recycler view
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getAmals(){
       DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        mAmalsRefs = rootRef.child("AmalList").child(getUid());
        mAmalsRefs.keepSynced(true);
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
                    ContentListAmal tvAmal = (ContentListAmal) View.inflate(MainActivity.this, R.layout.content_listamal, null);
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

                amalsView.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent amalsDetails = new Intent(MainActivity.this, AmalsDetailsActivity.class);
                        amalsDetails.putExtra("amals", mAdapter.getItem(position));
                        startActivity(amalsDetails);
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
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_simple, menu);
        int dp = (int) (getResources().getDimension(R.dimen.profile_dimension) / getResources().getDisplayMetrics().density);
        menu.findItem(R.id.action_profile).setIcon(ConstVar.resizeImage(this, R.drawable.empty_profile, dp,dp));
        return true;
    }

    private void InitProfile(){
        if (item != null) {
            SubMenu sm = item.getSubMenu();
            sm.clear();//delete everything first;

            if(mPublicApplication.user!= null) {
                sm.add(0, MENU_PROFILE, Menu.NONE, mPublicApplication.user.name).setIcon(ConstVar.resizeImage(this, R.drawable.empty_profile, 100, 100));
            }
            sm.add(0,MENU_LOGOUT, Menu.NONE,getString(R.string.logout));

        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        InitProfile();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        item = menu.findItem(R.id.action_profile);
        InitProfile();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PROFILE:
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                break;
            case MENU_SETTING:

                break;
            case MENU_LOGOUT:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.logout_message)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance(mApp).signOut();
                                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(login);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUpdatedProfile() {
        final String userId = getUid();
        FirebaseDatabase.getInstance(mApp).getReference("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    mPublicApplication.user = user;
                }catch (Exception ex){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void insertPredefinedAmals(){
        String[] amalTitle = new String[]{"Solat Subuh Berjemaah",
        "Solat Zohor Berjemaah", "Solat Asar Berjemaah", "Solat Maghrib Berjemaah", "Solat Isyak Berjemaah",
        "Solat Sunat Rawatib", "Solat Sunat Dhuha","Tahajud/Qiyamulail", "Solat Sunat Taubat", "Solat Sunat Witir", "Tilawah Al-Quran",
        "Tadabbur Al-Quran", "Hafalan Al-Quran", "Hafalan Hadith", "Bacaan Surah Al-Mulk", "Al-Mathurat Pagi",
        "Al-Mathurat Petang", "Zikir Harian Imam Al-Ghazali", "Istighfar (100x)", "Selawat (50x)", "Syahadah", "Puasa Sunat", "Sedekah/Infaq",
        "Baca Buku Ilmiah 5m/s", "Hadir Majlis Ilmu", "Birrul Walidain","Muhasabah Diri","Ziarah","Memaafkan Orang Lain", "Bersangka Baik (Husnuzhon)"};
        for(int i = 0; i < amalTitle.length; i++) {
            String key = FirebaseDatabase.getInstance(mApp).getReference().child("PredefinedAmals").push().getKey();
            Amal amal = new Amal();
            amal.dateCreated = ConstVar.getNowDateTime();
            amal.title = amalTitle[i];
            amal.description = "";
            amal.type = ConstVar.AMAL_TYPE_BOOLEAN;
            amal.uid = key;

            FirebaseDatabase.getInstance(mApp).getReference().child("PredefinedAmals").child(key).setValue(amal).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        }
    }


    private void insertMaduList(){

        Madu madu = new Madu();
        madu.madu = mPublicApplication.user;
        madu.status = ConstVar.MADU_STATUS_ACCEPTED;

        MaduList maduList= new MaduList();
        maduList.maduList = new ArrayList<>();
        maduList.maduList.add(madu);
        madu.status = ConstVar.MADU_STATUS_PENDING;
        maduList.maduList.add(madu);
        FirebaseDatabase.getInstance(mApp).getReference().child("MaduList").child(getUid()).setValue(maduList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });
    }
}
