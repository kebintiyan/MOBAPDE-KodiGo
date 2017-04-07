package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by kevin on 2/27/2017.
 */

public class HTMLImageHandler implements Html.ImageGetter {
    Context context;
    InputStream imageStream;
    public HTMLImageHandler(Context context){
        this.context = context;
    }
    @Override
    public Drawable getDrawable(String source) {
        try{
            imageStream = context.getContentResolver().openInputStream(Uri.parse(source));
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            Bitmap scaled = Bitmap.createScaledBitmap(image, 48*19, 48*14, true);

            BitmapDrawable bd = new BitmapDrawable(Resources.getSystem(), scaled);
            bd.setBounds(0, 0, bd.getIntrinsicWidth(),
                    bd.getIntrinsicHeight());
            imageStream.close();
            Log.i("tapos","tapos");
            return bd;
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }
}
