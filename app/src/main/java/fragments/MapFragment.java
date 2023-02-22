package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.go4lunchapp.R;


public class MapFragment extends Fragment {

    Toolbar toolbar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
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