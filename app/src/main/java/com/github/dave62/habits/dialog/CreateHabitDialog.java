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
import com.github.dave62.habits.constants.Constants;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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


        builder.setTitle("Create your habit")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveHabit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateHabitDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
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
                startDateInput.setText(Constants.DATE_FORMAT.format(calendar.getTime()));
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
        if (isFormValid()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Habit habit = bgRealm.createObject(Habit.class, UUID.randomUUID().toString());
                    habit.setName(nameInput.getText().toString().trim());
                    habit.setStartingDate(calendar.getTime());
                    habit.setTimeThresholdInMin(Integer.parseInt(timeThresholdInput.getText().toString()));
                }
            });
            CreateHabitDialog.this.getDialog().cancel();
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
