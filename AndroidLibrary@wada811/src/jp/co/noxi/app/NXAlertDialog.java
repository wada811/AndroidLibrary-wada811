/*
 * Copyright (C) 2013 noxi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.noxi.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListAdapter;

/**
 * {@link android.support.v4.app.DialogFragment} like {@link AlertDialog.Builder}
 *
 * @author noxi
 */
public class NXAlertDialog extends NXDialog {


    /**
     * Delegate for list adapter bindings.
     */
    interface ListAdapterDelegate {
        ListAdapter getListAdapter(NXDialogInterface dialog);
    }

    /**
     * Delegate for custom title view.
     */
    interface TitleViewDelegate {
        View getTitleView(NXDialogInterface dialog);
    }

    /**
     * Special theme constant for {@link Builder#Builder(Context, int)}: use
     * the traditional (pre-Holo) alert dialog theme.
     */
    public static final int THEME_TRADITIONAL = AlertDialog.THEME_TRADITIONAL;

    /**
     * Special theme constant for {@link Builder#Builder(Context, int)}: use
     * the holographic alert theme with a dark background.
     */
    public static final int THEME_HOLO_DARK = AlertDialog.THEME_HOLO_DARK;

    /**
     * Special theme constant for {@link Builder#Builder(Context, int)}: use
     * the holographic alert theme with a light background.
     */
    public static final int THEME_HOLO_LIGHT = AlertDialog.THEME_HOLO_LIGHT;

    /**
     * Special theme constant for {@link Builder#Builder(Context, int)}: use
     * the device's default alert theme with a dark background.
     */
    public static final int THEME_DEVICE_DEFAULT_DARK = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

    /**
     * Special theme constant for {@link Builder#Builder(Context, int)}: use
     * the device's default alert theme with a dark background.
     */
    public static final int THEME_DEVICE_DEFAULT_LIGHT = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;


    private static final String ARG_THEME = "theme";
    private static final String ARG_ICON = "icon";
    private static final String ARG_INVERSE_BACKGROUND = "inverseBackground";
    private static final String ARG_TITLE = "title";
    private static final String ARG_CUSTOM_TITLE = "customTitle";
    private static final String ARG_MESSAGE = "message";

    private static final String ARG_ITEMS = "items";
    private static final String ARG_ITEMS_LISTENER = "itemsListener";

    private static final String ARG_ADAPTER = "adapter";
    private static final String ARG_ADAPTER_LISTENER = "adapterListener";

    private static final String ARG_CHECKED_ITEMS = "checkedItems";
    private static final String ARG_MULTI_CHOICE_ITEMS = "multiChoiceItems";
    private static final String ARG_MULTI_CHOICE_LISTENER = "multiChoiceListener";

    private static final String ARG_CHECKED_ITEM = "checkedItem";
    private static final String ARG_SINGLE_CHOICE_ITEMS = "singleChoiceItems";
    private static final String ARG_SINGLE_CHOICE_ADAPTER = "singleChoiceAdapter";
    private static final String ARG_SINGLE_CHOICE_LISTENER = "singleChoiceListener";

    private static final String ARG_NEGATIVE_BUTTON = "negative";
    private static final String ARG_NEGATIVE_BUTTON_LISTENER = "negativeListener";

    private static final String ARG_NEUTRAL_BUTTON = "neutral";
    private static final String ARG_NEUTRAL_BUTTON_LISTENER = "neutralListener";

    private static final String ARG_POSITIVE_BUTTON = "positive";
    private static final String ARG_POSITIVE_BUTTON_LISTENER = "positiveListener";

    private static final String ARG_VIEW = "view";

    private static final String ARG_CANCEL_LISTENER = "cancelListener";
    private static final String ARG_DISMISS_LISTENER = "dismissListener";
    private static final String ARG_KEY_LISTENER = "keyListener";


    public static class Builder {

        private final Context mContext;
        private final Bundle mArguments = new Bundle();
        private boolean mCancelable = true;
        private Bundle mExtra;

        /**
         * Constructor using a context for this builder and the {@link NXAlertDialog} it creates.
         */
        public Builder(Context context) {
            this(context, VALUE_NULL);
        }

