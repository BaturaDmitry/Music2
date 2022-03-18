package by.bsuir.mobilki2laba;

import static by.bsuir.mobilki2laba.MainActivity.lru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    ImageView songImage;
    TextView songName;
    TextView albumName;
    TextView songPrice;
    TextView currency;


    public SongAdapter(@NonNull Context context, int resource, @NonNull List<Song> songsList) {
        super(context, resource, songsList);
    }

    static class SongCoverHolder{
        private Song song;
        private ImageView imageView;

        public SongCoverHolder(ImageView itemView, Song song){
            this.song = song;
            imageView = itemView;
        }

        public void bindSongCoverImage(Drawable drawable){
            imageView.setImageDrawable(drawable);
            synchronized (MainActivity.lru){
                lru.put(song.getCoverUrl(),drawable);
            }
        }
    }

    private double getButifiedCoeffPrice(double price){
        return Math.round(price * 100 * MainActivity.GLOBAL_CURRENCY_COEFF) / 100.0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item, parent, false);
        }


        songImage = (ImageView) convertView.findViewById(R.id.itemCoverImage);
        songName = (TextView) convertView.findViewById(R.id.itemSongName);
        albumName = (TextView) convertView.findViewById(R.id.itemAlbumName);
        songPrice = (TextView) convertView.findViewById(R.id.itemPrice);
        currency = (TextView) convertView.findViewById(R.id.itemCurrency);

        SongCoverHolder songCoverHolder = new SongCoverHolder(songImage, song);
        songImage.setImageDrawable(MainActivity.mainActivityContext.getDrawable(R.drawable.ic_default_image));

        if (MainActivity.isNetworkAvailable()) {
            String coverUrl = song.getCoverUrl();

            synchronized (MainActivity.lru) {
                if (MainActivity.lru.getBitmapFromMemory(coverUrl) == null)
                    MainActivity.songImageDownloader.queueSongImage(songCoverHolder, song.getCoverUrl());
            }
        }

        songName.setText(song.getName());
        albumName.setText(song.getAlbum());


        songPrice.setText(String.valueOf(getButifiedCoeffPrice(song.getPrice())));
        if (MainActivity.IS_USD){
            currency.setText("$");
        } else {
            currency.setText("BYN");
        }


        handleBehaviourSpecifics(((View)parent).getId());

        return convertView;
    }

    private void handleBehaviourSpecifics(int id){
 /*       switch (id){
            case R.id.basketListView:{
                isFavourite.setVisibility(View.GONE);
                addToCart.setVisibility(View.GONE);
                break;
            }
        }*/
    }
}
