package com.alib.myanimelist.SearchFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.R;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.enums.SortOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.netty.resolver.DefaultAddressResolverGroup;

public class SearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Call searchAnimeinAPI method to get anime data
        searchAnimeinAPI();
    }

    private void searchAnimeinAPI() {
        try {
            Jikan jikan = new Jikan.JikanBuilder()
                    .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                    .build();

            // Get anime search result and add titles to data list
            List<String> data = new ArrayList<>();
            Collection<Anime> searchResult = jikan.query().anime().search()
                    .query("sword art online")
                    .orderBy(AnimeOrderBy.POPULARITY, SortOrder.ASCENDING)
                    .execute()
                    .collectList()
                    .block();

            // Extract the title of each anime and add to the data list
            for (Anime anime : searchResult) {
                data.add(anime.getTitle());
            }

            // Set the data to the adapter
            mAdapter.setData(data);
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mData = new ArrayList<>();

        public void setData(List<String> data) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mTextView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
