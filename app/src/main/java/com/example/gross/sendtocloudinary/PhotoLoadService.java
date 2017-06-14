package com.example.gross.sendtocloudinary;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;

import static com.example.gross.sendtocloudinary.LoadFragment.BROADCAST_ACTION;
import static com.example.gross.sendtocloudinary.LoadFragment.IS_DOWNLOADED;


public class PhotoLoadService extends Service {

    private static final String CLOUD_NAME = "nodacloud";
    private static final String API_KEY = "336952692573118";
    private static final String API_SECRET = "-o-D-0by0CdGVGWEwY7gh8l--WU";

    private Intent broadcastIntent = new Intent(BROADCAST_ACTION);
    boolean isDownloaded = true;
    private static SparseArray<File> toUpload = LoadFragment.getToUpload();
    private Cloudinary cloudinary =  new Cloudinary(ObjectUtils.asMap(
            "cloud_name", CLOUD_NAME, "api_key", API_KEY, "api_secret", API_SECRET));


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new UploadImage().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    private class UploadImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0, arraySize = toUpload.size(); i < arraySize; i++) {

                try {
                    cloudinary.uploader().upload(toUpload.valueAt(i), ObjectUtils.emptyMap());

                } catch (IOException e) {
                    isDownloaded = false;
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            broadcastIntent.putExtra(IS_DOWNLOADED, isDownloaded);
            sendBroadcast(broadcastIntent);
            if (isDownloaded){
                Toast.makeText(getApplicationContext(), "Image uploaded.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Cant uploaded Image(s).", Toast.LENGTH_SHORT).show();
            }

            stopSelf();
            super.onPostExecute(s);
        }
    }
}
