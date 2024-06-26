package com.example.tvshowsappmvvm.listeners;

import com.example.tvshowsappmvvm.models.TVShow;

public interface WatchListListener {

    void onTVShowClicked(TVShow tvShow);
    
    void removeTVShowFromWatchList(TVShow tvShow, int position);
}
