package com.talk.dialog;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    @LayoutRes
    public static int getInflateLayout(TalkDialog.Builder builder) {
        return R.layout.talk_dialog_basic;
    }

    @UiThread
    public static void init(final TalkDialog dialog, View view) {
        final TalkDialog.Builder builder = dialog.mBuilder;
        dialog.title = (TextView) view.findViewById(R.id.title);
        dialog.icon = (ImageView) view.findViewById(R.id.icon);
        dialog.content = (TextView) view.findViewById(R.id.content);
        dialog.titleFrame = view.findViewById(R.id.titleFrame);

        if (dialog.title != null) {
            dialog.title.setTextColor(builder.titleColor);
            dialog.title.setGravity(builder.titleGravity.getGravityInt());

            if (TextUtils.isEmpty(builder.title)) {
                dialog.titleFrame.setVisibility(View.GONE);
            } else {
                dialog.title.setText(builder.title);
                dialog.titleFrame.setVisibility(View.VISIBLE);
            }
            if (builder.titleTextSize != -1) {
                dialog.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, builder.titleTextSize);
            }
        }

        if (builder.icon != null) {
            dialog.icon.setVisibility(View.VISIBLE);
            dialog.icon.setImageDrawable(builder.icon);
        } else {
            Drawable d = DialogUtils.resolveDrawable(builder.context, R.attr.talk_icon);
            if (d != null) {
                dialog.icon.setVisibility(View.VISIBLE);
                dialog.icon.setImageDrawable(d);
            } else {
                dialog.icon.setVisibility(View.GONE);
            }
        }

        if (dialog.content != null) {
            dialog.content.setMovementMethod(new LinkMovementMethod());
            dialog.setTypeface(dialog.content, builder.regularFont);
            dialog.content.setLineSpacing(0f, builder.contentLineSpacingMultiplier);
            if (builder.positiveColor == null) {
                dialog.content.setLinkTextColor(DialogUtils.resolveColor(dialog.getActivity(), android.R.attr.textColorPrimary));
            } else {
                dialog.content.setLinkTextColor(builder.positiveColor);
            }
            dialog.content.setTextColor(builder.contentColor);
            dialog.content.setGravity(builder.contentGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.content.setTextAlignment(builder.contentGravity.getTextAlignment());
            }
            if (builder.contentTextSize != -1) {
                dialog.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, builder.contentTextSize);
            }
            dialog.content.setText(builder.content);
            dialog.content.setVisibility(View.VISIBLE);
        }

        if (dialog.titleFrame != null && builder.titleBackgroundColor != -1) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(builder.titleBackgroundColor);
            float radius;
            if (builder.radius == -1) {
                radius = builder.context.getResources().getDimension(R.dimen.talk_bg_corner_radius);
            } else {
                radius = builder.radius;
            }
            float[] radii = {radius, radius, radius, radius, 0, 0, 0, 0};
            drawable.setCornerRadii(radii);
            DialogUtils.setBackgroundCompat(dialog.titleFrame, drawable);
        }
    }
}
