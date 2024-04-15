package com.example.tvshowsappmvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshowsappmvvm.R;
import com.example.tvshowsappmvvm.adapters.WatchListAdapter;
import com.example.tvshowsappmvvm.databinding.ActivityWatchListBinding;
import com.example.tvshowsappmvvm.listeners.WatchListListener;
import com.example.tvshowsappmvvm.models.TVShow;
import com.example.tvshowsappmvvm.ultilities.TempDataHolder;
import com.example.tvshowsappmvvm.viewmodels.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel watchListViewModel;
    private List<TVShow> watchList;
    private WatchListAdapter watchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this,R.layout.activity_watch_list);
        doInitialization();

    }

    private void doInitialization(){
        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchList = new ArrayList<>();
    }

    private void loadWatchList() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    if(watchList.size() > 0){
                        watchList.clear();
                    }
                    watchList.addAll(tvShows);
                    watchListAdapter = new WatchListAdapter(watchList,this);
                    activityWatchListBinding.watchListRecyclerView.setAdapter(watchListAdapter);
                    activityWatchListBinding.watchListRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchList();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), WatchListActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchList(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.removeTVShowWatchList(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchListAdapter.notifyItemRemoved(position);
                    watchListAdapter.notifyItemRangeChanged(position,watchListAdapter.getItemCount());
                    compositeDisposable.dispose();
                }));
    }

}