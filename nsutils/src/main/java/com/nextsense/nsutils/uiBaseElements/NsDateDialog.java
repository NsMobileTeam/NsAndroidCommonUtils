package com.nextsense.nsutils.uiBaseElements;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.nextsense.nsutils.baseElements.NsDate;
import com.nextsense.nsutils.listeners.IUniversalListener;

import java.util.Calendar;

@SuppressWarnings("unused")
public class NsDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final NsDate MAX_DATE = new NsDate(126144000000000L);
    public static final NsDate MIN_DATE = new NsDate(-62125920000000L);

    private FragmentManager fragmentManager;
    private IUniversalListener<NsDate> listener;
    private NsDate fromNsDate;
    private final NsDate toNsDate, selectedNsDate;
    private final Calendar calendar;

    private boolean dateTime = false;
    private boolean timeOnly = false;

    private NsDateDialog(@Nullable NsDate fromNsDate, @Nullable NsDate toNsDate, @Nullable NsDate selectedNsDate) {
        this.fromNsDate = fromNsDate != null ? fromNsDate : MIN_DATE;
        this.toNsDate = toNsDate != null ? toNsDate : MAX_DATE;
        this.fromNsDate = this.fromNsDate.before(this.toNsDate) ? this.fromNsDate : MIN_DATE;
        this.selectedNsDate = selectedNsDate != null ? new NsDate(Math.min(Math.max(selectedNsDate.getTime(), MIN_DATE.getTime()), MAX_DATE.getTime())) : new NsDate();
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.selectedNsDate);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return timeOnly ? createTimePicker() : createDatePicker();
    }

    private Dialog createDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, selectedNsDate.year(), selectedNsDate.monthNative(), selectedNsDate.dayOfMonth());

        dialog.getDatePicker().setMaxDate(toNsDate.getTime());
        dialog.getDatePicker().setMinDate(fromNsDate.getTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getDatePicker().setFirstDayOfWeek(2);
        }

        return dialog;
    }

    private Dialog createTimePicker() {
        NsDate time = new NsDate();
        return new TimePickerDialog(getActivity(), this, time.hour(), time.minute(),true);
    }

    private void setListener(IUniversalListener<NsDate> listener) {
        this.listener = listener;
    }

    /**
     * Creates a date picker dialog
     * @param fragmentManager manager for loading the DialogFragment
     * @param fromNsDate minimum allowed date
     * @param toNsDate maximum allowed date
     * @param selectedNsDate initially selected date
     * @param listener date set listener
     */
    public static void showDateDialog(@NonNull FragmentManager fragmentManager, @Nullable NsDate fromNsDate, @Nullable NsDate toNsDate, @Nullable NsDate selectedNsDate, @NonNull IUniversalListener<NsDate> listener) {
        NsDateDialog dialog = new NsDateDialog(fromNsDate, toNsDate, selectedNsDate);
        dialog.fragmentManager = fragmentManager;
        dialog.setListener(listener);
        dialog.show(fragmentManager, "");
    }


    /**
     * Creates a time picker dialog
     * @param fragmentManager manager for loading the DialogFragment
     * @param fromNsDate minimum allowed date
     * @param toNsDate maximum allowed date
     * @param selectedNsDate initially selected date
     * @param listener date set listener
     */
    public static void showTimeDialog(@NonNull FragmentManager fragmentManager, @Nullable NsDate fromNsDate, @Nullable NsDate toNsDate, @Nullable NsDate selectedNsDate, @NonNull IUniversalListener<NsDate> listener) {
        NsDateDialog dialog = new NsDateDialog(fromNsDate, toNsDate, selectedNsDate);
        dialog.fragmentManager = fragmentManager;
        dialog.timeOnly = true;
        dialog.setListener(listener);
        dialog.show(fragmentManager, "");
    }


    /**
     * Creates a date and time picker dialog
     * @param fragmentManager manager for loading the DialogFragment
     * @param fromNsDate minimum allowed date
     * @param toNsDate maximum allowed date
     * @param selectedNsDate initially selected date
     * @param listener date set listener
     */
    public static void showDateTimeDialog(@NonNull FragmentManager fragmentManager, @Nullable NsDate fromNsDate, @Nullable NsDate toNsDate, @Nullable NsDate selectedNsDate, @NonNull IUniversalListener<NsDate> listener) {
        NsDateDialog dialog = new NsDateDialog(fromNsDate, toNsDate, selectedNsDate);
        dialog.fragmentManager = fragmentManager;
        dialog.dateTime = true;
        dialog.setListener(listener);
        dialog.show(fragmentManager, "");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day, 0, 0, 0);
        calendar.setTimeInMillis((calendar.getTimeInMillis() / 1000) * 1000); //set milliseconds to 0
        if (dateTime) {
            showTimeDialog(fragmentManager, fromNsDate, toNsDate, new NsDate(calendar.getTimeInMillis()), listener);
        } else {
            listener.onSuccess(new NsDate(calendar.getTimeInMillis()));
        }
        dismiss();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);
        calendar.setTimeInMillis((calendar.getTimeInMillis() / 1000) * 1000); //set milliseconds to 0
        listener.onSuccess(new NsDate(calendar.getTimeInMillis()));
        dismiss();
    }
}
