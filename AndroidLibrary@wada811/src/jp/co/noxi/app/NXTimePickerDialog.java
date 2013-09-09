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

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

/**
 * {@link android.support.v4.app.DialogFragment} like {@link TimePickerDialog}.
 *
 * @author noxi
 * @see android.app.TimePickerDialog
 */
public class NXTimePickerDialog extends NXDialog implements TimePickerDialog.OnTimeSetListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view      The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute    The minute that was set.
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }


    private static final String ARG_THEME = "theme";
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";
    private static final String ARG_24HOUR = "24hour";
    private static final String ARG_LISTENER = "listener";
    private static final String SAVED_INIT = "nx:timePickerDialog.init";


    /**
     * @param callback     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public static <T extends Fragment & OnTimeSetListener> NXTimePickerDialog newInstance(
            T callback, int hourOfDay, int minute, boolean is24HourView) {
        return newInstanceInternal(VALUE_NULL, callback, hourOfDay, minute, is24HourView);
    }

    /**
     * @param callback     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public static <T extends Activity & OnTimeSetListener> NXTimePickerDialog newInstance(
            T callback, int hourOfDay, int minute, boolean is24HourView) {
        return newInstanceInternal(VALUE_NULL, callback, hourOfDay, minute, is24HourView);
    }

    /**
     * @param theme        the theme to apply to this dialog
     * @param callback     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public static <T extends Fragment & OnTimeSetListener> NXTimePickerDialog newInstance(
            int theme, T callback, int hourOfDay, int minute, boolean is24HourView) {
        return newInstanceInternal(theme, callback, hourOfDay, minute, is24HourView);
    }

    /**
     * @param theme        the theme to apply to this dialog
     * @param callback     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public static <T extends Activity & OnTimeSetListener> NXTimePickerDialog newInstance(
            int theme, T callback, int hourOfDay, int minute, boolean is24HourView) {
        return newInstanceInternal(theme, callback, hourOfDay, minute, is24HourView);
    }

    static NXTimePickerDialog newInstanceInternal(int theme, OnTimeSetListener callback,
                                                  int hourOfDay, int minute, boolean is24HourView) {
        final Bundle args = new Bundle();
        args.putInt(ARG_THEME, theme);
        args.putInt(ARG_HOUR, hourOfDay);
        args.putInt(ARG_MINUTE, minute);
        args.putBoolean(ARG_24HOUR, is24HourView);
        saveState(args, ARG_LISTENER, callback);

        NXTimePickerDialog dialog = new NXTimePickerDialog();
        dialog.setArguments(args);
        dialog.mInitFlag = true;
        return dialog;
    }


    TimePickerDialog mDialog;
    boolean mInitFlag;

    public NXTimePickerDialog() {
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
            throw new RuntimeException("User NXTimePickerDialog#newInstance");

        final Bundle args = getArguments();
        final int theme = args.getInt(ARG_THEME);
        final int hourOfDay = args.getInt(ARG_HOUR);
        final int minute = args.getInt(ARG_MINUTE);
        final boolean is24HourView = args.getBoolean(ARG_24HOUR);

        if (theme == VALUE_NULL) {
            return mDialog = new TimePickerDialog(getActivity(), this, hourOfDay, minute, is24HourView);
        } else {
            return mDialog = new TimePickerDialog(getActivity(), theme, this, hourOfDay, minute, is24HourView);
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
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!isResumed())
            return;
        final OnTimeSetListener listener = findListenerByTag(
                OnTimeSetListener.class, getArguments(), ARG_LISTENER);
        if (listener != null)
            listener.onTimeSet(view, hourOfDay, minute);
    }

    protected void setIsInitialize() {
        mInitFlag = true;
    }

    public void updateTime(int hourOfDay, int minute) {
        if (mDialog != null)
            mDialog.updateTime(hourOfDay, minute);
    }
}
