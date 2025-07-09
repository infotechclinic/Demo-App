package com.example.loginandsignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.android.material.snackbar.Snackbar;

import java.util.*;

public class AddFamilyFragment extends Fragment {

    EditText etName, etAge, etRelation;
    Button btnAdd;
    RecyclerView recyclerView;
    FamilyAdapter adapter;
    List<FamilyMemberModel> memberList = new ArrayList<>();
    FamilyDBHelper dbHelper;
    LinearLayout rootLayout;

    public AddFamilyFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_family, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etRelation = view.findViewById(R.id.etRelation);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerFamily);
        rootLayout = view.findViewById(R.id.rootLayout);

        dbHelper = new FamilyDBHelper(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FamilyAdapter(getContext(), memberList, dbHelper, this::refreshList, member -> {
            Intent intent = new Intent(getContext(), EditFamilyMemberActivity.class);
            intent.putExtra("id", member.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String relation = etRelation.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(relation)) {
                Snackbar.make(rootLayout, "All fields are required", Snackbar.LENGTH_SHORT).show();
                return;
            }

            dbHelper.insertMember(new FamilyMemberModel(0, name, age, relation));
            etName.setText("");
            etAge.setText("");
            etRelation.setText("");

            Snackbar.make(rootLayout, "Member added successfully!", Snackbar.LENGTH_SHORT).show();
            refreshList();
        });

        refreshList();
    }

    private void refreshList() {
        memberList.clear();
        memberList.addAll(dbHelper.getAllMembers());
        adapter.notifyDataSetChanged();
    }
}
