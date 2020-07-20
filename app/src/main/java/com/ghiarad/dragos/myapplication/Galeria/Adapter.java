package com.ghiarad.dragos.myapplication.Galeria;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiarad.dragos.myapplication.R;

import java.io.File;
import java.util.ArrayList;

//for the list of images
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Cell>galleryList;
    private Context context;

    public Adapter(Context context, ArrayList<Cell> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell, viewGroup, false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galleryList.get(i).getPath(), viewHolder.img);

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, ""+galleryList.get(i).getPath(), Toast.LENGTH_SHORT).show();

                Intent a = new Intent(context, SinglePhoto.class);
                a.putExtra("path",galleryList.get(i).getPath());
                a.putExtra("title",galleryList.get(i).getTitle());
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(a);
            }
        });

        /*viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                File file = new File(galleryList.get(i).getPath());
                boolean deleted = file.delete();

                return false;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        public ViewHolder(View view){
            super(view);

            img = (ImageView) view.findViewById(R.id.img);
        }

    }

    private void setImageFromPath(String path, ImageView image) {

        File imgFIle = new File(path);

        if(imgFIle.exists()){
            Bitmap myBitmap = ImageHelper.decodeSampledBitmapFromPath(imgFIle.getAbsolutePath(),200,200);
            image.setImageBitmap(myBitmap);
        }
    }

}
