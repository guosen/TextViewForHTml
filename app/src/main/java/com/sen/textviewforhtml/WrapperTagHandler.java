package com.sen.textviewforhtml;

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2020/05/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
import android.text.Editable;

import androidx.annotation.Nullable;

import org.xml.sax.Attributes;

public interface WrapperTagHandler {
    boolean handleTag(boolean opening, String tag, Editable output, @Nullable Attributes attributes);
}
