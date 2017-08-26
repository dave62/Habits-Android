package com.github.dave62.habits.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dave62.habits.R;
import com.github.dave62.habits.constants.Constants;
import com.github.dave62.habits.model.DayRecord;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

@EFragment
public class DayRecordDialog extends DialogFragment {

    private View view;
    private Realm realm;
    private Habit currentHabit;
    private DayRecord currentDayRecord;
    private Date selectedDate;

    @ViewById
    protected EditText minutesInput;

    public static DayRecordDialog newInstance(String habitId, Date selectedDate) {
        DayRecordDialog instance = new DayRecordDialog_();
        Bundle args = new Bundle();
        args.putString("habitId", habitId);
        instance.setArguments(args);
        args.putString("selectedDate", Constants.DATE_FORMAT.format(selectedDate));
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        currentHabit = realm.where(Habit.class).equalTo("id", getArguments().getString("habitId")).findFirst();
        try {
            selectedDate = Constants.DATE_FORMAT.parse(getArguments().getString("selectedDate"));
            DayRecord searchExistingRecord = realm.where(DayRecord.class).equalTo("habit.id", currentHabit.getId()).equalTo("dayOfRecord", selectedDate).findFirst();
            currentDayRecord = searchExistingRecord != null ? searchExistingRecord : null;
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            this.getDialog().cancel();
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_create_day_record, null);

        builder.setTitle("How much time have you spent ?")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveDayRecord();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DayRecordDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    protected void saveDayRecord() {
        if (isFormValid()) {
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
            DayRecordDialog.this.getDialog().cancel();
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    @AfterViews
    protected void afterViews() {
        if (currentDayRecord != null) {
            minutesInput.setText(Integer.toString(currentDayRecord.getTimeSpentInMin()));
        }
    }

    private boolean isFormValid() {
        boolean isFormValid = validateEmptyEditText(minutesInput);
        return isFormValid;
    }

    private boolean validateEmptyEditText(EditText editText) {
        editText.setError(null);
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError("This field can't be empty");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }
}
