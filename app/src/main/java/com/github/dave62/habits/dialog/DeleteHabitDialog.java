package com.github.dave62.habits.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.EFragment;

import io.realm.Realm;

@EFragment
public class DeleteHabitDialog extends DialogFragment {

    private Realm realm;
    private Habit habit;

    public static DeleteHabitDialog newInstance(String habitId) {
        DeleteHabitDialog f = new DeleteHabitDialog();
        Bundle args = new Bundle();
        args.putString("habitId", habitId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        habit = realm.where(Habit.class).equalTo("id", getArguments().getString("habitId")).findFirst();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Deletion")
                .setMessage("Do you want to delete this habit ? \n" +
                        "(" + habit.getName() + ")")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                habit.deleteFromRealm();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteHabitDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
