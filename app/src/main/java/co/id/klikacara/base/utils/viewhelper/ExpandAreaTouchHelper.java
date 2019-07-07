package co.id.klikacara.base.utils.viewhelper;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dell on 6/21/2017.
 */

public class ExpandAreaTouchHelper {

    public static void expandSingleTouchArea(final View view, final int... paddings) {
        final ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.post(() -> {
                final Rect hitRect = new Rect();
                view.getHitRect(hitRect);
                for (int padding : paddings) {
                    hitRect.left -= padding;
                    hitRect.top -= padding;
                    hitRect.right += padding;
                    hitRect.bottom += padding;
                }
                parent.setTouchDelegate(new TouchDelegate(hitRect, view));
            });
        }
    }

    public static void expandSingleTouchArea(final View view, final int padding) {
        final ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.post(() -> {
                final Rect hitRect = new Rect();
                view.getHitRect(hitRect);
                hitRect.left -= padding;
                hitRect.top -= padding;
                hitRect.right += padding;
                hitRect.bottom += padding;

                parent.setTouchDelegate(new TouchDelegate(hitRect, view));
            });
        }
    }
}
