package com.talk.dialog;

import android.support.annotation.NonNull;

import com.talk.dialog.util.DialogUtils;

/**
 * Created by wlanjie on 15/9/9.
 */
public class DialogInit {

    public static int getTheme(@NonNull TalkDialog.Builder builder) {
        boolean darkTheme = DialogUtils.resolveBoolean(builder.context, R.attr.talk_dark_theme, builder.theme == Theme.DARK);
        builder.theme = darkTheme ? Theme.DARK : Theme.LIGHT;
        return darkTheme ? R.style.Talk_Dark : R.style.Talk_Light;
    }
}