        /**
         * Constructor using a context and theme for this builder and
         * the {@link NXAlertDialog} it creates.  The actual theme
         * that an AlertDialog uses is a private implementation, however you can
         * here supply either the name of an attribute in the theme from which
         * to get the dialog's style (such as {@link android.R.attr#alertDialogTheme}
         * or one of the constants
         * {@link NXAlertDialog#THEME_TRADITIONAL},
         * {@link NXAlertDialog#THEME_HOLO_DARK}, or
         * {@link NXAlertDialog#THEME_HOLO_LIGHT}.
         */
        public Builder(Context context, int theme) {
            mContext = context.getApplicationContext();
            mArguments.putInt(ARG_THEME, theme);
        }

        /**
         * Creates a {@link NXAlertDialog} with the arguments supplied to this builder. It does not
         * {@link NXDialog#show} the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use {@link #show} if you don't have any other processing
         * to do and want this to be created and displayed.
         */
        public NXAlertDialog create() {
            final NXAlertDialog f = new NXAlertDialog();
            f.setFromBuilder(true);
            f.setArguments(mArguments);
            if (!mCancelable)
                f.setCancelable(false);
            if (mExtra != null)
                f.setExtra(mExtra);
            return f;
        }

        /**
         * Creates a {@link NXAlertDialog} with the arguments supplied to this builder and
         * {@link NXDialog#show}'s the dialog.
         */
        public void show(FragmentManager manager, String tag) {
            create().show(manager, tag);
        }

        /**
         * Creates a {@link NXAlertDialog} with the arguments supplied to this builder and
         * {@link NXDialog#show}'s the dialog.
         */
        public void show(FragmentTransaction transaction, String tag) {
            create().show(transaction, tag);
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * Set the resource id of the {@link android.graphics.drawable.Drawable} to be used in the title.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIcon(int iconId) {
            mArguments.putInt(ARG_ICON, iconId);
            return this;
        }

        /**
         * Set an icon as supplied by a theme attribute. e.g. android.R.attr.alertDialogIcon
         *
         * @param attrId ID of a theme attribute that points to a drawable resource.
         */
        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            mContext.getTheme().resolveAttribute(attrId, out, true);
            setIcon(out.resourceId);
            return this;
        }

