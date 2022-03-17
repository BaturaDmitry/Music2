package by.bsuir.mobilki2laba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import by.bsuir.mobilki2laba.R;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView songsListView;

    public static Button nirvanaButton;
    public static Button grobButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (MainActivity.isNetworkAvailable()) MainActivity.songImageDownloader.clearQueue();
    }

    public static void quickFixFilterButtonsText(){
        nirvanaButton.setText("Nirvana (" + MainActivity.NIRVANA_SONGS_COUNT + ")");
        grobButton.setText("GrOb (" + MainActivity.GROB_SONGS_COUNT + ")");
    }

    public static void handleFilterButtons() {
        if (MainActivity.nirvanaIsSelectedFilter) {
            nirvanaButton.setBackgroundColor(Color.GREEN);
            grobButton.setBackgroundColor(Color.WHITE);
        } else {
            nirvanaButton.setBackgroundColor(Color.WHITE);
            grobButton.setBackgroundColor(Color.GREEN);
        }
        nirvanaButton.setTextColor(Color.BLACK);
        grobButton.setTextColor(Color.BLACK);

        nirvanaButton.setText("Nirvana (" + MainActivity.NIRVANA_SONGS_COUNT + ")");
        grobButton.setText("GrOb (" + MainActivity.GROB_SONGS_COUNT + ")");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        songsListView = view.findViewById(R.id.songsListView);
        filterLists();
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song selectedSong = (Song) songsListView.getItemAtPosition(position);
                Intent showDetail = new Intent(MainActivity.mainActivityContext, DetailActivity.class);
                showDetail.putExtra("id", selectedSong.getId() - 1);
                startActivity(showDetail);
            }
        });

        if (!MainActivity.isNetworkAvailable()) {
            TextView internetConnectionLost = (TextView) view.findViewById(R.id.warningTextView);
            internetConnectionLost.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void filterLists(){
        SongAdapter adapter = new SongAdapter(MainActivity.mainActivityContext, 0, MainActivity.songsList );
        songsListView.setAdapter(adapter);
    }
}