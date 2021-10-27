package com.nextsense.nsutils.uiBaseElements;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

@SuppressWarnings("unused")
public class NsAlertDialog {

    /**
     * Display a customized AlertDialog with custom components and action
     * @param context of a ui component necessary to inflate a custom View
     * @param layoutRes layout of the dialog box
     * @param components elements used to change the content and set listeners of the Views within layoutRes
     */
    public static void show(Context context, @LayoutRes int layoutRes, Component... components) {
        show(context, false, layoutRes, components);
    }

    /**
     * Display a customized AlertDialog with custom components and action
     * @param context of a ui component necessary to inflate a custom View
     * @param requiresAction determines if the dialog can be dismissed without selecting an option
     * @param layoutRes layout of the dialog box
     * @param components elements used to change the content and set listeners of the Views within layoutRes
     */
    public static void show(Context context, boolean requiresAction, @LayoutRes int layoutRes, Component... components) {
        View dialogView = LayoutInflater.from(context).inflate(layoutRes, null, false);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dialogView).create();
        alertDialog.setCancelable(!requiresAction);

        for (Component component : components) {
            if (component instanceof Text) {
                ((TextView) dialogView.findViewById(component.idResource)).setText(((Text) component).text);
            } else if (component instanceof Button) {
                Button button = (Button) component;
                dialogView.findViewById(button.idResource).setOnClickListener(view -> {
                    if (button.dismiss) {
                        alertDialog.dismiss();
                    }

                    if (button.listener != null) {
                        button.listener.onClick(view);
                    }
                });
            }
        }

        alertDialog.show();
    }

    public abstract static class Component {
        public @IdRes
        int idResource;
    }

    /**
     * Object used to set the text of any view by it's id
     */
    public static class Text extends Component {
        public String text;

        private Text(@IdRes int idResource, String text) {
            this.idResource = idResource;
            this.text = text;
        }

        public static Text set(@IdRes int idResource, String text) {
            return new Text(idResource, text);
        }
    }

    /**
     * Object used to set a click listener of any view by it's id
     */
    public static class Button extends Component {
        public View.OnClickListener listener;
        public boolean dismiss;

        private Button(@IdRes int idResource, boolean dismiss, View.OnClickListener listener) {
            this.idResource = idResource;
            this.listener = listener;
            this.dismiss = dismiss;
        }

        public static Button set(@IdRes int idResource, boolean dismiss, View.OnClickListener listener) {
            return new Button(idResource, dismiss, listener);
        }
    }
}
