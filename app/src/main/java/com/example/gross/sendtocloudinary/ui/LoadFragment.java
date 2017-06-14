package com.example.gross.sendtocloudinary.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gross.sendtocloudinary.PhotoLoadService;
import com.example.gross.sendtocloudinary.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;


public class LoadFragment extends Fragment implements View.OnClickListener {

    public static final String BROADCAST_ACTION = "brodcast";
    public static final String IS_DOWNLOADED = "isDownloaded";

    private ImageView imgView1,imgView2,imgView3,imgView4,imgView5;
    private View rootView;
    private static SparseArray<File> toUpload = new SparseArray<>();
    private int selectedImgViewID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.load_tab, container, false);

        initializeViews();

        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(IS_DOWNLOADED,false)){
                    clearDownloadedImages();
                }
            }

        };
        IntentFilter iFilterIsDownloaded = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(br,iFilterIsDownloaded);

        return rootView;
    }

    private void initializeViews() {

        imgView1 = (ImageView) rootView.findViewById(R.id.imgLoad1);
        imgView1.setOnClickListener(this);
        imgView2 = (ImageView) rootView.findViewById(R.id.imgLoad2);
        imgView2.setOnClickListener(this);
        imgView3 = (ImageView) rootView.findViewById(R.id.imgLoad3);
        imgView3.setOnClickListener(this);
        imgView4 = (ImageView) rootView.findViewById(R.id.imgLoad4);
        imgView4.setOnClickListener(this);
        imgView5 = (ImageView) rootView.findViewById(R.id.imgLoad5);
        imgView5.setOnClickListener(this);
        Button btnLoad = (Button) rootView.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLoad){
            if(toUpload.size() ==0 ){
                Toast.makeText(getContext(),"Select image first!",Toast.LENGTH_LONG).show();
            }else {
                getActivity().startService(new Intent(getContext(),PhotoLoadService.class));
            }

        }else {
            selectedImgViewID = v.getId();
            EasyImage.openGallery(LoadFragment.this,0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {}

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                toUpload.put(selectedImgViewID,imageFiles.get(0));
                ImageView selectedImgView =(ImageView) rootView.findViewById(selectedImgViewID);
                Picasso.with(getContext()).load(imageFiles.get(0)).resize(300,240).into(selectedImgView);

            }
            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {}
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyImage.clearConfiguration(getContext());
    }

    public void clearDownloadedImages(){

        imgView1.setImageResource(android.R.drawable.ic_menu_add);
        imgView2.setImageResource(android.R.drawable.ic_menu_add);
        imgView3.setImageResource(android.R.drawable.ic_menu_add);
        imgView4.setImageResource(android.R.drawable.ic_menu_add);
        imgView5.setImageResource(android.R.drawable.ic_menu_add);
        toUpload.clear();
    }

    public static SparseArray<File> getToUpload() {
        return toUpload;
    }
}
