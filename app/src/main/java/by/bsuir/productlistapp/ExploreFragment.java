package by.bsuir.productlistapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import by.bsuir.productlistapp.R;

public class ExploreFragment extends Fragment {

    enum SEARCH_CATEGORIES { SONG_NAME, ARTIST, PRICE};

    SEARCH_CATEGORIES CURR_CATEGORY = SEARCH_CATEGORIES.SONG_NAME;

    private ListView searchResultListView;
    private SearchView searchView;
    private EditText firstPrice;
    private EditText secondPrice;
    private Button searchBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private boolean categoryFunction(Song song, String s){
        switch (CURR_CATEGORY){
            case SONG_NAME:
                return song.getName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT));
            case ARTIST:
                return song.getArtist().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT));
            case PRICE:
                try {
                    return song.getPrice() * MainActivity.GLOBAL_CURRENCY_COEFF <= Double.valueOf(s) ;
                } catch (NumberFormatException e){
                    return false;
                }
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.mainActivityContext,
                R.array.search_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        CURR_CATEGORY = SEARCH_CATEGORIES.SONG_NAME;
                        searchView.setVisibility(View.VISIBLE);
                        firstPrice.setVisibility(View.INVISIBLE);
                        secondPrice.setVisibility(View.INVISIBLE);
                        searchBtn.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        CURR_CATEGORY = SEARCH_CATEGORIES.ARTIST;
                        searchView.setVisibility(View.VISIBLE);
                        firstPrice.setVisibility(View.INVISIBLE);
                        secondPrice.setVisibility(View.INVISIBLE);
                        searchBtn.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        CURR_CATEGORY = SEARCH_CATEGORIES.PRICE;
                        searchView.setVisibility(View.INVISIBLE);
                        firstPrice.setVisibility(View.VISIBLE);
                        secondPrice.setVisibility(View.VISIBLE);
                        searchBtn.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        firstPrice=view.findViewById(R.id.firstPrice);
        secondPrice=view.findViewById(R.id.secondPrice);
        searchResultListView = view.findViewById(R.id.searchResultListView);
        fillFullList();

        searchView = view.findViewById(R.id.searchView);
        searchBtn= view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchPrice(firstPrice.getText().toString(),secondPrice.getText().toString());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Song> filteredSongs = new ArrayList<Song>();
                for (Song song : MainActivity.songsList){

                    if (categoryFunction(song, s)){
                        filteredSongs.add(song);
                    }

                }
                SongAdapter songAdapter = new SongAdapter(MainActivity.mainActivityContext, 0,filteredSongs);
                searchResultListView.setAdapter(songAdapter);

                return false;
            }
        });


        return view;
    }

    private void searchPrice(String firstPrice, String secondPrice){
        ArrayList<Song> filteredSongs = new ArrayList<Song>();
        if(firstPrice.length()==0||secondPrice.length()==0){
            for (Song song : MainActivity.songsList){

                    filteredSongs.add(song);
            }
        }else{
            for (Song song : MainActivity.songsList){

                if (categoryFunction2(song, firstPrice,secondPrice)){
                    filteredSongs.add(song);
                }
            }
        }
        SongAdapter songAdapter = new SongAdapter(MainActivity.mainActivityContext, 0,filteredSongs);
        searchResultListView.setAdapter(songAdapter);
    }
    private boolean categoryFunction2(Song song, String firstPrice, String secondPrice){
        switch (CURR_CATEGORY){
            case PRICE:
                try {
                    return song.getPrice() * MainActivity.GLOBAL_CURRENCY_COEFF >= Double.parseDouble(firstPrice) && song.getPrice() * MainActivity.GLOBAL_CURRENCY_COEFF <= Double.parseDouble(secondPrice);
                } catch (NumberFormatException e){
                    return false;
                }
        }
        return false;
    }
    private void fillFullList() {
        SongAdapter adapter = new SongAdapter(MainActivity.mainActivityContext, 0, MainActivity.songsList);
        searchResultListView.setAdapter(adapter);
    }
}