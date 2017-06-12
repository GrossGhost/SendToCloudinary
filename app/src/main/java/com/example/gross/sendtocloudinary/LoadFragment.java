package com.example.gross.sendtocloudinary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Gross on 09.06.2017.
 */

public class LoadFragment extends Fragment implements View.OnClickListener {

    private ImageView imgView1,imgView2,imgView3,imgView4,imgView5;
    private static final int REQUEST = 1;
    private View rootView;
    private Uri selectedImage;
    private int selectedImgViewID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.load_tab, container, false);

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
        return rootView;
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        selectedImgViewID = v.getId();
        startActivityForResult(i,REQUEST);
        //Picasso.with(getContext()).load(selectedImage).resize(300,240).into((ImageView) v);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            ImageView v =(ImageView) rootView.findViewById(selectedImgViewID);
            Picasso.with(getContext()).load(selectedImage).resize(300,240).into(v);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
