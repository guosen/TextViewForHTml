package com.sen.textviewforhtml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import java.util.HashSet;
/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HtmlImageGetter implements Html.ImageGetter {

    private Context mContext;
    private TextView mTvContent;
    private HashSet<GifDrawable> gifDrawables;
    private HashSet<CustomTarget> targets;
    UrlDrawable drawable;

    public HtmlImageGetter(Context context, TextView tv) {
        this.mTvContent = tv;
        this.mContext = context;
        targets = new HashSet<>();
        gifDrawables = new HashSet<>();
    }

    @Override
    public Drawable getDrawable(String url) {
        drawable = new UrlDrawable();

        CustomTarget target;
        RequestBuilder build;
        if (isGif(url)) {
            build = Glide.with(mContext).asDrawable().load(url);
            target = new BitmapTarget(drawable);//Todo

        } else {
            build = Glide.with(mContext).asBitmap().load(url);
            target = new BitmapTarget(drawable);
        }
        targets.add(target);
        build.into(target);
        return drawable;
    }

    private static boolean isGif(String path) {
        int index = path.lastIndexOf('.');
        return index > 0 && "gif".toUpperCase().equals(path.substring(index + 1).toUpperCase());
    }

    private class BitmapTarget extends CustomTarget<Bitmap> {

        private BitmapDrawable drawable1;

        public BitmapTarget(BitmapDrawable drawable) {
            this.drawable1 = drawable;
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            drawable1 = new BitmapDrawable(resource);
            drawable1.setBounds(0, 0, resource.getWidth(), resource.getHeight());
            drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
            drawable.setDrawable(drawable1);
            mTvContent.setText(mTvContent.getText());
            mTvContent.refreshDrawableState();
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }
    }
}
