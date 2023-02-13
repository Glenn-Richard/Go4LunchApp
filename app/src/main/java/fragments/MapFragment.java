package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.go4lunchapp.R;

import java.util.Objects;


public class MapFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Map");

        return inflater.inflate(R.layout.fragment_map, container, false);
    }


}

// - Mauvaise pratique (dataparser), utiliser Retrofit -> Jack Warston
// - Bibliotheque GSON || moschi pour parser les données JSON
// - regarder discord pour Facebook
// - injecter données dans le view model partagé

//ETAPES

//1- recuperer les données sur retrofit
//2- faire l'architecture MVM
//3 - Appel des données via le view model
// 4 - pas de back end en view

// rating bar