package com.sen.textviewforhtml;

import android.app.Activity;
import android.os.Bundle;
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HtmlTextView htmlTextView=(HtmlTextView) findViewById(R.id.htvHtml);
        String text="<p style=\"text-align:center\"><em><strong>YSL TEST</strong></em></p>" +
                "<p style=\"text-align:center\">测试测试测试<span style=\"color:#FF0000\">测试测试</span>测试测试测试</p>" +
                "" +
                "<p style=\"text-align:center\">测试测试测试测试</p>" +
                "" +
                "<p style=\"text-align:center\"><span style=\"font-size:20px\">测试</span></p>" +
                "" +
                "<p style=\"text-align:center\">测<span style=\"color:#EE82EE\">试测</span>试</p>" +
                "" +
                "<p style=\"text-align:center\">测试测试测试测试测试</p>" +
                "" +
                "<p style=\"text-align:center\">测试测试测试测试测试测试测试</p>" +
                "" +
                "<ul>" +
                "\t<li>d列表4</li>" +
                "\t<li>f列表5</li>" +
                "</ul>" +
                "" +
                "<blockquote>" +
                "<p>quote &nbsp;test</p>" +
                "</blockquote>" +
                "<img src = \"http://img1.cache.netease.com/catchpic/7/73/73B6B668BA8696D37A49675197C7D2B4.jpg\"/>\n" +
                "<p><a href=\"local://points/signin.html\">签到链接测试</a></p>\n";


        htmlTextView.setHtmlText(text);
    }
}
