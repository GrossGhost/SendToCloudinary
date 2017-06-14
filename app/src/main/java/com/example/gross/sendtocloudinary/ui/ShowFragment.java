package com.example.gross.sendtocloudinary.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gross.sendtocloudinary.DB;
import com.example.gross.sendtocloudinary.PhotoAdapter;
import com.example.gross.sendtocloudinary.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.gross.sendtocloudinary.ui.LoadFragment.BROADCAST_ACTION;
import static com.example.gross.sendtocloudinary.ui.LoadFragment.IS_DOWNLOADED;

public class ShowFragment extends Fragment {

    private RecyclerView rView;
    private BroadcastReceiver br;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_tab, container, false);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(IS_DOWNLOADED, false)) {
                    updateRecyclerView();
                }
            }

        };
        IntentFilter iFilterIsDownloaded = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(br, iFilterIsDownloaded);

        List<String> rowListItem = getAllItemList();
        GridLayoutManager lLayout;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lLayout = new GridLayoutManager(getContext(), 2);
        } else {
            lLayout = new GridLayoutManager(getContext(), 3);
        }

        rView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);

        rView.setLayoutManager(lLayout);

        PhotoAdapter photoAdapter = new PhotoAdapter(getContext(), rowListItem);
        rView.setAdapter(photoAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(br);
    }

    public List<String> getAllItemList() {

        List<String> allItems = new ArrayList<>();

        DB dbHelper = new DB(getContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DB.TABLE_CLOUDINARY_IMG, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int imgUrl = cursor.getColumnIndex(DB.KEY_IMG_URL);
            do {

                allItems.add(cursor.getString(imgUrl));
                Log.d("public_idDB", cursor.getString(imgUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (String item : allItems) {
            Log.d("public_id", item);
        }
        return allItems;
    }

    public void updateRecyclerView() {
        rView.setAdapter(new PhotoAdapter(getContext(), getAllItemList()));
    }
}
