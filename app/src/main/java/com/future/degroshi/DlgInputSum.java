package com.future.degroshi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class DlgInputSum extends DialogFragment implements View.OnClickListener {
    Integer inpValue;
    EditText inpValueSum;
    Integer mInputSum;
    final String LOG_TAG = "---Logtag---";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dlg_input_sum_title);
        View v = inflater.inflate(R.layout.dialog_input_sum, null);
        v.findViewById(R.id.btn_dlg_sum_tak).setOnClickListener(this);
        v.findViewById(R.id.btn_dlg_color_pick).setOnClickListener(this);
        inpValueSum = (EditText)v.findViewById(R.id.dlg_input_sum);
        return v;
    }

    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_dlg_sum_tak:
                    try{
                        inpValue = Integer.parseInt(inpValueSum.getText().toString());
                    }catch (Throwable t){
                        Toast.makeText(MainActivity.context.getApplicationContext(),
                                "Шось не то ", Toast.LENGTH_LONG).show();
                        return;
                    }

                    mInputSum = Integer.parseInt(inpValueSum.getText().toString());
                    Log.d(LOG_TAG, "Dialog 1: " + mInputSum);
                    ((MainActivity) getActivity()).confirmSumDlg(mInputSum);
                    dismiss();
                    break;
                case R.id.btn_dlg_color_pick:
                    ((MainActivity) getActivity()).onSpentColorChaged();
                    dismiss();
                    break;
            }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        inpValueSum.getText().clear();
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
