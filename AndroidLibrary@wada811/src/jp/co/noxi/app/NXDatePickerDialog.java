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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

/**
 * {@link android.support.v4.app.DialogFragment} like {@link DatePickerDialog}.
 *
 * @author noxi
 * @see android.app.DatePickerDialog
 */
public class NXDatePickerDialog extends NXDialog implements DatePickerDialog.OnDateSetListener {

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        /**
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }


    private static final String ARG_LISTENER = "listener";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    private static final String ARG_THEME = "theme";
    private static final String SAVED_INIT = "nx:datePickerDialog.init";


    /**
     * @param callback How the parent is notified that the date is set.
     * @param year     The initial year of the dialog.
     * @param month    The initial month of the dialog.
     * @param day      The initial day of the dialog.
     */
    public static <T extends Fragment & OnDateSetListener> NXDatePickerDialog newInstance(
            T callback, int year, int month, int day) {
        return newInstanceInternal(callback, VALUE_NULL, year, month, day);
    }

    /**
     * @param callback How the parent is notified that the date is set.
     * @param year     The initial year of the dialog.
     * @param month    The initial month of the dialog.
     * @param day      The initial day of the dialog.
     */
    public static <T extends Activity & OnDateSetListener> NXDatePickerDialog newInstance(
            T callback, int year, int month, int day) {
        return newInstanceInternal(callback, VALUE_NULL, year, month, day);
    }

    /**
     * @param theme    the theme to apply to this dialog
     * @param callback How the parent is notified that the date is set.
     * @param year     The initial year of the dialog.
     * @param month    The initial month of the dialog.
     * @param day      The initial day of the dialog.
     */
    public static <T extends Fragment & OnDateSetListener> NXDatePickerDialog newInstance(
            T callback, int theme, int year, int month, int day) {
        return newInstanceInternal(callback, theme, year, month, day);
    }

    /**
     * @param theme    the theme to apply to this dialog
     * @param callback How the parent is notified that the date is set.
     * @param year     The initial year of the dialog.
     * @param month    The initial month of the dialog.
     * @param day      The initial day of the dialog.
     */
    public static <T extends Activity & OnDateSetListener> NXDatePickerDialog newInstance(
            T callback, int theme, int year, int month, int day) {
        return newInstanceInternal(callback, theme, year, month, day);
    }

    static NXDatePickerDialog newInstanceInternal(
            OnDateSetListener callback, int theme, int year, int month, int day) {
        final Bundle args = new Bundle();
        args.putInt(ARG_THEME, theme);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        saveState(args, ARG_LISTENER, callback);

        final NXDatePickerDialog dialog = new NXDatePickerDialog();
        dialog.setArguments(args);
        dialog.setIsInitialize();
        return dialog;
    }


    DatePickerDialog mDialog;
    boolean mInitFlag;

    public NXDatePickerDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            return;
        mInitFlag = savedInstanceState.getBoolean(SAVED_INIT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!mInitFlag)
            throw new RuntimeException("Use NXDatePickerDialog#newInstance");

        final Bundle args = getArguments();
        final int theme = args.getInt(ARG_THEME);
        final int year = args.getInt(ARG_YEAR);
        final int month = args.getInt(ARG_MONTH);
        final int day = args.getInt(ARG_DAY);

        if (theme == VALUE_NULL) {
            return mDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        } else {
            return mDialog = new DatePickerDialog(getActivity(), theme, this, year, month, day);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_INIT, mInitFlag);
    }

    @Override
    public void onDestroyView() {
        mDialog = null;
        super.onDestroyView();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (!isResumed())
            return;
        final OnDateSetListener listener = findListenerByTag(
                OnDateSetListener.class, getArguments(), ARG_LISTENER);
        if (listener != null)
            listener.onDateSet(view, year, monthOfYear, dayOfMonth);
    }

    protected void setIsInitialize() {
        mInitFlag = true;
    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public DatePicker getDatePicker() {
        if (mDialog == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return getDatePickerHC();

        final Window window = mDialog.getWindow();
        if (window == null)
            return null;
        return findDatePicker(window.getDecorView());
    }

    DatePicker findDatePicker(View view) {
        if (view == null)
            return null;
        if (view instanceof DatePicker)
            return (DatePicker) view;
        if (!(view instanceof ViewGroup))
            return null;

        final ViewGroup viewGroup = (ViewGroup) view;
        final int count = viewGroup.getChildCount();
        if (count == 0)
            return null;
        for (int i = 0; i < count; i++) {
            DatePicker datePicker = findDatePicker(viewGroup.getChildAt(i));
            if (datePicker != null)
                return datePicker;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    DatePicker getDatePickerHC() {
        return mDialog.getDatePicker();
    }

    /**
     * Sets the current date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        if (mDialog != null)
            mDialog.updateDate(year, monthOfYear, dayOfMonth);
    }
}
