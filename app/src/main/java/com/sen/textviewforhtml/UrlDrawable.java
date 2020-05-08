package com.sen.textviewforhtml;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UrlDrawable extends BitmapDrawable implements Drawable.Callback{
    public Drawable drawable;

    @SuppressWarnings("deprecation")
    public UrlDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawable != null)
            drawable.draw(canvas);
    }

    public Drawable getDrawable() {
        return drawable;
    }
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }
    @Override
    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
