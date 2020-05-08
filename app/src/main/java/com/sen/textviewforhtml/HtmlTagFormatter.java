package com.sen.textviewforhtml;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;

public class HtmlTagFormatter implements WrapperTagHandler{
    private static final String TAG_HANDLE_SPAN      = "span";
    private static final String TAG_HANDLE_STYLE     = "style";
    private static final String TAG_HANDLE_ALIGN     = "align";
    private static final String TAG_FONT_SIZE        = "font-size";
    private static final String TAG_BACKGROUND_COLOR = "background-color";
    private static final String Tag_FONT_COLOR       = "color";
    private static final String TAG_TEXT_ALIGN       = "text-align";
    private String         styleContent   = "";
    private Vector<String> mListParents   = new Vector<String>();//用来标记列表(有序和无序列表)
    private int            mListItemCount = 0;//用来标记列表(有序和无序列表)
    HashMap<String,String> mTagStyle =new HashMap<>();
    HashMap<String,Integer>mTagStartIndex=new HashMap<>();//用来保存标签开始标记

    private Context mContext ;
    public HtmlTagFormatter(Context context){
        this.mContext = context;
    }
    @Override
    public boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
        if (tag.equals(TAG_HANDLE_SPAN)) {
            //<style>标签的处理方式
            if (opening) {
                mTagStartIndex.put(tag,output.length());
                styleContent = HtmlParser.getValue(attributes, TAG_HANDLE_STYLE);
                mTagStyle.put(tag,styleContent);
            } else {
                handleStyleTag(output,tag,mContext);
                mTagStyle.put(tag,"");
            }
        }
        if(tag.equals("p")){
            if(opening) {
                mTagStartIndex.put(tag,output.length());
                styleContent = HtmlParser.getValue(attributes, TAG_HANDLE_STYLE);
                mTagStyle.put(tag,styleContent);
            } else {
                handleStyleTag(output,tag,mContext);
                mTagStyle.put(tag,"");
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
            handleStyleTag(output, tag, mContext);
            mTagStyle.put(tag, "");
        } else if (tag.equals("li") && opening) {
            mTagStartIndex.put(tag, output.length());
            styleContent = HtmlParser.getValue(attributes, TAG_HANDLE_STYLE);
            mTagStyle.put(tag, styleContent);
        }
        return false;
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
            String[] split = output.toString().split("\n");
            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, mListItemCount + ". ");
            if (output.charAt(start-1)!='\n'){
                output.insert(start-1,"\n");
            }
            Log.d("dd","11111111111 start: " +start);
             setSpan(output,new BulletSpan(15), start, output.length(), 0);
        } else if (mListParents.lastElement().equals("ol")) {
            mListItemCount++;
            String[] split = output.toString().split("\n");
            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, mListItemCount + ". ");

            Log.d("dd","22222222"+"start: " +start);
            setSpan(output,new LeadingMarginSpan.Standard(15 * mListParents.size()), start, output.length(), 0);
        }
    }
    //处理<text-align>
    private void handleAlignTag(Editable output,String parentTag,String alignTag){
        int startIndex=mTagStartIndex.get(parentTag);
        int stopIndex=output.length();
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
//        if(!"\n".equals(""+output.charAt(stopIndex-1))) {
//            output.append("\n");
//            stopIndex++;
//
//        }
//        if(!"\n".equals(""+output.charAt(0))){
//            output.insert(0,"\n");
//            stopIndex++;
//            startIndex--;
//        }
        output.setSpan(as, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void handleStyleTag(Editable output,String tag,Context context){
        String styleContent=mTagStyle.get(tag);
        int startIndex=mTagStartIndex.get(tag);
        int stopIndex=output.length();
        char c = output.charAt(stopIndex-1);
        if (c!='\n'){
            output.append('\n');
            stopIndex++;
        }
        if (!TextUtils.isEmpty(styleContent)) {
            String[] styleValues = styleContent.split(";");
            for (String styleValue : styleValues) {
                String[] tmpValues = styleValue.split(":");
                if (tmpValues != null && tmpValues.length > 0) { //�?要标�?+数据才可食用(font-size=14px)
                    if(TAG_FONT_SIZE.equals(tmpValues[0])) { //处理文字效果
                        int size = Integer.valueOf(getAllNumbers(tmpValues[1]));
                        Log.i("tag",size+"");
                        output.setSpan(new AbsoluteSizeSpan(sp2px(context,size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if(TAG_BACKGROUND_COLOR.equals(tmpValues[0])) { //处理背景效果
                        output.setSpan(new BackgroundColorSpan(Color.parseColor(tmpValues[1])), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if(Tag_FONT_COLOR.equals(tmpValues[0])){
                        String str=output.toString();
                        output.setSpan(new ForegroundColorSpan(Color.parseColor(tmpValues[1])),startIndex,stopIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else if(TAG_TEXT_ALIGN.equals(tmpValues[0])){
                        handleAlignTag(output,tag,tmpValues[1]);
                    }
                }
            }
        }
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void setSpan(Editable output,Object what, int start,int end,  int flags) {
        if (output.charAt(start-1)!='\n'){
            output.insert(start-1,"\n");
        }
        if (output.charAt(end-1)!='\n'){
            output.append('\n');
            end ++ ;
        }
        output.setSpan( what,start, end, flags);
    }

}
