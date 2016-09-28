package com.alexapps.movienight.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.alexapps.movienight.R;

public class AlertNoInternetFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle(R.string.network_error).
                setMessage(R.string.network_error_message).
                setPositiveButton(R.string.ok_button, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
