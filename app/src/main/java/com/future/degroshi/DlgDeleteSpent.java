package com.future.degroshi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;


public class DlgDeleteSpent extends DialogFragment {
    ArrayList mSelectedSpents;
    ArrayList<String> mListOfSpents;
    String[] mCharOfSpents;
    FileWorker fileworker;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mListOfSpents = MainActivity.mSpentsNames;
        mSelectedSpents = new ArrayList();
        fileworker = new FileWorker(getActivity());
        mCharOfSpents = new String[mListOfSpents.size()];
        int i = 0;
        for(String tmp : mListOfSpents){
            mCharOfSpents[i] = tmp;
            i ++;
        }

          // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.btn_delete_spent)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(mCharOfSpents, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedSpents.add(mListOfSpents.get(which));
                                } else if (mSelectedSpents.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedSpents.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.btn_dlg_tak, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity) getActivity()).confirmDeleteSpentDlg(mSelectedSpents);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.btn_dlg_ni, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }
}


