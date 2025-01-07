package com.example.myproject2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FlowerFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vegtable, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter setup
        List<VegetableItem> vegetableList = new ArrayList<>();
        VegetableAdapter adapter = new VegetableAdapter(getContext(), vegetableList);
        recyclerView.setAdapter(adapter);

        // Fetch data
        fetchDataByCategory("மலர்கள்", vegetableList, adapter);

        return rootView;
    }

    private void fetchDataByCategory(String categoryCollection, List<VegetableItem> vegetableList, VegetableAdapter adapter) {
        FirebaseFirestore db;
        db=FirebaseFirestore.getInstance();
        db.collection(categoryCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        vegetableList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            VegetableItem item = document.toObject(VegetableItem.class);
                            vegetableList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}