package co.adrianblan.noraoke;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adrian on 2015-03-27.
 */
public class SongAdapter extends BaseAdapter {

    ArrayList<SongItem> songs = new ArrayList<SongItem>();

    private Activity myContext;

    public SongAdapter(Context context) {
        //super(context, textViewResourceId);
        myContext = (Activity) context;

        songs.add(new SongItem("Baby", "Justin Bieber", "bieber"));
        songs.add(new SongItem("I will always love you", "Celine Dion", "dion"));
        songs.add(new SongItem("Bohemian Rhapsody", "Queen", "queen"));
        songs.add(new SongItem("Gangnam Style", "Psy", "psy"));
        songs.add(new SongItem("Dancing Queen", "ABBA", "abba"));
        songs.add(new SongItem("Baby Got Back", "Sir Mix-A-Lot", "sirmixalot"));
        songs.add(new SongItem("Don't Stop Believing", "Journey", "journey"));
        songs.add(new SongItem("Wannabe", "Spice Girls", "spicegirls"));
    }

    public int getCount() {
        return songs.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = myContext.getLayoutInflater();
        View song = inflater.inflate(R.layout.song, null);

        if(!songs.get(position).getImage().isEmpty()){
            ImageView songImage = (ImageView) song.findViewById(R.id.song_image);
            int resId = myContext.getResources().getIdentifier(songs.get(position).getImage(), "drawable", myContext.getPackageName());
            songImage.setImageResource(resId);
        }

        TextView songTitle = (TextView) song.findViewById(R.id.song_title);
        songTitle.setText(songs.get(position).getTitle());

        TextView songArtist = (TextView) song.findViewById(R.id.song_artist);
        songArtist.setText(songs.get(position).getArtist());

        return song;
    }
}
