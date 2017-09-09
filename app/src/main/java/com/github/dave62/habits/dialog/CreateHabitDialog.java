package com.github.dave62.habits.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.dave62.habits.R;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;

@EFragment
public class CreateHabitDialog extends DialogFragment {

    private View view;
    private Realm realm;

    @ViewById
    protected EditText nameInput;
    @ViewById
    protected EditText startDateInput;
    @ViewById
    protected EditText timeThresholdInput;

    protected Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_create_habit, null);

        builder.setTitle(R.string.create_habit_title)
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
                        if (isFormValid()) {
                            saveHabit();
                            CreateHabitDialog.this.dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @AfterViews
    protected void afterViews() {
        initializeDatePicker();
    }

    private void initializeDatePicker() {
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

    protected void saveHabit() {
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
}
