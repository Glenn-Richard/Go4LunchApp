package fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchapp.R;

import MVVM.GeneralViewModel;
import adapter.WorkmatesFragmentAdapter;

public class WorkmatesFragment extends Fragment {

    GeneralViewModel viewModel;
    RecyclerView recyclerView;
    WorkmatesFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        recyclerView = view.findViewById(R.id.workmates_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WorkmatesFragmentAdapter(getContext());
        setViewModel();
        recyclerView.setAdapter(adapter);
        return view;
    }



    @SuppressLint("NotifyDataSetChanged")
    private void setViewModel(){
        viewModel = new ViewModelProvider(this).get(GeneralViewModel.class);
        viewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            if (!(users == null)){
                adapter.setAdapter(users);
                adapter.notifyDataSetChanged();
            }
        });
    }
}