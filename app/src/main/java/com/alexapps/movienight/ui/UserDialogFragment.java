package com.alexapps.movienight.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.alexapps.movienight.R;

public class UserDialogFragment extends DialogFragment {
    private String mTitle;
    private String mMessage;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        Bundle bundle = getArguments();
        mTitle = bundle.getString(MainActivity.TITLE);
        mMessage = bundle.getString(MainActivity.MESSAGE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle(mTitle).
                setMessage(mMessage).
                setPositiveButton(R.string.ok_button, null);
        return builder.create();
        }
   }
