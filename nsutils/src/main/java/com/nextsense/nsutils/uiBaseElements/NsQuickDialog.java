package com.nextsense.nsutils.uiBaseElements;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nextsense.nsutils.listeners.IUniversalListener;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NsQuickDialog implements View.OnClickListener {
    private AlertDialog alertDialog;
    private final ArrayList<EditText> payloadInputs = new ArrayList<>();
    private IUniversalListener<ArrayList<Pair<String, String>>> listener;

    /**
     * Shows a quick dialog without a callback listener
     * @param activity context for loading the dialog
     * @param dialogueLayout the special dialog layout
     */
    public static void show(Activity activity, @LayoutRes int dialogueLayout) {
        new NsQuickDialog().showDialog(activity, dialogueLayout, null);
    }

    /**
     * Shows a quick dialog with a callback listener
     * @param activity context for loading the dialog
     * @param dialogueLayout the special dialog layout
     * @param listener callback listener for actions
     */
    public static void show(Activity activity, @LayoutRes int dialogueLayout, @Nullable IUniversalListener<ArrayList<Pair<String, String>>> listener) {
        new NsQuickDialog().showDialog(activity, dialogueLayout, listener);
    }

    private void showDialog(Activity activity, @LayoutRes int dialogueLayout, @Nullable IUniversalListener<ArrayList<Pair<String, String>>> listener) {
        this.listener = listener;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View rootView = inflater.inflate(dialogueLayout, null);
        setup(rootView);
        builder.setCancelable(false);
        builder.setView(rootView);
        builder.create();
        alertDialog = builder.show();
    }

    private void setup(View view) {
        ViewGroup group = ((ViewGroup) view);
        for(int index = 0; index < group.getChildCount(); index++) {
            View child = group.getChildAt(index);
            if(child instanceof ViewGroup) {
                setup(child);
            }

            if(child.getTag() != null && child.getTag() instanceof String) {
                if(child instanceof EditText) {
                    payloadInputs.add((EditText) child);
                } else if(((String) child.getTag()).toLowerCase().equals("close")) {
                    child.setOnClickListener(v -> onClose());
                } else {
                    child.setOnClickListener(this);
                }
            }
        }
    }

    private ArrayList<Pair<String, String>> preparePayloads(View view) {
        ArrayList<Pair<String, String>> payloads = new ArrayList<>();
        payloads.add(new Pair<>("result", (String) view.getTag()));
        for (EditText editText : payloadInputs) {
            payloads.add(new Pair<>((String) editText.getTag(), editText.getText().toString()));
        }

        return payloads;
    }

    private void onClose() {
        alertDialog.dismiss();
        if(listener != null) {
            listener.onFail(new Exception("Dialog was closed."));
        }
    }

    @Override
    public void onClick(View view) {
        alertDialog.dismiss();
        if(listener != null) {
            listener.onSuccess(preparePayloads(view));
        }
    }
}
