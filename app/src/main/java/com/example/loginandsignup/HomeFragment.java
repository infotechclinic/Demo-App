package com.example.loginandsignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FamilyAdapter adapter;
    List<FamilyMemberModel> memberList;
    FamilyDBHelper dbHelper;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerFamily);
        logout = view.findViewById(R.id.logout);
        ImageView imageView = view.findViewById(R.id.imageView);

        dbHelper = new FamilyDBHelper(getContext());
        dbHelper.addDummyData(); // adds if table is empty

        memberList = dbHelper.getAllMembers();
        adapter = new FamilyAdapter(getContext(), memberList, dbHelper, this::refreshList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);



        logout.setOnClickListener(v -> {
            SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        memberList.clear();
        memberList.addAll(dbHelper.getAllMembers());
        adapter.notifyDataSetChanged();
    }
    private void refreshList() {
        memberList.clear();
        memberList.addAll(dbHelper.getAllMembers());
        adapter.notifyDataSetChanged();
    }

}
