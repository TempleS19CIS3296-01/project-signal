package org.thoughtcrime.securesms.conversation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.logging.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ChangeBackgroundDialog extends AppCompatDialogFragment {

    private static final String URL_STRING_KEY = "myUrlStringKey";
    private static final String LOG_TAG = ">>ChangeBackgroundDialog";
    private static final String URL_ENCODING = "UTF-8";
    private TextInputLayout inputLayout;
    private String urlString;

    ChangeBackgroundListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        if (savedInstanceState != null) {
            urlString = savedInstanceState.getString(URL_STRING_KEY);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.change_background_dialog, null);

        inputLayout = rootView.findViewById(R.id.change_background_input_layout);

        setInputLayoutListeners();

        builder.setView(rootView)
                .setTitle(getString(R.string.change_background_dialog_title))
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(LOG_TAG, "Cancel called. which = " + which);
                    }
                })
                .setPositiveButton(R.string.button_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }); // listener code below



        // return dialog
        return builder.create();

    }

    @Override
    public void onAttach(Context parentContext) {
        super.onAttach(parentContext);

        try {
            listener = (ChangeBackgroundListener) parentContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(parentContext.toString()
            + "must implement ChangeBackgroundListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urlString = inputLayout.getEditText().getText().toString().trim();

                    try {
                        Log.d(LOG_TAG, "onClick() called with urlString = " + urlString);
                        URL imageUrl = new URL(urlString);

                        listener.setBackgroundImage(imageUrl, true);

                        dismiss();
                    } catch (MalformedURLException e) {
                        inputLayout.setError(getString(R.string.error_malformed_url));
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(URL_STRING_KEY, urlString);
    }

    private void setInputLayoutListeners() {
        inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                inputLayout.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public interface ChangeBackgroundListener {
        void setBackgroundImage(URL imageUrl, boolean newImage);
    }
}
