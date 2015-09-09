package com.talk.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.talk.dialog.util.DialogUtils;
import com.talk.dialog.util.TypefaceHelper;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Created by wlanjie on 15/9/9.
 */
public class TalkDialog extends DialogFragment {

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * The class used to construct a MaterialDialog.
     */
    public static class Builder implements Serializable {

        protected final FragmentActivity context;
        protected final static String ARGS_KEY = "builder_serializable";
        protected final static String TAG = "TalkDialog";
        protected CharSequence titleError;
        protected CharSequence title;
        protected GravityEnum titleGravity = GravityEnum.START;
        protected GravityEnum contentGravity = GravityEnum.START;
        protected GravityEnum btnStackedGravity = GravityEnum.END;
        protected GravityEnum itemsGravity = GravityEnum.START;
        protected GravityEnum buttonsGravity = GravityEnum.START;
        protected boolean disableAnimation = false;
        protected float radius = -1;
        protected int titleTextSize = -1;
        protected int titleColor = -1;
        protected int contentColor = -1;
        protected int titleBackgroundColor = -1;
        protected int titleErrorColor = -1;
        protected int contentErrorColor = -1;
        protected int titleBackgroundErrorColor = -1;
        protected int contentTextSize = -1;
        protected CharSequence contentError;
        protected CharSequence content;
        protected CharSequence[] items;
        protected CharSequence positiveText;
        protected CharSequence neutralText;
        protected CharSequence negativeText;
        protected View customView;
        protected int widgetColor;
        protected ColorStateList positiveColor;
        protected ColorStateList negativeColor;
        protected ColorStateList neutralColor;
        protected ButtonCallback callback;
        protected ListCallback listCallback;
        protected ListCallbackSingleChoice listCallbackSingleChoice;
        protected ListCallbackMultiChoice listCallbackMultiChoice;
        protected ListCallback listCallbackCustom;
        protected boolean alwaysCallMultiChoiceCallback = false;
        protected boolean alwaysCallSingleChoiceCallback = false;
        protected Theme theme = Theme.LIGHT;
        protected boolean cancelable = true;
        protected float contentLineSpacingMultiplier = 1.2f;
        protected int selectedIndex = -1;
        protected Integer[] selectedIndices = null;
        protected boolean autoDismiss = true;
        protected Typeface regularFont;
        protected Typeface mediumFont;
        protected Drawable icon;
        protected boolean limitIconToDefaultSize;
        protected int maxIconSize = -1;
        protected ListAdapter adapter;
        protected DialogInterface.OnDismissListener dismissListener;
        protected DialogInterface.OnCancelListener cancelListener;
        protected DialogInterface.OnKeyListener keyListener;
        protected DialogInterface.OnShowListener showListener;
        protected boolean forceStacking;
        protected boolean wrapCustomViewInScroll;
        protected int dividerColor;
        protected int backgroundColor;
        protected int itemColor;
        protected boolean indeterminateProgress;
        protected boolean showMinMax;
        protected int progress = -2;
        protected int progressMax = 0;
        protected CharSequence inputPrefill;
        protected CharSequence inputHint;
        protected InputCallback inputCallback;
        protected boolean inputAllowEmpty;
        protected int inputType = -1;
        protected boolean alwaysCallInputCallback;
        protected int inputMaxLength = -1;
        protected int inputMaxLengthErrorColor = 0;

        protected String progressNumberFormat;
        protected NumberFormat progressPercentFormat;
        protected boolean indeterminateIsHorizontalProgress;

        protected boolean titleColorSet = false;
        protected boolean contentColorSet = false;
        protected boolean itemColorSet = false;
        protected boolean positiveColorSet = false;
        protected boolean neutralColorSet = false;
        protected boolean negativeColorSet = false;
        protected boolean widgetColorSet = false;
        protected boolean dividerColorSet = false;

        @DrawableRes
        protected int listSelector;
        @DrawableRes
        protected int btnSelectorStacked;
        @DrawableRes
        protected int btnSelectorPositive;
        @DrawableRes
        protected int btnSelectorNeutral;
        @DrawableRes
        protected int btnSelectorNegative;

        public final Context getContext() {
            return context;
        }

        public final GravityEnum getItemsGravity() {
            return itemsGravity;
        }

        public final int getItemColor() {
            return itemColor;
        }

