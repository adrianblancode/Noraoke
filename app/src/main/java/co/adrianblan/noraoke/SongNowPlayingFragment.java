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

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.StringTokenizer;

public class SongNowPlayingFragment extends Fragment {

    private int position;
    private View rootView;
    FloatingActionButton fab;
    static MediaPlayer mPlayer = null;
    WebSocketClient mWebSocketClient = null;
    Button play = null;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SongPlayingActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //Fulhaxx in order to preserve media player when fragment is destroyed
        if(mPlayer == null) {
            mPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.iwantitthatway);
        }

        //Connect to the websocket if we don't have it
        if(mWebSocketClient == null){
            connectWebSocket();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.seekTo(0);
            }
        };

        //Pressing the previous song button just starts it at the beginning
        final Button prev = (Button) rootView.findViewById(R.id.music_control_prev);
        prev.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_skip_previous_black_24dp);
        prev.setOnClickListener(listener);

        final Button next = (Button) rootView.findViewById(R.id.music_control_next);
        next.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_skip_next_black_24dp);
        next.setOnClickListener(listener);

        //Get music control play button
        play = (Button) rootView.findViewById(R.id.music_control_play);

        //Set the play button to its initial state
        if(mPlayer.isPlaying()){
            play.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_pause_black_24dp);
        } else {
            play.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_play_arrow_black_24dp);
        }

        //Play and pause the song, also change the icons
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPlayer.isPlaying()){

                    //Send the time in milliseconds
                    if(mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
                        mWebSocketClient.send(System.currentTimeMillis() + " - " + mPlayer.getCurrentPosition());
                    }

                    mPlayer.start();
                    play.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_pause_black_24dp);
                } else {

                    if(mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
                        mWebSocketClient.send("STOP");
                    }

                    mPlayer.pause();
                    play.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_play_arrow_black_24dp);
                }
            }
        });

        //Get music control playlist button
        Button playlist = (Button) rootView.findViewById(R.id.music_control_playlist);

        //We switch the playlist visibility when we press the playlist button
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout playlist = (LinearLayout) rootView.findViewById(R.id.now_playing_playlist);

                if(playlist.getVisibility() != View.VISIBLE) {
                    playlist.setVisibility(View.VISIBLE);
                    Animation slideIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in);
                    playlist.startAnimation(slideIn);
                    fab.hide();
                } else {
                    Animation slideOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out);
                    playlist.startAnimation(slideOut);
                    playlist.setVisibility(View.GONE);
                    fab.show();
                }
            }
        });

        rootView.findViewById(R.id.now_playing_playlist).setVisibility(View.GONE);

        ViewCompat.setElevation(rootView.findViewById(R.id.music_control), 50);
        return rootView;
    }

    private void connectWebSocket() {
        URI uri;
        try {
            //URL to a DigitalOcean instance in Singapore, dedicated to Noraoke
            uri = new URI("ws://128.199.134.30:8000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Opening socket");

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("HELLO");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;

                //Thread?
                System.out.println(message);

                //If it's connection messages we quit
                if(message.contains("HELLO") || message.contains("connected")){
                    return;
                }

                //If the others pause, we pause too
                if(message.contains("STOP")){
                    mPlayer.pause();
                    return;
                }

                //Otherwise we need to seek to their position

                //The strings are split into three parts by " - " as token
                String[] result = message.split("\\s-\\s");

                //The first part is IP, the second the time it was played and third where in the song it was played
                if(result.length != 3){
                    return;
                }

                //Get the time in milliseconds
                //long timeshift = (System.currentTimeMillis() - Long.parseLong(result[1]));
                long timeshift = 250;
                int seekTo = Integer.parseInt(result[2]) + (int) timeshift;

                System.out.println("timeshift: " + timeshift + " + " + Integer.parseInt(result[2]));

                //The seek must be fitting within the song
                if (seekTo >= 0 && seekTo < mPlayer.getDuration()) {
                    mPlayer.seekTo(seekTo);

                    //If it's not playing, start
                    if(!mPlayer.isPlaying()){

                        //Only play if activity is in foreground
                        if(MainActivity.isInForeground()) {
                            mPlayer.start();
                        }
                    }
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}
