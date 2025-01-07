package com.example.myproject2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private LinearLayout layoutOption, shareOption, languageOption, profileOption;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        profileOption = view.findViewById(R.id.profile_option); // Profile option
        layoutOption = view.findViewById(R.id.layout_option);
        shareOption = view.findViewById(R.id.share_option);
        languageOption = view.findViewById(R.id.language_option);

        // Profile functionality
        profileOption.setOnClickListener(v -> {
            // Open the ProfileActivity
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Logout functionality
        layoutOption.setOnClickListener(v -> {
            // Redirect to Login Activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Show confirmation message
            ToastUtil.showCustomToast(getActivity(), "Logout successfully");
        });

        // Share functionality
        shareOption.setOnClickListener(v -> {
            // Create share intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this app on the Play Store: https://play.google.com/store/apps/details?id=com.example.myapp";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share App via"));
        });

        // Language selection functionality
        languageOption.setOnClickListener(v -> showLanguageDialog());

        return view;
    }

    private void showLanguageDialog() {
        // Create an AlertDialog to show language options
        String[] languages = {"English", "தமிழ்"}; // Tamil and English
        new AlertDialog.Builder(getActivity())
                .setTitle("Choose Language")
                .setItems(languages, (dialog, which) -> {
                    if (which == 0) {
                        // Switch to English
                        setLocale("en");
                    } else {
                        // Switch to Tamil
                        setLocale("ta");
                    }
                })
                .show();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Update app's resources
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

        // Restart the activity to apply the new language
        getActivity().recreate();
    }
}
