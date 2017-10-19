package com.github.dave62.habits.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.dave62.habits.R;
import com.github.dave62.habits.activity.DayRecordActivity;
import com.github.dave62.habits.constants.Constants;
import com.github.dave62.habits.model.DayRecord;
import com.github.dave62.habits.model.Habit;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

//TODO : Factorize this dialog and CreateHabitDialog ?
public class DayRecordDialog extends DialogFragment {

    private View view;
    private Realm realm;
    private Habit currentHabit;
    private DayRecord currentDayRecord;
    private Date selectedDate;

    private EditText minutesInput;

    public static DayRecordDialog newInstance(String habitId, Date selectedDate) {
        DayRecordDialog instance = new DayRecordDialog();
        Bundle args = new Bundle();
        args.putString("habitId", habitId);
        instance.setArguments(args);
        args.putString("selectedDate", Constants.DATE_FORMAT_FOR_BUNDLE.format(selectedDate));
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        currentHabit = realm.where(Habit.class).equalTo("id", getArguments().getString("habitId")).findFirst();
        try {
            selectedDate = Constants.DATE_FORMAT_FOR_BUNDLE.parse(getArguments().getString("selectedDate"));
            currentDayRecord = realm.where(DayRecord.class).equalTo("habit.id", currentHabit.getId()).equalTo("dayOfRecord", selectedDate).findFirst();
        } catch (ParseException e) {
            //We are sur that the date has the right format since it is ours
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_create_day_record, null);
        minutesInput = (EditText) view.findViewById(R.id.minutesInput);
        if (currentDayRecord != null) {
            minutesInput.setText(Integer.toString(currentDayRecord.getTimeSpentInMin()));
        }
        initInputToSubmitFormWithKeyboard();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.time_spent)
                .setView(view)
                .setPositiveButton(R.string.save, null) //We will override this later
                .setNegativeButton(R.string.cancel, null); //The cancel button is just a dismiss that android is doing by himself

        //workaround to dismiss the dialog only when we want to
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitForm();
                    }
                });
            }
        });
        return dialog;
    }

    private void initInputToSubmitFormWithKeyboard() {
        minutesInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitForm();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void submitForm() {
        if (isFormValid()) {
            saveDayRecord();
            //This fragment is only used in a DayRecordActivity. Bit ugly
            ((DayRecordActivity) getActivity()).redrawCalendarMarkers();
            DayRecordDialog.this.dismiss();
        }
    }

    private void saveDayRecord() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                if (currentDayRecord == null) {
                    DayRecord dayRecord = bgRealm.createObject(DayRecord.class, UUID.randomUUID().toString());
                    dayRecord.setDayOfRecord(selectedDate);
                    dayRecord.setTimeSpentInMin(Integer.parseInt(minutesInput.getText().toString()));
                    currentHabit.getRecords().add(dayRecord);
                } else {
                    currentDayRecord.setTimeSpentInMin(Integer.parseInt(minutesInput.getText().toString()));
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = validateEmptyEditText(minutesInput);
        return isFormValid;
    }

    private boolean validateEmptyEditText(EditText editText) {
        editText.setError(null);
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(getString(R.string.empry_field_validation));
            return false;
        }
        return true;
    }

}
