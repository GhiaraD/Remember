package com.ghiarad.dragos.myapplication.Alarme.ViewController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.ghiarad.dragos.myapplication.R;

public class AlertAlarm extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Theme_Sphinx_Dialog_Alert));
        builder.setTitle("PillApp");
        setCancelable(false);
        final String pill_name = getActivity().getIntent().getStringExtra("pill_name");
        builder.setMessage("Did you take your "+ pill_name + " ?");

        builder.setPositiveButton("I took it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertActivity act = (AlertActivity)getActivity();
                act.doPositiveClick(pill_name);
                getActivity().finish();
            }
        });
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}