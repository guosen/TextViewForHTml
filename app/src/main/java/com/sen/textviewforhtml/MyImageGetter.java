package com.sen.textviewforhtml;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyImageGetter  implements Html.ImageGetter {
    TextView textView;
    Context context;

    public MyImageGetter(Context contxt, TextView textView) {
        this.context = contxt;
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String paramString) {
        UrlDrawable urlDrawable = new UrlDrawable();

        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        UrlDrawable urlDrawable;

        public ImageGetterAsyncTask(UrlDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;
                urlDrawable.invalidateSelf();

                MyImageGetter.this.textView.setText(textView.getText());
            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        public Drawable fetchDrawable(String url) {
            Drawable drawable = null;
            URL Url;
            try {
                Url = new URL(url);
                drawable = fetchCompressedDrawable(Url.openStream(), textView.getResources(),url);
            } catch (Exception e) {
                return null;
            }
            int newwidth = drawable.getIntrinsicWidth();
            int newheight = drawable.getIntrinsicHeight();

            drawable.setBounds(0, 0, newwidth, newheight);
            urlDrawable.setBounds(0, 0, newwidth, newheight);
            textView.invalidate();

            return drawable;
        }

        public Drawable fetchCompressedDrawable(InputStream is ,Resources res, String urlString) {
            try {

                Bitmap original = new BitmapDrawable(res, is).getBitmap();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.JPEG, 50, out);
                original.recycle();
                is.close();

                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                out.close();

                float scale = getScale(decoded);
                BitmapDrawable b = new BitmapDrawable(res, decoded);

                b.setBounds(0, 0, (int) (b.getIntrinsicWidth() * scale), (int) (b.getIntrinsicHeight() * scale));
                return b;
            } catch (Exception e) {
                return null;
            }
        }
        private float getScale(Bitmap bitmap) {
            View container = textView;
            if (container == null) {
                return 1f;
            }
            float maxWidth = container.getWidth();
            float originalDrawableWidth = bitmap.getWidth();

            return maxWidth / originalDrawableWidth;
        }
    }
}