        public final Typeface getRegularFont() {
            return regularFont;
        }

        public Builder(@NonNull FragmentActivity context) {
            this.context = context;
            final int materialBlue = context.getResources().getColor(R.color.talk_material_blue);

            // Retrieve default accent colors, which are used on the action buttons and progress bars
            this.widgetColor = DialogUtils.resolveColor(context, R.attr.colorAccent, materialBlue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.widgetColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent, this.widgetColor);
            }

            this.positiveColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            this.negativeColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            this.neutralColor = DialogUtils.getActionTextStateList(context, this.widgetColor);

            this.progressPercentFormat = NumberFormat.getPercentInstance();
            this.progressNumberFormat = "%1d/%2d";

            // Set the default theme based on the Activity theme's primary color darkness (more white or more black)
            final int primaryTextColor = DialogUtils.resolveColor(context, android.R.attr.textColorPrimary);
            this.theme = DialogUtils.isColorDark(primaryTextColor) ? Theme.LIGHT : Theme.DARK;

            // Load theme values from the ThemeSingleton if needed
            checkSingleton();

            // Retrieve gravity settings from global theme attributes if needed
            this.titleGravity = DialogUtils.resolveGravityEnum(context, R.attr.talk_title_gravity, this.titleGravity);
            this.contentGravity = DialogUtils.resolveGravityEnum(context, R.attr.talk_content_gravity, this.contentGravity);
            this.btnStackedGravity = DialogUtils.resolveGravityEnum(context, R.attr.talk_btnstacked_gravity, this.btnStackedGravity);
            this.itemsGravity = DialogUtils.resolveGravityEnum(context, R.attr.talk_items_gravity, this.itemsGravity);
            this.buttonsGravity = DialogUtils.resolveGravityEnum(context, R.attr.talk_buttons_gravity, this.buttonsGravity);

            final String mediumFont = DialogUtils.resolveString(context, R.attr.talk_medium_font);
            final String regularFont = DialogUtils.resolveString(context, R.attr.talk_regular_font);
            typeface(mediumFont, regularFont);

