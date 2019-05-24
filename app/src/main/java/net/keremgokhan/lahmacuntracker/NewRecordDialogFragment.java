package net.keremgokhan.lahmacuntracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import net.keremgokhan.lahmacuntracker.db.AppDatabase;
import net.keremgokhan.lahmacuntracker.db.dao.LahmacunDao;
import net.keremgokhan.lahmacuntracker.db.entity.Lahmacun;

public class NewRecordDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.diagram_add, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase db = AppDatabase.getInstance(getActivity().getApplicationContext());
                                LahmacunDao lahmacunDao = db.lahmacunDao();
                                Lahmacun l = new Lahmacun();
                                l.name = "Lahmacun";
                                lahmacunDao.insert(l);
                            }
                        }).start();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewRecordDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