        /**
         * Sets the Dialog to use the inverse background, regardless of what the
         * contents is.
         *
         * @param useInverseBackground Whether to use the inverse background
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            mArguments.putInt(ARG_INVERSE_BACKGROUND, useInverseBackground ? VALUE_TRUE : VALUE_FALSE);
            return this;
        }

        /**
         * Sets an extra object.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see NXDialog#setExtra(android.os.Bundle)
         */
        public Builder setExtra(Bundle extra) {
            mExtra = extra;
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(CharSequence title) {
            mArguments.putCharSequence(ARG_TITLE, title);
            return this;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int resId) {
            mArguments.putCharSequence(ARG_TITLE, mContext.getText(resId));
            return this;
        }

        /**
         * Set the title using the custom view {@code customTitleView}. The
         * methods {@link #setTitle(int)} and {@link #setIcon(int)} should be
         * sufficient for most titles, but this is provided if the title needs
         * more customization. Using this will replace the title and icon set
         * via the other methods.
         *
         * @param customTitleView The custom view to use as the title.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & TitleViewDelegate> Builder setCustomTitle(T customTitleView) {
            putArgument(ARG_CUSTOM_TITLE, customTitleView);
            return this;
        }

        /**
         * Set the title using the custom view {@code customTitleView}. The
         * methods {@link #setTitle(int)} and {@link #setIcon(int)} should be
         * sufficient for most titles, but this is provided if the title needs
         * more customization. Using this will replace the title and icon set
         * via the other methods.
         *
         * @param customTitleView The custom view to use as the title.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & TitleViewDelegate> Builder setCustomTitle(T customTitleView) {
            putArgument(ARG_CUSTOM_TITLE, customTitleView);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(CharSequence message) {
            mArguments.putCharSequence(ARG_MESSAGE, message);
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(int resId) {
            return setMessage(mContext.getText(resId));
        }

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied view is an instance
         * of a {@link android.widget.ListView} the light background will be used.
         *
         * @param view The {@link ViewDelegate} to use as the contents of the Dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & ViewDelegate> Builder setView(T view) {
            putArgument(ARG_VIEW, view);
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied view is an instance
         * of a {@link android.widget.ListView} the light background will be used.
         *
         * @param view The {@link ViewDelegate} to use as the contents of the Dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & ViewDelegate> Builder setView(T view) {
            putArgument(ARG_VIEW, view);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnChoiceClickListener> Builder setItems(CharSequence[] items, T listener) {
            mArguments.putCharSequenceArray(ARG_ITEMS, items);
            putArgument(ARG_ITEMS_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener. This should be an array type i.e. R.array.foo
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnChoiceClickListener> Builder setItems(int itemsId, T listener) {
            return setItems(mContext.getResources().getTextArray(itemsId), listener);
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnChoiceClickListener> Builder setItems(CharSequence[] items, T listener) {
            mArguments.putCharSequenceArray(ARG_ITEMS, items);
            putArgument(ARG_ITEMS_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener. This should be an array type i.e. R.array.foo
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */

        public <T extends Activity & OnChoiceClickListener> Builder setItems(int itemsId, T listener) {
            return setItems(mContext.getResources().getTextArray(itemsId), listener);
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter  The {@link ListAdapterDelegate} to supply the list of items
         * @param listener The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Fragment & ListAdapterDelegate,
                TListener extends Fragment & OnChoiceClickListener> Builder setAdapter(
                TAdapter adapter, TListener listener) {
            putArgument(ARG_ADAPTER, adapter);
            putArgument(ARG_ADAPTER_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter  The {@link ListAdapterDelegate} to supply the list of items
         * @param listener The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Activity & ListAdapterDelegate,
                TListener extends Activity & OnChoiceClickListener> Builder setAdapter(
                TAdapter adapter, TListener listener) {
            putArgument(ARG_ADAPTER, adapter);
            putArgument(ARG_ADAPTER_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter  The {@link ListAdapterDelegate} to supply the list of items
         * @param listener The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Fragment & ListAdapterDelegate,
                TListener extends Activity & OnChoiceClickListener> Builder setAdapter(
                TAdapter adapter, TListener listener) {
            putArgument(ARG_ADAPTER, adapter);
            putArgument(ARG_ADAPTER_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter  The {@link ListAdapterDelegate} to supply the list of items
         * @param listener The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Activity & ListAdapterDelegate,
                TListener extends Fragment & OnChoiceClickListener> Builder setAdapter(
                TAdapter adapter, TListener listener) {
            putArgument(ARG_ADAPTER, adapter);
            putArgument(ARG_ADAPTER_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items        the text of the items to be displayed in the list.
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnMultiChoiceClickListener> Builder setMultiChoiceItems(
                CharSequence[] items, boolean[] checkedItems, T listener) {
            mArguments.putCharSequenceArray(ARG_MULTI_CHOICE_ITEMS, items);
            mArguments.putBooleanArray(ARG_CHECKED_ITEMS, checkedItems);
            putArgument(ARG_MULTI_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * This should be an array type, e.g. R.array.foo. The list will have
         * a check mark displayed to the right of the text for each checked
         * item. Clicking on an item in the list will not dismiss the dialog.
         * Clicking on a button will dismiss the dialog.
         *
         * @param itemsId      the resource id of an array i.e. R.array.foo
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnMultiChoiceClickListener> Builder setMultiChoiceItems(
                int itemsId, boolean[] checkedItems, T listener) {
            return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItems, listener);
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items        the text of the items to be displayed in the list.
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnMultiChoiceClickListener> Builder setMultiChoiceItems(
                CharSequence[] items, boolean[] checkedItems, T listener) {
            mArguments.putCharSequenceArray(ARG_MULTI_CHOICE_ITEMS, items);
            mArguments.putBooleanArray(ARG_CHECKED_ITEMS, checkedItems);
            putArgument(ARG_MULTI_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * This should be an array type, e.g. R.array.foo. The list will have
         * a check mark displayed to the right of the text for each checked
         * item. Clicking on an item in the list will not dismiss the dialog.
         * Clicking on a button will dismiss the dialog.
         *
         * @param itemsId      the resource id of an array i.e. R.array.foo
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnMultiChoiceClickListener> Builder setMultiChoiceItems(
                int itemsId, boolean[] checkedItems, T listener) {
            return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItems, listener);
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items       the items to be displayed.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnChoiceClickListener> Builder setSingleChoiceItems(
                CharSequence[] items, int checkedItem, T listener) {
            mArguments.putCharSequenceArray(ARG_SINGLE_CHOICE_ITEMS, items);
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. This should be an array type i.e.
         * R.array.foo The list will have a check mark displayed to the right of the text for the
         * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
         * button will dismiss the dialog.
         *
         * @param itemsId     the resource id of an array i.e. R.array.foo
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnChoiceClickListener> Builder setSingleChoiceItems(
                int itemsId, int checkedItem, T listener) {
            return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItem, listener);
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items       the items to be displayed.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnChoiceClickListener> Builder setSingleChoiceItems(
                CharSequence[] items, int checkedItem, T listener) {
            mArguments.putCharSequenceArray(ARG_SINGLE_CHOICE_ITEMS, items);
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. This should be an array type i.e.
         * R.array.foo The list will have a check mark displayed to the right of the text for the
         * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
         * button will dismiss the dialog.
         *
         * @param itemsId     the resource id of an array i.e. R.array.foo
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnChoiceClickListener> Builder setSingleChoiceItems(
                int itemsId, int checkedItem, T listener) {
            return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItem, listener);
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter     The {@link ListAdapterDelegate} to supply the list of items
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Fragment & ListAdapterDelegate,
                TListener extends Fragment & OnChoiceClickListener> Builder setSingleChoiceItems(
                TAdapter adapter, int checkedItem, TListener listener) {
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_ADAPTER, adapter);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter     The {@link ListAdapterDelegate} to supply the list of items
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Activity & ListAdapterDelegate,
                TListener extends Activity & OnChoiceClickListener> Builder setSingleChoiceItems(
                TAdapter adapter, int checkedItem, TListener listener) {
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_ADAPTER, adapter);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter     The {@link ListAdapterDelegate} to supply the list of items
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Fragment & ListAdapterDelegate,
                TListener extends Activity & OnChoiceClickListener> Builder setSingleChoiceItems(
                TAdapter adapter, int checkedItem, TListener listener) {
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_ADAPTER, adapter);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter     The {@link ListAdapterDelegate} to supply the list of items
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <TAdapter extends Activity & ListAdapterDelegate,
                TListener extends Fragment & OnChoiceClickListener> Builder setSingleChoiceItems(
                TAdapter adapter, int checkedItem, TListener listener) {
            mArguments.putInt(ARG_CHECKED_ITEM, checkedItem);
            putArgument(ARG_SINGLE_CHOICE_ADAPTER, adapter);
            putArgument(ARG_SINGLE_CHOICE_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setNegativeButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_NEGATIVE_BUTTON, text);
            putArgument(ARG_NEGATIVE_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setNegativeButton(int textId, T listener) {
            return setNegativeButton(mContext.getText(textId), listener);
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setNegativeButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_NEGATIVE_BUTTON, text);
            putArgument(ARG_NEGATIVE_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setNegativeButton(int textId, T listener) {
            return setNegativeButton(mContext.getText(textId), listener);
        }

        /**
         * Set a negative button.
         *
         * @param text The text to display in the negative button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(CharSequence text) {
            mArguments.putCharSequence(ARG_NEGATIVE_BUTTON, text);
            return this;
        }

        /**
         * Set a negative button.
         *
         * @param textId The resource id of the text to display in the negative button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(int textId) {
            return setNegativeButton(mContext.getText(textId));
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text     The text to display in the neutral button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setNeutralButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_NEUTRAL_BUTTON, text);
            putArgument(ARG_NEUTRAL_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the neutral button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setNeutralButton(int textId, T listener) {
            return setNeutralButton(mContext.getText(textId), listener);
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text     The text to display in the neutral button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setNeutralButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_NEUTRAL_BUTTON, text);
            putArgument(ARG_NEUTRAL_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the neutral button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setNeutralButton(int textId, T listener) {
            return setNeutralButton(mContext.getText(textId), listener);
        }

        /**
         * Set a neutral button.
         *
         * @param text The resource id of the text to display in the neutral button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(CharSequence text) {
            mArguments.putCharSequence(ARG_NEUTRAL_BUTTON, text);
            return this;
        }

        /**
         * Set a positive button.
         *
         * @param textId The resource id of the text to display in the neutral button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(int textId) {
            return setNeutralButton(mContext.getText(textId));
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setPositiveButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_POSITIVE_BUTTON, text);
            putArgument(ARG_POSITIVE_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnClickListener> Builder setPositiveButton(int textId, T listener) {
            return setPositiveButton(mContext.getText(textId), listener);
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setPositiveButton(CharSequence text, T listener) {
            mArguments.putCharSequence(ARG_POSITIVE_BUTTON, text);
            putArgument(ARG_POSITIVE_BUTTON_LISTENER, listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnClickListener> Builder setPositiveButton(int textId, T listener) {
            return setPositiveButton(mContext.getText(textId), listener);
        }

        /**
         * Set a positive button.
         *
         * @param text The text to display in the positive button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(CharSequence text) {
            mArguments.putCharSequence(ARG_POSITIVE_BUTTON, text);
            return this;
        }

        /**
         * Set a positive button.
         *
         * @param textId The resource id of the text to display in the positive button
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(int textId) {
            return setPositiveButton(mContext.getText(textId));
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p/>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.support.v4.app.Fragment)}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.support.v4.app.Fragment)
         */
        public <T extends Fragment & OnCancelListener> Builder setOnCancelListener(T listener) {
            putArgument(ARG_CANCEL_LISTENER, listener);
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p/>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.app.Activity)}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.app.Activity)
         */
        public <T extends Activity & OnCancelListener> Builder setOnCancelListener(T listener) {
            putArgument(ARG_CANCEL_LISTENER, listener);
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnDismissListener> Builder setOnDismissListener(T listener) {
            putArgument(ARG_DISMISS_LISTENER, listener);
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnDismissListener> Builder setOnDismissListener(T listener) {
            putArgument(ARG_DISMISS_LISTENER, listener);
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Fragment & OnKeyListener> Builder setOnKeyListener(T listener) {
            putArgument(ARG_KEY_LISTENER, listener);
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public <T extends Activity & OnKeyListener> Builder setOnKeyListener(T listener) {
            putArgument(ARG_KEY_LISTENER, listener);
            return this;
        }


        //
        // Helper
        //

        private void putArgument(String key, Fragment fragment) {
            if (fragment != null && fragment.getTag() != null)
                mArguments.putString(key, TAG_FRAGMENT + fragment.getTag());
        }

        private void putArgument(String key, Activity activity) {
            if (activity != null)
                mArguments.putString(key, TAG_ACTIVITY);
        }
    }


    private static final String SAVED_BUILDER_FLAG = "nx:alertDialog.builder";

    boolean mFromBuilder;

    public NXAlertDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            return;

        mFromBuilder = savedInstanceState.getBoolean(SAVED_BUILDER_FLAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!mFromBuilder)
            throw new RuntimeException("Use NXAlertDialog$Builder");

        final Bundle args = getArguments();
        final int theme = args.getInt(ARG_THEME);

        AlertDialog.Builder builder;
        if (theme == VALUE_NULL || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            builder = newDialogBuilder(theme);
        }

        final CharSequence title = args.getCharSequence(ARG_TITLE);
        if (title == null) {
            setCustomTitle(builder);
        } else {
            builder.setTitle(title);
        }

        final CharSequence message = args.getCharSequence(ARG_MESSAGE);
        if (message != null)
            builder.setMessage(message);

        final int iconId = args.getInt(ARG_ICON, VALUE_NULL);
        if (iconId != VALUE_NULL)
            builder.setIcon(iconId);

        final int useInverseBackground = args.getInt(ARG_INVERSE_BACKGROUND);
        if (useInverseBackground != VALUE_NULL)
            builder.setInverseBackgroundForced(useInverseBackground == VALUE_TRUE);

        // View
        setView(builder);

        // List
        setItems(builder);
        setAdapter(builder);
        setMultiChoiceItems(builder);
        setSingleChoiceItems(builder);

        // Buttons
        setPositiveButton(builder);
        setNegativeButton(builder);
        setNeutralButton(builder);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_BUILDER_FLAG, mFromBuilder);
    }

    protected void setFromBuilder(boolean fromBuilder) {
        mFromBuilder = fromBuilder;
    }

    private void setCustomTitle(AlertDialog.Builder builder) {
        final TitleViewDelegate delegate = findListenerByTag(
                TitleViewDelegate.class, ARG_CUSTOM_TITLE);
        if (delegate == null)
            return;
        builder.setCustomTitle(delegate.getTitleView(this));
    }

    private void setView(AlertDialog.Builder builder) {
        final ViewDelegate delegate = findListenerByTag(ViewDelegate.class, ARG_VIEW);
        if (delegate == null)
            return;
        builder.setView(delegate.getView(this));
    }

    private void setAdapter(AlertDialog.Builder builder) {
        final ListAdapterDelegate delegate = findListenerByTag(ListAdapterDelegate.class, ARG_ADAPTER);
        if (delegate == null)
            return;
        builder.setAdapter(delegate.getListAdapter(this), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnChoiceClickListener listener = findListenerByTag(
                        OnChoiceClickListener.class, ARG_ADAPTER_LISTENER);
                listener.onClick(NXAlertDialog.this, which);
            }
        });
    }

    private void setSingleChoiceItems(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence[] items = args.getCharSequenceArray(ARG_SINGLE_CHOICE_ITEMS);
        final int checkedItem = args.getInt(ARG_CHECKED_ITEM);
        if (items != null) {
            builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final OnChoiceClickListener listener = findListenerByTag(
                            OnChoiceClickListener.class, ARG_SINGLE_CHOICE_LISTENER);
                    if (listener != null)
                        listener.onClick(NXAlertDialog.this, which);
                }
            });
            return;
        }

        final ListAdapterDelegate delegate = findListenerByTag(
                ListAdapterDelegate.class, ARG_SINGLE_CHOICE_ADAPTER);
        if (delegate == null)
            return;
        builder.setSingleChoiceItems(delegate.getListAdapter(this),
                checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnChoiceClickListener listener = findListenerByTag(
                        OnChoiceClickListener.class, ARG_SINGLE_CHOICE_LISTENER);
                if (listener != null)
                    listener.onClick(NXAlertDialog.this, which);
            }
        });
    }

    private void setPositiveButton(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence positiveButtonText = args.getCharSequence(ARG_POSITIVE_BUTTON);
        if (positiveButtonText == null)
            return;
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnClickListener listener = findListenerByTag(
                        OnClickListener.class, ARG_POSITIVE_BUTTON_LISTENER);
                if (listener != null)
                    listener.onClickPositive(NXAlertDialog.this);
            }
        });
    }

    private void setNeutralButton(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence naturalButtonText = args.getCharSequence(ARG_NEUTRAL_BUTTON);
        if (naturalButtonText == null)
            return;
        builder.setNeutralButton(naturalButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnClickListener listener = findListenerByTag(
                        OnClickListener.class, ARG_NEUTRAL_BUTTON_LISTENER);
                if (listener != null)
                    listener.onClickNeutral(NXAlertDialog.this);
            }
        });
    }

    private void setNegativeButton(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence negativeButtonText = args.getCharSequence(ARG_NEGATIVE_BUTTON);
        if (negativeButtonText == null)
            return;
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnClickListener listener = findListenerByTag(
                        OnClickListener.class, ARG_NEGATIVE_BUTTON_LISTENER);
                if (listener != null)
                    listener.onClickNegative(NXAlertDialog.this);
            }
        });
    }

    private void setItems(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence[] items = args.getCharSequenceArray(ARG_ITEMS);
        if (items == null)
            return;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final OnChoiceClickListener listener =
                        findListenerByTag(OnChoiceClickListener.class, ARG_ITEMS_LISTENER);
                if (listener != null)
                    listener.onClick(NXAlertDialog.this, which);
            }
        });
    }

    private void setMultiChoiceItems(AlertDialog.Builder builder) {
        final Bundle args = getArguments();
        final CharSequence[] items = args.getCharSequenceArray(ARG_MULTI_CHOICE_ITEMS);
        final boolean[] checked = args.getBooleanArray(ARG_CHECKED_ITEMS);
        if (items == null || checked == null || items.length != checked.length)
            return;
        builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                final OnMultiChoiceClickListener listener =
                        findListenerByTag(OnMultiChoiceClickListener.class, ARG_MULTI_CHOICE_LISTENER);
                if (listener != null)
                    listener.onClick(NXAlertDialog.this, which, isChecked);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AlertDialog.Builder newDialogBuilder(int theme) {
        return new AlertDialog.Builder(getActivity(), theme);
    }

    /**
     * Find listener/delegate from {@link #getArguments()}.
     */
    private <T> T findListenerByTag(Class<T> clss, String argName) {
        return findListenerByTag(clss, getArguments(), argName);
    }
}
