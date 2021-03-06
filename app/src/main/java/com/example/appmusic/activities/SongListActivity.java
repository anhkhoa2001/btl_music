package com.example.appmusic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.api.MusicApi;
import com.example.appmusic.adapters.SongListAdapter;
import com.example.appmusic.models.AMusic;
import com.example.appmusic.models.Music;
import com.example.appmusic.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends Base {
    private int id;
    private String type_id;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingtoolbarLayout;
    private RecyclerView recyclerviewSongList;
    private List<Music> songList = new ArrayList<>();
    private String name;
    private SongListAdapter songListAdapter;
    private ImageView collapView;
    ActionBar actionBar;

    @Override
    public void onBackPressed() {
        Log.v("Music", "you clicked button back");
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        collapsingtoolbarLayout = findViewById(R.id.collapsing_toolbar);
        recyclerviewSongList = findViewById(R.id.recyclerView_SongList);
        collapView = findViewById(R.id.collap_view);
        actionBar = getSupportActionBar();
        //set background
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Source")) {
                String source = intent.getStringExtra("Source");
                new LoadImageURL(collapView).execute(source);
            }

            if(intent.hasExtra("Name")) {
                String name = intent.getStringExtra("Name");
                actionBar.setTitle(name);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        getDataIntent(intent);
    }

    //sq
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    //end

    public void getDataIntent(Intent intent) {

        if (intent != null) {
            if (intent.hasExtra("Type_ID")) {
                type_id = intent.getStringExtra("Type_ID");
                id = intent.getIntExtra(intent.getStringExtra("Type_ID"), 0);
            }

            if (intent.hasExtra("ID")) {
                id = intent.getIntExtra("ID", 0);
                type_id = "ID";
                // Truy V???n DB v???i id
                new GetAllTask().execute("/music/by-genre-id", String.valueOf(id));
            } else {
                type_id = "Album_ID";
                id = intent.getIntExtra("Album_ID", 0);
                // Truy V???n DB v???i id
                new GetAllTask().execute("/music/by-singer-id", String.valueOf(id));
            }
        }
    }

    private class GetAllTask extends AsyncTask<String, Void, List<Music>> {
        protected ProgressDialog dialog;
        public GetAllTask() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Music> doInBackground(String... params) {
            try {
                return (List<Music>) MusicApi.getAllMusicById((String) params[0],(String) params[1]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Music> result) {
            super.onPostExecute(result);

            songList = result;
            List<AMusic> aMusics = new ArrayList<>();
            aMusics.addAll(songList);

            songListAdapter = new SongListAdapter(SongListActivity.this, aMusics, id, type_id);
            recyclerviewSongList.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
            recyclerviewSongList.setAdapter(songListAdapter);
        }
    }

    private class LoadImageURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public LoadImageURL(ImageView rs) {
            this.imageView = rs;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(strings[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}