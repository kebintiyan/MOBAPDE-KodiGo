package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLImageHandler implements Html.ImageGetter {
    @Override
    public Drawable getDrawable(String source) {
        Log.i("sourcez", source+"");
        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/20170403_170255.jpg");
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 48*5, 48*5, true);

        BitmapDrawable bd = new BitmapDrawable(Resources.getSystem(), scaled);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(),
                bd.getIntrinsicHeight());
        return bd;
    }
}
