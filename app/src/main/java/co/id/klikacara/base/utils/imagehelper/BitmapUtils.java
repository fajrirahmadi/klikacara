package co.id.klikacara.base.utils.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

public class BitmapUtils {

    public static Bitmap getBitmap(Context context, int color, int icon) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), icon, null);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable != null ? drawable.getIntrinsicWidth() : 0,
                drawable != null ? drawable.getIntrinsicHeight() : 0, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        //set round background on lolipop+ and square before
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(ContextCompat.getColor(context, color));
            canvas.drawCircle(canvas.getHeight() / 2, canvas.getHeight() / 2, canvas.getHeight() / 2, p);
        } else {
            canvas.drawColor(ContextCompat.getColor(context, color));
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }
}
