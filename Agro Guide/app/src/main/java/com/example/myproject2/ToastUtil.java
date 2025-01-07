package com.example.myproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    // Static method to show a custom Toast
    public static void showCustomToast(Context context, String message) {
        // Inflate custom layout for the toast
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Set message and icon
        TextView toastMessage = layout.findViewById(R.id.toast_message);
        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        toastMessage.setText(message);
        toastIcon.setImageResource(R.drawable.icon); // Replace with your icon

        // Create and show the Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
