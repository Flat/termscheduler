package com.kennethswenson.termschedule;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.Models.TermViewModel;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView terms = getView().findViewById(R.id.termList);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(getActivity().getApplicationContext());
        TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getTerms().observe(this, termList -> {
            ArrayAdapter<Term> arrayAdapter = new ArrayAdapter<Term>(getActivity(), android.R.layout.simple_list_item_1, termList);
            terms.setAdapter(arrayAdapter);
        });
        terms.setClickable(true);
        terms.setOnItemClickListener((adapterView, view, i, l) -> {
            Term term = (Term) terms.getItemAtPosition(i);
            Intent intent = new Intent(getActivity(), TermDetailActivity.class);
            intent.putExtra("TERMID", term.getId());
            startActivity(intent);
        });
    }
}
