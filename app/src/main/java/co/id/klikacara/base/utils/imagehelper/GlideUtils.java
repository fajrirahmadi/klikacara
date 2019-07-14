package co.id.klikacara.base.utils.imagehelper;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import androidx.appcompat.widget.AppCompatImageView;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class GlideUtils {

    public static void setImage(Context context, String path, ImageView imageView) {
        if (context instanceof Activity)
            if (!((Activity) context).isDestroyed())
                GlideApp.with(context)
                        .load(path)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(imageView);
    }

    public static void setFoto(Context context, File uri, ImageView imageView) {
        GlideApp.with(context)
                .load(uri.getAbsoluteFile())
                .centerCrop()
                .into(imageView);
    }

    public static void setFotoWithUrl(Context context, Uri uri, ImageView imageView) {
        GlideApp.with(context)
                .load(uri)
                .centerCrop()
                .into(imageView);
    }

    public static void setFotoWithUrl(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    public static void setFotoCircle(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform())
                .into(imageView);
    }

    public static void setFotoCircle(Context context, int url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform())
                .into(imageView);
    }

    public static void setFotoCircleFromStorage(Context context,
                                                String url,
                                                AppCompatImageView imageView) {
        GlideApp.with(context)
                .load(FirebaseStorage.getInstance().getReference().child(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform())
                .into(imageView);
    }

    public static void setFotoFromStorage(Context context,
                                          String url,
                                          AppCompatImageView imageView) {
        GlideApp.with(context)
                .load(FirebaseStorage.getInstance().getReference().child(url))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
