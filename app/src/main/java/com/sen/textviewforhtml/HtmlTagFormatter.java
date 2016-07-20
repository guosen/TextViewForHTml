package com.sen.textviewforhtml;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;


/**
 * 扩展TextView显示html
 * <p/>
 * fix 还没支持图片
 *
 * @author shouwang
 * @date 2016-7-20
 */
public class HtmlTagFormatter {
    private static final String TAG_HANDLE_SPAN      = "span";
    private static final String TAG_HANDLE_STYLE     = "style";
    private static final String TAG_HANDLE_ALIGN     = "align";
    private static final String TAG_FONT_SIZE        = "font-size";
    private static final String TAG_BACKGROUND_COLOR = "background-color";
    private static final String Tag_FONT_COLOR       = "color";
    private static final String TAG_TEXT_ALIGN       = "text-align";
    private int startIndex;
    private int stopIndex;
    private String         styleContent   = "";
    private Vector<String> mListParents   = new Vector<String>();//用来标记列表(有序和无序列表)
    private int            mListItemCount = 0;//用来标记列表(有序和无序列表)

    public Spanned handlerHtmlContent(final Context context, String htmlContent) throws NumberFormatException {
        return HtmlParser.buildSpannedText(htmlContent, new HtmlParser.TagHandler() {
            @Override
            public boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
                if (tag.equals(TAG_HANDLE_SPAN)) {
                    //<style>标签的处理方式
                    if (opening) {
                        startIndex = output.length();
                        styleContent = HtmlParser.getValue(attributes, TAG_HANDLE_STYLE);
                    } else {
                        stopIndex = output.length();
                        if (!TextUtils.isEmpty(styleContent)) {
                            String[] styleValues = styleContent.split(";");
                            for (String styleValue : styleValues) {
                                String[] tmpValues = styleValue.split(":");
                                if (tmpValues != null && tmpValues.length > 0) { //(font-size=14px)
                                    if (TAG_FONT_SIZE.equals(tmpValues[0])) { //处理文字效果字体大小
                                        int size = Integer.valueOf(getAllNumbers(tmpValues[1]));
                                        output.setSpan(new AbsoluteSizeSpan(sp2px(context, size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else if (TAG_BACKGROUND_COLOR.equals(tmpValues[0])) { //处理背景效果
                                        output.setSpan(new BackgroundColorSpan(Color.parseColor(tmpValues[1])), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else if (Tag_FONT_COLOR.equals(tmpValues[0])) {//处理字体颜色<span style="color:"#000000">
                                        output.setSpan(new ForegroundColorSpan(Color.parseColor(tmpValues[1])), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else if(TAG_TEXT_ALIGN.equals(tmpValues[0])){
                                        handleAlignTag(output,tmpValues[1]);
                                    }
                                }
                            }
                        }
                    }
                } else if (tag.equals(TAG_HANDLE_ALIGN)) {
                    if (opening) {
                        startIndex = output.length();
                    } else {
                        stopIndex = output.length();
                    }
                }
                //列表标签的解析渲染
                if (tag.equals("ul") || tag.equals("ol") || tag.equals("dd")) {
                    if (opening) {
                        mListParents.add(tag);
                    } else mListParents.remove(tag);

                    mListItemCount = 0;
                } else if (tag.equals("li") && !opening) {
                    handleListTag(output);
                }
                return false;
            }
        });
    }

    //正则获取字体
    private static String getAllNumbers(String body) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    //处理列表标签
    private void handleListTag(Editable output) {
        if (mListParents.lastElement().equals("ul")) {
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.setSpan(new BulletSpan(15), start, output.length(), 0);
        } else if (mListParents.lastElement().equals("ol")) {
            mListItemCount++;
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, mListItemCount + ". ");
            output.setSpan(new LeadingMarginSpan.Standard(15 * mListParents.size()), start, output.length(), 0);
        }
    }
    //处理<text-align>
    private void handleAlignTag(Editable output,String alignTag){
        AlignmentSpan.Standard as=new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
        if(alignTag.equals("center")){
            as = new AlignmentSpan.Standard(
                    Layout.Alignment.ALIGN_CENTER);
        }else if(alignTag.equals("right")){
            as = new AlignmentSpan.Standard(
                    Layout.Alignment.ALIGN_OPPOSITE);
        }else if(alignTag.equals("left")){
            as = new AlignmentSpan.Standard(
                    Layout.Alignment.ALIGN_NORMAL);
        }
        // 参考:https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/SpannableStringBuilder.java
        // throw new RuntimeException("PARAGRAPH span must start at paragraph boundary");
        // AlignmentSpan继承ParagraphStyle;会检查前后是不是有换行符\n;没有的话抛出以上异常
        if(!"\n".equals(output.charAt(stopIndex-1))) {
            output.append("\n");
            output.setSpan(as, startIndex, stopIndex+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}