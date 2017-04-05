package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by kevin on 3/26/2017.
 */

public class ImageSpanHandler extends ImageSpan {
    Context context;
    Uri imageURI;
    public ImageSpanHandler(Context context, Uri imageURI) {
        super(context, imageURI);
        this.context = context;
        this.imageURI = imageURI;
    }

    @Override
    public Drawable getDrawable() {
        try {
            Log.i("urii", imageURI.getPath());
            InputStream imageStream = context.getContentResolver().openInputStream(imageURI);
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            Bitmap scaled = Bitmap.createScaledBitmap(image, 48 * 5, 48 * 5, true);
            BitmapDrawable bd = new BitmapDrawable(context.getResources(), scaled);
            bd.setBounds(0, 0, bd.getIntrinsicWidth(),
                    bd.getIntrinsicHeight());
            imageStream.close();
            return bd;
        } catch (Exception e) {

        }
        return super.getDrawable();
    }

}
