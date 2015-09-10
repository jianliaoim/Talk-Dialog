package com.talk.dialog;

import android.content.res.Resources;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.talk.dialog.internal.TalkButton;
import com.talk.dialog.simplelist.TalkSimpleListAdapter;
import com.talk.dialog.util.DialogUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
        if (builder.customView != null) {
            return R.layout.talk_dialog_custom;
        } else if(builder.items != null && builder.items.length > 0 || builder.adapter != null) {
            return R.layout.talk_dialog_list;
        } else {
            return R.layout.talk_dialog_basic;
        }
    }

    @UiThread
    public static void init(final TalkDialog dialog, View view) {
        final TalkDialog.Builder builder = dialog.mBuilder;
        dialog.title = (TextView) view.findViewById(R.id.title);
        dialog.icon = (ImageView) view.findViewById(R.id.icon);
        dialog.content = (TextView) view.findViewById(R.id.content);
        dialog.titleFrame = view.findViewById(R.id.titleFrame);
        dialog.positiveButton = (TalkButton) view.findViewById(R.id.buttonDefaultPositive);
        dialog.negativeButton = (TalkButton) view.findViewById(R.id.buttonDefaultNegative);
        dialog.neutralButton = (TalkButton) view.findViewById(R.id.buttonDefaultNeutral);
        dialog.listView = (ListView) view.findViewById(R.id.contentListView);

        // Retrieve action button colors from theme attributes or the Builder
        if (!builder.positiveColorSet) {
            builder.positiveColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.talk_positive_color, builder.positiveColor);
        }
        if (!builder.neutralColorSet) {
            builder.neutralColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.talk_neutral_color, builder.neutralColor);
        }
        if (!builder.negativeColorSet) {
            builder.negativeColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.talk_negative_color, builder.negativeColor);
        }
        if (!builder.widgetColorSet) {
            builder.widgetColor = DialogUtils.resolveColor(builder.context, R.attr.talk_widget_color, builder.widgetColor);
        }

        // Retrieve default title/content colors
        if (!builder.titleColorSet) {
            final int titleColorFallback = DialogUtils.resolveColor(builder.context, android.R.attr.textColorPrimary);
            builder.titleColor = DialogUtils.resolveColor(builder.context, R.attr.talk_title_color, titleColorFallback);
        }
        if (!builder.contentColorSet) {
            final int contentColorFallback = DialogUtils.resolveColor(builder.context, android.R.attr.textColorSecondary);
            builder.contentColor = DialogUtils.resolveColor(builder.context, R.attr.talk_content_color, contentColorFallback);
        }
        if (!builder.itemColorSet) {
            builder.itemColor = DialogUtils.resolveColor(builder.context, R.attr.talk_item_color, builder.contentColor);
        }

        if (builder.backgroundColor == 0) {
            builder.backgroundColor = DialogUtils.resolveColor(builder.context, R.attr.talk_background_color);
        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(builder.backgroundColor);
            float radius;
            if (builder.radius == -1) {
                radius = builder.context.getResources().getDimension(R.dimen.talk_bg_corner_radius);
            } else {
                radius = builder.radius;
            }
            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            drawable.setCornerRadii(radii);
            DialogUtils.setBackgroundCompat(view, drawable);
        }

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

        int maxIconSize = builder.maxIconSize;
        if (maxIconSize == -1) {
            maxIconSize = DialogUtils.resolveDimension(builder.context, R.attr.talk_icon_max_size);
        }
        if (builder.limitIconToDefaultSize || DialogUtils.resolveBoolean(builder.context, R.attr.talk_icon_limit_icon_to_default_size)) {
            maxIconSize = builder.context.getResources().getDimensionPixelSize(R.dimen.talk_icon_max_size);
        }
        if (maxIconSize > -1) {
            dialog.icon.setAdjustViewBounds(true);
            dialog.icon.setMaxHeight(maxIconSize);
            dialog.icon.setMaxWidth(maxIconSize);
            dialog.icon.requestLayout();
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

        //setup action buttons
        boolean textAllCaps;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textAllCaps = DialogUtils.resolveBoolean(builder.context, android.R.attr.textAllCaps, true);
            if (textAllCaps) {
                textAllCaps = DialogUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
            }
        } else {
            textAllCaps = DialogUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
        }

        if (builder.customView != null) {
            initCustomView(dialog, view);
        }

        if (builder.inputCallback != null && builder.positiveText == null) {
            builder.positiveText = builder.context.getText(android.R.string.ok);
        }
        // Set up the initial visibility of action buttons based on whether or not text was set
        dialog.positiveButton.setVisibility(builder.positiveText != null ? View.VISIBLE : View.GONE);
        dialog.neutralButton.setVisibility(builder.neutralText != null ? View.VISIBLE : View.GONE);
        dialog.negativeButton.setVisibility(builder.negativeText != null ? View.VISIBLE : View.GONE);

        TalkButton positiveTextView = dialog.positiveButton;
        dialog.setTypeface(positiveTextView, builder.mediumFont);
        positiveTextView.setAllCapsCompat(textAllCaps);
        positiveTextView.setText(builder.positiveText);
        positiveTextView.setTextColor(builder.positiveColor);
        positiveTextView.setStackedSelector(dialog.getButtonSelector(DialogAction.POSITIVE, true));
        positiveTextView.setDefaultSelector(dialog.getButtonSelector(DialogAction.POSITIVE, false));
        positiveTextView.setTag(DialogAction.POSITIVE);
        positiveTextView.setOnClickListener(dialog);
        positiveTextView.setVisibility(View.VISIBLE);

        TalkButton negativeTextView = dialog.negativeButton;
        dialog.setTypeface(negativeTextView, builder.mediumFont);
        negativeTextView.setAllCapsCompat(textAllCaps);
        negativeTextView.setText(builder.negativeText);
        negativeTextView.setTextColor(builder.negativeColor);
        negativeTextView.setStackedSelector(dialog.getButtonSelector(DialogAction.NEGATIVE, true));
        negativeTextView.setDefaultSelector(dialog.getButtonSelector(DialogAction.NEGATIVE, false));
        negativeTextView.setTag(DialogAction.NEGATIVE);
        negativeTextView.setOnClickListener(dialog);
        negativeTextView.setVisibility(View.VISIBLE);

        TalkButton neutralTextView = dialog.neutralButton;
        dialog.setTypeface(neutralTextView, builder.mediumFont);
        neutralTextView.setAllCapsCompat(textAllCaps);
        neutralTextView.setText(builder.neutralText);
        neutralTextView.setTextColor(builder.neutralColor);
        neutralTextView.setStackedSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, true));
        neutralTextView.setDefaultSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, false));
        neutralTextView.setTag(DialogAction.NEUTRAL);
        neutralTextView.setOnClickListener(dialog);
        neutralTextView.setVisibility(View.VISIBLE);

        if (builder.items != null && builder.items.length > 0 || builder.adapter != null) {
            initListView(dialog, view);
            dialog.invalidateList();
            dialog.checkIfListInitScroll();
        }
    }

    static void initListView(TalkDialog dialog, View view) {
        final TalkDialog.Builder builder = dialog.mBuilder;
        if (builder.listCallbackMultiChoice != null) {
            dialog.selectedIndicesList = new ArrayList<>();
        }
        if (dialog.listView != null && (builder.items != null && builder.items.length > 0 || builder.adapter != null)) {
//            dialog.listView.setSelector(dialog.getListSelector());
            if (builder.adapter == null) {
                if (builder.listCallbackSingleChoice != null) {
                    dialog.listType = TalkDialog.ListType.SINGLE;
                } else if (builder.listCallbackMultiChoice != null) {
                    dialog.listType = TalkDialog.ListType.MULTI;
                    if (builder.selectedIndices != null) {
                        dialog.selectedIndicesList = new ArrayList<>(Arrays.asList(builder.selectedIndices));
                    }
                } else {
                    dialog.listType = TalkDialog.ListType.REGULAR;
                }
                builder.adapter = new TalkDialogAdapter(dialog, TalkDialog.ListType.getLayoutForType(dialog.listType));
            } else if (builder.adapter instanceof TalkSimpleListAdapter) {
                ((TalkSimpleListAdapter) builder.adapter).setDialog(dialog);
            }
        }
    }

    static void initCustomView(TalkDialog dialog, View view) {
        final TalkDialog.Builder builder = dialog.mBuilder;
        LinearLayout frame = (LinearLayout) view.findViewById(R.id.customViewFrame);
        dialog.customViewFrame = frame;
        View innerView = builder.customView;
        final Resources r = builder.context.getResources();
        final int framePadding = r.getDimensionPixelSize(R.dimen.talk_dialog_frame_margin);
        if (builder.wrapCustomViewInScroll) {
            final ScrollView sv = new ScrollView(builder.context);
            final int paddingTop = r.getDimensionPixelSize(R.dimen.talk_content_padding_top);
            final int paddingBottom = r.getDimensionPixelSize(R.dimen.talk_content_padding_bottom);
            sv.setClipToPadding(false);
            if (innerView instanceof EditText) {
                sv.setPadding(framePadding, paddingTop, framePadding, paddingBottom);
            } else {
                sv.setPadding(0, paddingTop, 0, paddingBottom);
                innerView.setPadding(framePadding, 0, framePadding, 0);
            }
            sv.addView(innerView, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            innerView = sv;
        } else {
            innerView.setPadding(framePadding, 0, framePadding, 0);
        }
        frame.addView(innerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
