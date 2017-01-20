package my.org.haluan.tarbiyahdzatiyah.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.text.format.DateUtils;

import com.facebook.drawee.drawable.RoundedBitmapDrawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ismi on 1/20/2017.
 */

public class ConstVar{
    public static String TAG = "haluan.tarbiyahdzatiyah";

    public static String MADU_STATUS_ACCEPTED = "Approved";
    public static String MADU_STATUS_PENDING = "Pending";
    public static String MADU_STATUS_REJECTED = "Rejected";

    public static String MASUL_STATUS_REQUESTED = "Confirm";
    public static String MASUL_STATUS_ACCEPTED = "Success";
    public static String MASUL_STATUS_REJECTED = "Rejected";

    public static String AMAL_TYPE_NUMBER = "numeric";
    public static String AMAL_TYPE_BOOLEAN = "bool";
    public static String AMAL_TYPE_TEXT = "text";

    public static String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
    public static String getYesterday(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String currentDateandTime = sdf.format(cal.getTime());
        return currentDateandTime;
    }

    public static String getNowDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static RoundedBitmapDrawable resizeImage(Context context, int resId, int w, int h)
    {
        // load the origial Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, width, height), new RectF(0, 0, w, h), Matrix.ScaleToFit.FILL);

        RoundedBitmapDrawable d = new RoundedBitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, true));
        d.setCircle(true);
        return d;
    }
}
