package co.adrianblan.noraoke;

/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;

public class SongNowPlayingFragment extends Fragment {

    private int position;
    private View rootView;
    FloatingActionButton fab;
    static MediaPlayer mPlayer = null;

    public static SongNowPlayingFragment newInstance() {
        SongNowPlayingFragment f = new SongNowPlayingFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_song_playing,container,false);

        ListView listView = (ListView) rootView.findViewById(R.id.now_playing_song_list);
        listView.setAdapter(new SongAdapter(getActivity(), 3));

        //LinearLayout listHeaderView = (LinearLayout)inflater.inflate(R.layout.list_header, null);
        //listView.addHeaderView(listHeaderView);

        fab = (FloatingActionButton) rootView.findViewById(R.id.now_playing_fab);
        fab.attachToListView(listView);

        //Fulhaxx in order to preserve media player when fragment is destroyed
        if(mPlayer == null) {
            mPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.iwantitthatway);
        }

        //Get music control play button
        final Button play = (Button) rootView.findViewById(R.id.music_control_play);

        //Set the play button to its initial state
        if(mPlayer.isPlaying()){
            play.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause_black_24dp, 0);
        } else {
            play.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_play_arrow_black_24dp, 0);
        }

        //Play and pause the song, also change the icons
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPlayer.isPlaying()){

                    mPlayer.start();
                    play.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause_black_24dp, 0);
                } else {
                    mPlayer.pause();
                    play.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_play_arrow_black_24dp, 0);
                }
            }
        });

        //Get music control playlist button
        Button playlist = (Button) rootView.findViewById(R.id.music_control_playlist);

        //We switch the playlist visibility when we press the playlist button
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView = (ListView) rootView.findViewById(R.id.now_playing_song_list);

                if(listView.getVisibility() != View.VISIBLE) {
                    listView.setVisibility(View.VISIBLE);
                    fab.hide();
                } else {
                    listView.setVisibility(View.GONE);
                    fab.show();
                }
            }
        });

        ViewCompat.setElevation(rootView.findViewById(R.id.music_control), 50);
        return rootView;
    }
}
