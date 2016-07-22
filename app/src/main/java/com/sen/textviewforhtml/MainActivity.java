package com.sen.textviewforhtml;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HtmlTextView htmlTextView=(HtmlTextView) findViewById(R.id.htvHtml);
        String text="<p style=\"text-align:center\"><em><strong>YSL TEST</strong></em></p>\n" +
                "\n" +
                "<p style=\"text-align:center\">测试测试测试<span style=\"color:#FF0000\">测试测试</span>测试测试测试</p>\n" +
                "\n" +
                "<p style=\"text-align:center\">测试测试测试测试</p>\n" +
                "\n" +
                "<p style=\"text-align:center\"><span style=\"font-size:20px\">测试</span></p>\n" +
                "\n" +
                "<p style=\"text-align:center\">测<span style=\"color:#EE82EE\">试测</span>试</p>\n" +
                "\n" +
                "<p style=\"text-align:center\">测试测试测试测试测试</p>\n" +
                "\n" +
                "<p style=\"text-align:center\">测试测试测试测试测试测试测试</p>\n" +
                "\n" +
                "<ol>\n" +
                "\t<li>列表1</li>\n" +
                "\t<li>列表2</li>\n" +
                "\t<li>列表3</li>\n" +
                "</ol>\n" +
                "\n" +
                "<ul>\n" +
                "\t<li>列表4</li>\n" +
                "\t<li>列表5</li>\n" +
                "</ul>\n" +
                "\n" +
                "<blockquote>\n" +
                "<p>quote &nbsp;test</p>\n" +
                "</blockquote>\n" +
                "\n" +
                "<p><a href=\"local://points/signin.html\">签到链接测试</a></p>\n";
        htmlTextView.setHtmlText(text);
    }
}
