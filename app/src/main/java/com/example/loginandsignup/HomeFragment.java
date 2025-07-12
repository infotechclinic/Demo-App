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
import android.widget.TextView;

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
        imageView.setImageResource(R.drawable.ic_profile);

        dbHelper = new FamilyDBHelper(getContext());
        dbHelper.addDummyData(); // Adds dummy data if DB is empty

        memberList = dbHelper.getAllMembers();

        DBHelper dbHelper1 = new DBHelper(getContext());
        TextView helloUsername = view.findViewById(R.id.helloUsername);

        String name = dbHelper1.getUsername();
        helloUsername.setText("Hello, " + name);


        adapter = new FamilyAdapter(getContext(), memberList, dbHelper, this::refreshList, member -> {
            Intent intent = new Intent(getContext(), MemberDetailActivity.class);
            intent.putExtra("member_id", member.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        imageView.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        logout.setOnClickListener(v -> {
            SharedPreferences preferences = requireActivity().getSharedPreferences("userData", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            requireActivity().finish();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        memberList.clear();
        memberList.addAll(dbHelper.getAllMembers());
        adapter.notifyDataSetChanged();
    }
}

