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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

public class SongNowPlayingFragment extends Fragment {

    private int position;

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
        View rootView = inflater.inflate(R.layout.fragment_song_playing,container,false);

        ListView listView = (ListView) rootView.findViewById(R.id.now_playing_song_list);
        listView.setAdapter(new SongAdapter(getActivity()));

        LinearLayout listHeaderView = (LinearLayout)inflater.inflate(R.layout.list_header, null);
        listView.addHeaderView(listHeaderView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.now_playing_fab);
        fab.attachToListView(listView);

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }
}
