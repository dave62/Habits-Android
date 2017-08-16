package com.github.dave62.habits.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.dave62.habits.R;
import com.github.dave62.habits.constants.Constants;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;

@EActivity(R.layout.activity_create_habit)
public class CreateHabitActivity extends AppCompatActivity {

    private Realm realm;

    @ViewById
    protected EditText nameInput;
    @ViewById
    protected EditText startDateInput;
    @ViewById
    protected EditText timeThresholdInput;


    protected Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
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
            }
        };

        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateHabitActivity.this, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    @Click(R.id.saveBtn)
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
            HabitsListActivity_.intent(this).start();
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


}
