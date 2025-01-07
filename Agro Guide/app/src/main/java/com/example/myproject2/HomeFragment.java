package com.example.myproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up the ImageButton to open RegistrationActivity
        ImageButton button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);  // Start the activity
            }
        });

        // Find CardViews and set click listeners to open respective fragments
        CardView vegetablesCard = rootView.findViewById(R.id.Vegtables);
        CardView fruitsCard = rootView.findViewById(R.id.Fruits);
        CardView flowersCard = rootView.findViewById(R.id.Flowers);
        CardView oilTypesCard = rootView.findViewById(R.id.Oil_Types);

        vegetablesCard.setOnClickListener(v -> openFragment(new VegtableFragment()));
        fruitsCard.setOnClickListener(v -> openFragment(new FruitFragment()));
        flowersCard.setOnClickListener(v -> openFragment(new FlowerFragment()));
        oilTypesCard.setOnClickListener(v -> openFragment(new OilFragment()));

        return rootView;
    }

    // Function to replace the current fragment
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);  // Allows the user to navigate back
        transaction.commit();
    }
}