            if (this.mediumFont == null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        this.mediumFont = Typeface.create("sans-serif-medium", Typeface.NORMAL);
                    else
                        this.mediumFont = Typeface.create("sans-serif", Typeface.BOLD);
                } catch (Exception ignored) {
                }
            }
            if (this.regularFont == null) {
                try {
                    this.regularFont = Typeface.create("sans-serif", Typeface.NORMAL);
                } catch (Exception ignored) {
                }
            }
        }

        private void checkSingleton() {
            if (ThemeSingleton.get(false) == null) return;
            ThemeSingleton s = ThemeSingleton.get();
            if (s.darkTheme)
                this.theme = Theme.DARK;
            if (s.titleColor != 0)
                this.titleColor = s.titleColor;
            if (s.contentColor != 0)
                this.contentColor = s.contentColor;
            if (s.positiveColor != null)
                this.positiveColor = s.positiveColor;
            if (s.neutralColor != null)
                this.neutralColor = s.neutralColor;
            if (s.negativeColor != null)
                this.negativeColor = s.negativeColor;
            if (s.itemColor != 0)
                this.itemColor = s.itemColor;
            if (s.icon != null)
                this.icon = s.icon;
            if (s.backgroundColor != 0)
                this.backgroundColor = s.backgroundColor;
            if (s.dividerColor != 0)
                this.dividerColor = s.dividerColor;
            if (s.btnSelectorStacked != 0)
                this.btnSelectorStacked = s.btnSelectorStacked;
            if (s.listSelector != 0)
                this.listSelector = s.listSelector;
            if (s.btnSelectorPositive != 0)
                this.btnSelectorPositive = s.btnSelectorPositive;
            if (s.btnSelectorNeutral != 0)
                this.btnSelectorNeutral = s.btnSelectorNeutral;
            if (s.btnSelectorNegative != 0)
                this.btnSelectorNegative = s.btnSelectorNegative;
            if (s.widgetColor != 0)
                this.widgetColor = s.widgetColor;
            this.titleGravity = s.titleGravity;
            this.contentGravity = s.contentGravity;
            this.btnStackedGravity = s.btnStackedGravity;
            this.itemsGravity = s.itemsGravity;
            this.buttonsGravity = s.buttonsGravity;
        }

        public Builder disableAnimation(boolean disableAnimation) {
            this.disableAnimation = disableAnimation;
            return this;
        }

        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder titleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder title(@StringRes int titleRes) {
            title(this.context.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder titleError(@StringRes int titleRes) {
            titleError(this.context.getResources().getString(titleRes));
            return this;
        }

        public Builder titleError(@NonNull CharSequence title) {
            this.titleError = title;
            return this;
        }

        public Builder titleGravity(@NonNull GravityEnum gravity) {
            this.titleGravity = gravity;
            return this;
        }

        public Builder titleColor(@ColorInt int color) {
            this.titleColor = color;
            this.titleColorSet = true;
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            titleColor(this.context.getResources().getColor(colorRes));
            return this;
        }

        public Builder titleErrorColor(@ColorInt int color) {
            this.titleErrorColor = color;
            return this;
        }

        public Builder titleErrorColorRes(@ColorRes int color) {
            titleErrorColor(this.context.getResources().getColor(color));
            return this;
        }

        public Builder titleBackgroundErrorColor(@ColorInt int color) {
            this.titleBackgroundErrorColor = color;
            return this;
        }

        public Builder titleBackgroundErrorColorRes(@ColorRes int color) {
            titleBackgroundErrorColor(this.context.getResources().getColor(color));
            return this;
        }

        public Builder titleColorAttr(@AttrRes int colorAttr) {
            titleColor(DialogUtils.resolveColor(this.context, colorAttr));
            return this;
        }

        public Builder titleBackgroundColor(@ColorInt int color) {
            this.titleBackgroundColor = color;
            return this;
        }

        public Builder titleBackgroundColorRes(@ColorRes int colorRes) {
            titleBackgroundColor(this.context.getResources().getColor(colorRes));
            return this;
        }

        /**
         * Sets the fonts used in the dialog. It's recommended that you use {@link #typeface(String, String)} instead,
         * to avoid duplicate Typeface allocations and high memory usage.
         *
         * @param medium  The font used on titles and action buttons. Null uses device default.
         * @param regular The font used everywhere else, like on the content and list items. Null uses device default.
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder typeface(@Nullable Typeface medium, @Nullable Typeface regular) {
            this.mediumFont = medium;
            this.regularFont = regular;
            return this;
        }

        /**
         * Sets the fonts used in the dialog, by file names. This also uses TypefaceHelper in order
         * to avoid any un-needed allocations (it recycles typefaces for you).
         *
         * @param medium  The name of font in assets/fonts used on titles and action buttons (null uses device default). E.g. [your-project]/app/main/assets/fonts/[medium]
         * @param regular The name of font in assets/fonts used everywhere else, like content and list items (null uses device default). E.g. [your-project]/app/main/assets/fonts/[regular]
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder typeface(@Nullable String medium, @Nullable String regular) {
            if (medium != null) {
                this.mediumFont = TypefaceHelper.get(this.context, medium);
                if (this.mediumFont == null)
                    throw new IllegalArgumentException("No font asset found for " + medium);
            }
            if (regular != null) {
                this.regularFont = TypefaceHelper.get(this.context, regular);
                if (this.regularFont == null)
                    throw new IllegalArgumentException("No font asset found for " + regular);
            }
            return this;
        }

        public Builder icon(@NonNull Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder iconRes(@DrawableRes int icon) {
            this.icon = ResourcesCompat.getDrawable(context.getResources(), icon, null);
            return this;
        }

        public Builder iconAttr(@AttrRes int iconAttr) {
            this.icon = DialogUtils.resolveDrawable(context, iconAttr);
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            content(this.context.getText(contentRes));
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder content(@StringRes int contentRes, Object... formatArgs) {
            content(this.context.getString(contentRes, formatArgs));
            return this;
        }

        public Builder contentTextSize(int contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public Builder contentErrorColor(@ColorInt int color) {
            this.contentErrorColor = color;
            return this;
        }

        public Builder contentErrorColorRes(@ColorRes int color) {
            contentErrorColor(this.context.getResources().getColor(color));
            return this;
        }

        public Builder contentError(@StringRes int contentRes) {
            contentError(this.context.getResources().getString(contentRes));
            return this;
        }

        public Builder contentError(@NonNull CharSequence contentError) {
            this.contentError = contentError;
            return this;
        }

        public Builder contentColor(@ColorInt int color) {
            this.contentColor = color;
            this.contentColorSet = true;
            return this;
        }

        public Builder contentColorRes(@ColorRes int colorRes) {
            contentColor(this.context.getResources().getColor(colorRes));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int colorAttr) {
            contentColor(DialogUtils.resolveColor(this.context, colorAttr));
            return this;
        }

        public Builder contentGravity(@NonNull GravityEnum gravity) {
            this.contentGravity = gravity;
            return this;
        }

        public Builder contentLineSpacing(float multiplier) {
            this.contentLineSpacingMultiplier = multiplier;
            return this;
        }

        public Builder items(@ArrayRes int itemsRes) {
            items(this.context.getResources().getTextArray(itemsRes));
            return this;
        }

        public Builder items(@NonNull CharSequence[] items) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set items() when you're using a custom view.");
            this.items = items;
            return this;
        }

        public Builder itemsCallback(@NonNull ListCallback callback) {
            this.listCallback = callback;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = null;
            return this;
        }

        public Builder itemColor(@ColorInt int color) {
            this.itemColor = color;
            this.itemColorSet = true;
            return this;
        }

        public Builder itemColorRes(@ColorRes int colorRes) {
            return itemColor(this.context.getResources().getColor(colorRes));
        }

        public Builder itemColorAttr(@AttrRes int colorAttr) {
            return itemColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder itemsGravity(@NonNull GravityEnum gravity) {
            this.itemsGravity = gravity;
            return this;
        }

        public Builder buttonsGravity(@NonNull GravityEnum gravity) {
            this.buttonsGravity = gravity;
            return this;
        }

        /**
         * Pass anything below 0 (such as -1) for the selected index to leave all options unselected initially.
         * Otherwise pass the index of an item that will be selected initially.
         *
         * @param selectedIndex The checkbox index that will be selected initially.
         * @param callback      The callback that will be called when the presses the positive button.
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder itemsCallbackSingleChoice(int selectedIndex, @NonNull ListCallbackSingleChoice callback) {
            this.selectedIndex = selectedIndex;
            this.listCallback = null;
            this.listCallbackSingleChoice = callback;
            this.listCallbackMultiChoice = null;
            return this;
        }

        /**
         * By default, the single choice callback is only called when the user clicks the positive button
         * or if there are no buttons. Call this to force it to always call on item clicks even if the
         * positive button exists.
         *
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder alwaysCallSingleChoiceCallback() {
            this.alwaysCallSingleChoiceCallback = true;
            return this;
        }

        /**
         * Pass null for the selected indices to leave all options unselected initially. Otherwise pass
         * an array of indices that will be selected initially.
         *
         * @param selectedIndices The radio button indices that will be selected initially.
         * @param callback        The callback that will be called when the presses the positive button.
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder itemsCallbackMultiChoice(@Nullable Integer[] selectedIndices, @NonNull ListCallbackMultiChoice callback) {
            this.selectedIndices = selectedIndices;
            this.listCallback = null;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = callback;
            return this;
        }

        /**
         * By default, the multi choice callback is only called when the user clicks the positive button
         * or if there are no buttons. Call this to force it to always call on item clicks even if the
         * positive button exists.
         *
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder alwaysCallMultiChoiceCallback() {
            this.alwaysCallMultiChoiceCallback = true;
            return this;
        }

        public Builder positiveText(@StringRes int postiveRes) {
            positiveText(this.context.getText(postiveRes));
            return this;
        }

        public Builder positiveText(@NonNull CharSequence message) {
            this.positiveText = message;
            return this;
        }

        public Builder positiveColor(@ColorInt int color) {
            return positiveColor(DialogUtils.getActionTextStateList(context, color));
        }

        public Builder positiveColorRes(@ColorRes int colorRes) {
            return positiveColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder positiveColorAttr(@AttrRes int colorAttr) {
            return positiveColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder positiveColor(ColorStateList colorStateList) {
            this.positiveColor = colorStateList;
            this.positiveColorSet = true;
            return this;
        }

        public Builder neutralText(@StringRes int neutralRes) {
            return neutralText(this.context.getText(neutralRes));
        }

        public Builder neutralText(@NonNull CharSequence message) {
            this.neutralText = message;
            return this;
        }

        public Builder negativeColor(@ColorInt int color) {
            return negativeColor(DialogUtils.getActionTextStateList(context, color));
        }

        public Builder negativeColorRes(@ColorRes int colorRes) {
            return negativeColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder negativeColorAttr(@AttrRes int colorAttr) {
            return negativeColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder negativeColor(ColorStateList colorStateList) {
            this.negativeColor = colorStateList;
            this.negativeColorSet = true;
            return this;
        }

        public Builder negativeText(@StringRes int negativeRes) {
            return negativeText(this.context.getText(negativeRes));
        }

        public Builder negativeText(@NonNull CharSequence message) {
            this.negativeText = message;
            return this;
        }

        public Builder neutralColor(@ColorInt int color) {
            return neutralColor(DialogUtils.getActionTextStateList(context, color));
        }

        public Builder neutralColorRes(@ColorRes int colorRes) {
            return neutralColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder neutralColorAttr(@AttrRes int colorAttr) {
            return neutralColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder neutralColor(ColorStateList colorStateList) {
            this.neutralColor = colorStateList;
            this.neutralColorSet = true;
            return this;
        }

        public Builder listSelector(@DrawableRes int selectorRes) {
            this.listSelector = selectorRes;
            return this;
        }

        public Builder btnSelectorStacked(@DrawableRes int selectorRes) {
            this.btnSelectorStacked = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes) {
            this.btnSelectorPositive = selectorRes;
            this.btnSelectorNeutral = selectorRes;
            this.btnSelectorNegative = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes, @NonNull DialogAction which) {
            switch (which) {
                default:
                    this.btnSelectorPositive = selectorRes;
                    break;
                case NEUTRAL:
                    this.btnSelectorNeutral = selectorRes;
                    break;
                case NEGATIVE:
                    this.btnSelectorNegative = selectorRes;
                    break;
            }
            return this;
        }

        /**
         * Sets the gravity used for the text in stacked action buttons. By default, it's #{@link GravityEnum#END}.
         *
         * @param gravity The gravity to use.
         * @return The Builder instance so calls can be chained.
         */
        public Builder btnStackedGravity(@NonNull GravityEnum gravity) {
            this.btnStackedGravity = gravity;
            return this;
        }

        public Builder customView(@LayoutRes int layoutRes, boolean wrapInScrollView) {
            LayoutInflater li = LayoutInflater.from(this.context);
            return customView(li.inflate(layoutRes, null), wrapInScrollView);
        }

        public Builder customView(@NonNull View view, boolean wrapInScrollView) {
//            if (this.content != null)
//                throw new IllegalStateException("You cannot use customView() when you have content set.");
//            else if (this.items != null)
//                throw new IllegalStateException("You cannot use customView() when you have items set.");
//            else if (this.inputCallback != null)
//                throw new IllegalStateException("You cannot use customView() with an input dialog");
//            else if (this.progress > -2 || this.indeterminateProgress)
//                throw new IllegalStateException("You cannot use customView() with a progress dialog");
            if (view.getParent() != null && view.getParent() instanceof ViewGroup)
                ((ViewGroup) view.getParent()).removeView(view);
            this.customView = view;
            this.wrapCustomViewInScroll = wrapInScrollView;
            return this;
        }

        /**
         * Makes this dialog a progress dialog.
         *
         * @param indeterminate If true, an infinite circular spinner is shown. If false, a horizontal progress bar is shown that is incremented or set via the built MaterialDialog instance.
         * @param max           When indeterminate is false, the max value the horizontal progress bar can get to.
         * @return An instance of the Builder so calls can be chained.
         */
        public Builder progress(boolean indeterminate, int max) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set progress() when you're using a custom view.");
            if (indeterminate) {
                this.indeterminateProgress = true;
                this.progress = -2;
            } else {
                this.indeterminateProgress = false;
                this.progress = -1;
                this.progressMax = max;
            }
            return this;
        }

        /**
         * Makes this dialog a progress dialog.
         *
         * @param indeterminate If true, an infinite circular spinner is shown. If false, a horizontal progress bar is shown that is incremented or set via the built MaterialDialog instance.
         * @param max           When indeterminate is false, the max value the horizontal progress bar can get to.
         * @param showMinMax    For determinate dialogs, the min and max will be displayed to the left (start) of the progress bar, e.g. 50/100.
         * @return An instance of the Builder so calls can be chained.
         */
        public Builder progress(boolean indeterminate, int max, boolean showMinMax) {
            this.showMinMax = showMinMax;
            return progress(indeterminate, max);
        }

        /**
         * hange the format of the small text showing current and maximum units of progress.
         * The default is "%1d/%2d".
         */
        public Builder progressNumberFormat(@NonNull String format) {
            this.progressNumberFormat = format;
            return this;
        }

        /**
         * Change the format of the small text showing the percentage of progress.
         * The default is NumberFormat.getPercentageInstance().
         */
        public Builder progressPercentFormat(@NonNull NumberFormat format) {
            this.progressPercentFormat = format;
            return this;
        }

        /**
         * By default, indeterminate progress dialogs will use a circular indicator. You
         * can change it to use a horizontal progress indicator.
         */
        public Builder progressIndeterminateStyle(boolean horizontal) {
            this.indeterminateIsHorizontalProgress = horizontal;
            return this;
        }

        public Builder widgetColor(@ColorInt int color) {
            this.widgetColor = color;
            this.widgetColorSet = true;
            return this;
        }

        public Builder widgetColorRes(@ColorRes int colorRes) {
            return widgetColor(this.context.getResources().getColor(colorRes));
        }

        public Builder widgetColorAttr(@AttrRes int colorAttr) {
            return widgetColorRes(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder dividerColor(@ColorInt int color) {
            this.dividerColor = color;
            this.dividerColorSet = true;
            return this;
        }

        public Builder dividerColorRes(@ColorRes int colorRes) {
            return dividerColor(this.context.getResources().getColor(colorRes));
        }

        public Builder dividerColorAttr(@AttrRes int colorAttr) {
            return dividerColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder backgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int colorRes) {
            return backgroundColor(this.context.getResources().getColor(colorRes));
        }

        public Builder backgroundColorAttr(@AttrRes int colorAttr) {
            return backgroundColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder callback(@NonNull ButtonCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder theme(@NonNull Theme theme) {
            this.theme = theme;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * This defaults to true. If set to false, the dialog will not automatically be dismissed
         * when an action button is pressed, and not automatically dismissed when the user selects
         * a list item.
         *
         * @param dismiss Whether or not to dismiss the dialog automatically.
         * @return The Builder instance so you can chain calls to it.
         */
        public Builder autoDismiss(boolean dismiss) {
            this.autoDismiss = dismiss;
            return this;
        }

        /**
         * Sets a custom {@link ListAdapter} for the dialog's list
         *
         * @param adapter  The adapter to set to the list.
         * @param callback The callback invoked when an item in the list is selected.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder adapter(@NonNull ListAdapter adapter, @Nullable ListCallback callback) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set adapter() when you're using a custom view.");
            this.adapter = adapter;
            this.listCallbackCustom = callback;
            return this;
        }

        /**
         * Limits the display size of a set icon to 48dp.
         */
        public Builder limitIconToDefaultSize() {
            this.limitIconToDefaultSize = true;
            return this;
        }

        public Builder maxIconSize(int maxIconSize) {
            this.maxIconSize = maxIconSize;
            return this;
        }

        public Builder maxIconSizeRes(@DimenRes int maxIconSizeRes) {
            return maxIconSize((int) this.context.getResources().getDimension(maxIconSizeRes));
        }

        public Builder showListener(@NonNull DialogInterface.OnShowListener listener) {
            this.showListener = listener;
            return this;
        }

        public Builder dismissListener(@NonNull DialogInterface.OnDismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public Builder cancelListener(@NonNull DialogInterface.OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }

        public Builder keyListener(@NonNull DialogInterface.OnKeyListener listener) {
            this.keyListener = listener;
            return this;
        }

        public Builder forceStacking(boolean stacked) {
            this.forceStacking = stacked;
            return this;
        }

        public Builder input(@Nullable CharSequence hint, @Nullable CharSequence prefill, boolean allowEmptyInput, @NonNull InputCallback callback) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set content() when you're using a custom view.");
            this.inputCallback = callback;
            this.inputHint = hint;
            this.inputPrefill = prefill;
            this.inputAllowEmpty = allowEmptyInput;
            return this;
        }

        public Builder input(@Nullable CharSequence hint, @Nullable CharSequence prefill, @NonNull InputCallback callback) {
            return input(hint, prefill, true, callback);
        }

        public Builder input(@StringRes int hint, @StringRes int prefill, boolean allowEmptyInput, @NonNull InputCallback callback) {
            return input(hint == 0 ? null : context.getText(hint), prefill == 0 ? null : context.getText(prefill), allowEmptyInput, callback);
        }

        public Builder input(@StringRes int hint, @StringRes int prefill, @NonNull InputCallback callback) {
            return input(hint, prefill, true, callback);
        }

        public Builder inputType(int type) {
            this.inputType = type;
            return this;
        }

        public Builder inputMaxLength(int maxLength) {
            return inputMaxLength(maxLength, 0);
        }

        /**
         * @param errorColor Pass in 0 for the default red error color (as specified in guidelines).
         */
        public Builder inputMaxLength(int maxLength, int errorColor) {
            if (maxLength < 1)
                throw new IllegalArgumentException("Max length for input dialogs cannot be less than 1.");
            this.inputMaxLength = maxLength;
            if (errorColor == 0) {
                inputMaxLengthErrorColor = context.getResources().getColor(R.color.talk_edittext_error);
            } else {
                this.inputMaxLengthErrorColor = errorColor;
            }
            return this;
        }

        /**
         * Same as #{@link #inputMaxLength(int, int)}, but it takes a color resource ID for the error color.
         */
        public Builder inputMaxLengthRes(int maxLength, @ColorRes int errorColor) {
            return inputMaxLength(maxLength, context.getResources().getColor(errorColor));
        }

        public Builder alwaysCallInputCallback() {
            this.alwaysCallInputCallback = true;
            return this;
        }

        @UiThread
        public TalkDialog build() {
            Bundle args = new Bundle();
            args.putSerializable(ARGS_KEY, this);
            return (TalkDialog) Fragment.instantiate(context, TalkDialog.class.getName(), args);
        }

        @UiThread
        public TalkDialog show() {
            TalkDialog dialog = build();
            dialog.show(context.getSupportFragmentManager(), TAG);
            return dialog;
        }

        @UiThread
        public TalkDialog showAllowingStateLoss() {
            TalkDialog dialog = build();
            dialog.showAllowingStateLoss(context.getSupportFragmentManager(), TAG);
            return dialog;
        }
    }
    /**
     * A callback used for regular list dialogs.
     */
    public interface ListCallback {
        void onSelection(TalkDialog dialog, View itemView, int which, CharSequence text);
    }

    /**
     * A callback used for multi choice (check box) list dialogs.
     */
    public interface ListCallbackSingleChoice {
        /**
         * Return true to allow the radio button to be checked, if the alwaysCallSingleChoice() option is used.
         *
         * @param dialog The dialog of which a list item was selected.
         * @param which  The index of the item that was selected.
         * @param text   The text of the  item that was selected.
         * @return True to allow the radio button to be selected.
         */
        boolean onSelection(TalkDialog dialog, View itemView, int which, CharSequence text);
    }

    /**
     * A callback used for multi choice (check box) list dialogs.
     */
    public interface ListCallbackMultiChoice {
        /**
         * Return true to allow the check box to be checked, if the alwaysCallSingleChoice() option is used.
         *
         * @param dialog The dialog of which a list item was selected.
         * @param which  The indices of the items that were selected.
         * @param text   The text of the items that were selected.
         * @return True to allow the checkbox to be selected.
         */
        boolean onSelection(TalkDialog dialog, Integer[] which, CharSequence[] text);
    }

    /**
     * Override these as needed, so no needing to sub empty methods from an interface
     */
    public static abstract class ButtonCallback {

        public void onAny(TalkDialog dialog) {
        }

        public void onPositive(TalkDialog dialog, View v) {
        }

        public void onNegative(TalkDialog dialog) {
        }

        public void onNeutral(TalkDialog dialog) {
        }

        public ButtonCallback() {
            super();
        }

        @Override
        protected final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public final boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        protected final void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public final int hashCode() {
            return super.hashCode();
        }

        @Override
        public final String toString() {
            return super.toString();
        }
    }

    public interface InputCallback {

        void onInput(TalkDialog dialog, CharSequence input);
    }

}
