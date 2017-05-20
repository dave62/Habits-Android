package com.github.dave62.habits.activity;

import android.app.DatePickerDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.dave62.habits.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@EActivity(R.layout.activity_create_habit)
public class CreateHabitActivity extends AppCompatActivity {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy", Locale.US);
    private final static int BASIC_POSITION = 0;
    private final static int CUSTOM_POSITION = 1;

    @ViewById
    protected EditText startDateInput;
    @ViewById
    protected Spinner habitTypeSpinner;
    @ViewById
    protected ConstraintLayout howContainer;
    @ViewById
    protected Spinner periodicitySpinner;

    protected Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void afterViews() {
        howContainer.setVisibility(View.GONE);
        initializeDatePicker();
        initializeTypesSpinner();
        initializePeriodicitiesSpinner();
    }

    private void initializeDatePicker() {
        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateInput.setText(DATE_FORMAT.format(calendar.getTime()));
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

    private void initializeTypesSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.habit_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitTypeSpinner.setAdapter(adapter);

        habitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case BASIC_POSITION:
                        hideHowContainer();
                        break;
                    case CUSTOM_POSITION:
                        showHowContainer();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing to do
            }
        });
    }

    private void hideHowContainer() {
        if (howContainer.isShown()) {
            howContainer.setVisibility(View.GONE);
        }
    }

    private void showHowContainer() {
        if (!howContainer.isShown()) {
            howContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initializePeriodicitiesSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.periodicity_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodicitySpinner.setAdapter(adapter);
    }




}
