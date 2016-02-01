package com.future.degroshi;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;


public class DlgAddNewSpent extends DialogFragment implements OnClickListener{
    public static EditText tempName;
    final String LOG_TAG = "---Logtag---";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dlg_new_title);
        View v = inflater.inflate(R.layout.dialog_new_spent, null);
        v.findViewById(R.id.btn_dlg_tak).setOnClickListener(this);
        tempName = (EditText)v.findViewById(R.id.dlg_new_input);
        return v;
    }

    public void onClick(View v) {
        String inputValue =(tempName.getText().toString());
        if(isValid(inputValue)){
            Log.d(LOG_TAG, "Dialog 1: " + inputValue);
            ((MainActivity) getActivity()).confirmNewSpentDlg(inputValue);
            dismiss();
        }else{
            return;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        tempName.getText().clear();
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }

    boolean isValid(String newSpentName){
        if(!newSpentName.equals("")) {
            for (Spent spent : MainActivity.mSpents) {
                if (spent.mName.equals(newSpentName)) return false;
            }
            return true;
        }
        return false;
    }
}
