package com.sen.textviewforhtml;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
/**
 * Created by shouwang on 16/7/20.
 */
public class HtmlTextView extends androidx.appcompat.widget.AppCompatTextView {
    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void setHtmlText(CharSequence text){
        HtmlTagFormatter htmlTagFormatter = new HtmlTagFormatter(getContext());
        HtmlImageGetter glideImageGeter=new HtmlImageGetter(getContext(), this);
        try {
             Spanned spanned1 = Html.fromHtml(text.toString(),glideImageGeter, new WrapperContentHandler(htmlTagFormatter));
             setText(spanned1);
        }catch (Exception ex){
            //如果标签处理有误先忽略;不闪退;忽略出错的标签
            //至少保证能显示文本(用系统自带Html.from())
           setText(Html.fromHtml(text.toString()));
        }
    }
}
