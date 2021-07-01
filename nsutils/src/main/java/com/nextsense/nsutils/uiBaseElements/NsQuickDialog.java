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

import com.nextsense.nsutils.baseElements.NsDialogResult;
import com.nextsense.nsutils.listeners.IUniversalListener;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NsQuickDialog implements View.OnClickListener {
    private AlertDialog alertDialog;
    private final ArrayList<EditText> payloadInputs = new ArrayList<>();
    private IUniversalListener<NsDialogResult> listener;
    private ArrayList<Pair<String, String>> presets;

    /**
     * Shows a quick dialog without a callback listener
     * @param activity context for loading the dialog
     * @param dialogueLayout the special dialog layout
     */
    public static void show(Activity activity, @LayoutRes int dialogueLayout) {
        new NsQuickDialog().showDialog(activity, dialogueLayout, null, null);
    }

    /**
     * Shows a quick dialog with a callback listener
     * @param activity context for loading the dialog
     * @param dialogueLayout the special dialog layout
     * @param listener callback listener for actions
     */
    public static void show(Activity activity, @LayoutRes int dialogueLayout, @Nullable IUniversalListener<NsDialogResult> listener) {
        new NsQuickDialog().showDialog(activity, dialogueLayout, null, listener);
    }

    /**
     * Shows a quick dialog with a callback listener and an arrayList of Pair<Tag,Text> presets for tagged EditTexts
     * @param activity context for loading the dialog
     * @param dialogueLayout the special dialog layout
     * @param listener callback listener for actions
     * @param presets an arrayList of all presets for EditTexts
     */
    public static void show(Activity activity, @LayoutRes int dialogueLayout, @Nullable ArrayList<Pair<String, String>> presets, @Nullable IUniversalListener<NsDialogResult> listener) {
        new NsQuickDialog().showDialog(activity, dialogueLayout, presets, listener);
    }

    private void showDialog(Activity activity, @LayoutRes int dialogueLayout, @Nullable ArrayList<Pair<String, String>> presets, @Nullable IUniversalListener<NsDialogResult> listener) {
        this.listener = listener;
        this.presets = presets != null ? presets : new ArrayList<>();
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
                String tag = (String) child.getTag();
                if(child instanceof EditText) {
                    ((EditText) child).setText(getPresetValue(tag));
                    payloadInputs.add((EditText) child);
                } else if(tag.toLowerCase().equals("close")) {
                    child.setOnClickListener(v -> onClose());
                } else {
                    child.setOnClickListener(this);
                }
            }
        }
    }

    private String getPresetValue(String tag) {
        for (Pair<String, String> pair : presets) {
            if(pair.first.equals(tag)) {
                return pair.second;
            }
        }

        return "";
    }

    private NsDialogResult preparePayloads(View view) {
        NsDialogResult payloads = new NsDialogResult();
        payloads.put("result", (String) view.getTag());
        for (EditText editText : payloadInputs) {
            payloads.put((String) editText.getTag(), editText.getText().toString());
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
