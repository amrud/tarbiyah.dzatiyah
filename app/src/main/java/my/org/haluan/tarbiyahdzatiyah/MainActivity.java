package my.org.haluan.tarbiyahdzatiyah;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {

    private final int MENU_PROFILE = 1;
    private final int MENU_SETTING = 2;
    private final int MENU_LOGOUT = 3;

    private MenuItem item;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        menu.findItem(R.id.action_profile).setIcon(resizeImage(R.drawable.empty_profile, dp,dp));
        return true;
    }

    private void InitProfile(){
        if (item != null) {
            SubMenu sm = item.getSubMenu();
            sm.clear();//delete everything first;

            if(mPublicApplication.user!= null) {
                sm.add(0, MENU_PROFILE, Menu.NONE, mPublicApplication.user.name).setIcon(resizeImage(R.drawable.empty_profile, 100, 100));
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

    private RoundedBitmapDrawable resizeImage(int resId, int w, int h)
    {
        // load the origial Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, width, height), new RectF(0, 0, w, h), Matrix.ScaleToFit.FILL);

        RoundedBitmapDrawable d = new RoundedBitmapDrawable(getResources(), Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, true));
        d.setCircle(true);
        return d;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PROFILE:

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
}
