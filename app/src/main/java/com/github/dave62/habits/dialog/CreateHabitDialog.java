package com.github.dave62.habits.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.dave62.habits.R;
import com.github.dave62.habits.model.Habit;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;

public class CreateHabitDialog extends DialogFragment {

    private Realm realm;
    private View view;
    private EditText nameInput;
    private EditText startDateInput;
    private EditText timeThresholdInput;
    private Calendar calendar = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_create_habit, null);
        bindViews();
        initDatePicker();
        initInputToSubmitFormWithKeyboard();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(R.string.create_habit_title)
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

    private void bindViews() {
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        startDateInput = (EditText) view.findViewById(R.id.startDateInput);
        timeThresholdInput = (EditText) view.findViewById(R.id.timeThresholdInput);
    }

    private void initDatePicker() {
        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateInput.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                timeThresholdInput.requestFocus();
            }
        };

        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateHabitDialog.this.getActivity(), onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initInputToSubmitFormWithKeyboard() {
        timeThresholdInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            saveHabit();
            this.dismiss();
        }
    }

    private boolean isFormValid() {
        boolean isFormValid = validateEmptyEditText(nameInput);
        isFormValid &= validateEmptyEditText(startDateInput);
        isFormValid &= validateEmptyEditText(timeThresholdInput);
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

    private void saveHabit() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Habit habit = bgRealm.createObject(Habit.class, UUID.randomUUID().toString());
                habit.setName(nameInput.getText().toString().trim());
                habit.setStartingDate(calendar.getTime());
                habit.setTimeThresholdInMin(Integer.parseInt(timeThresholdInput.getText().toString()));
            }
        });
    }
}
