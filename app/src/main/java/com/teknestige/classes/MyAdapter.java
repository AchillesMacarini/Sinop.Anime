package com.teknestige.classes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.teknestige.sinop.AnimeActivity;
import com.teknestige.sinop.NoticiaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DbControler.BDHelper;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<CreateList> galleryList;
    private Context context;
    BDHelper bdHelper = new BDHelper();
    String imgNewUrl = bdHelper.returnUrl()+"ws_images_news/";
    MyApplication myapp = new MyApplication();
    private ArrayList<String> listaNews = new ArrayList<String>();
    String imgAnimeUrl = bdHelper.returnUrl()+"ws_images_animes/";

    public MyAdapter(Context context, ArrayList<CreateList> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(com.teknestige.sinop.R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap imagem = null;
        try {
//            System.out.println(imgNewUrl+returnIdImg(galleryList.get(i).getImage_title().toString(), i)+".png");
            imagem = LoadImageFromWebOperations(imgNewUrl+returnIdImg(galleryList.get(i).getImage_title().toString(), i)+".png");
//                imagem= LoadImageFromWebOperations(imgNewUrl+"01.png");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(galleryList.get(i).getImage_title());
        viewHolder.img.setImageBitmap(imagem);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.img.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
                try {
                    buildNews();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, NoticiaActivity.class);
                String nomeNews = galleryList.get(i).getImage_title();
                    System.out.println("mamãe "+ nomeNews);
                    Bundle b = new Bundle();
                    b.putString("nomeNews" , nomeNews.toString());
                    intent.putExtras(b);
                    context.startActivity(intent);
            }
        });
        viewHolder.img.setOnTouchListener(new View.OnTouchListener()
        {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                Log.d("Pressed", "Button pressed");
                viewHolder.img.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
            }else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
                Log.d("Released", "Button released");
                viewHolder.img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                // TODO Auto-generated method stub

            }
            return false;
        }
    });

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(com.teknestige.sinop.R.id.title);
            img = (ImageView) view.findViewById(com.teknestige.sinop.R.id.img);
        }
    }
    public Bitmap LoadImageFromWebOperations(String url) {
        try {
            ImageView i = null;
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void buildNews() throws IOException, JSONException{
        JSONArray jsonNews = bdHelper.selectAllFromNoticias(context);

        for (int i = 0; i < 5; i++) {
            JSONObject animeObject = jsonNews.getJSONObject(i);
            String id = animeObject.getString("noticia_imagem");
            listaNews.add(id);
        }
    }


    public String returnIdImg(String tittle, int x) throws IOException, JSONException {
        buildNews();
        for (int i = 0; i < listaNews.size(); i++){
            if (listaNews.get(x).equals(tittle)){
                return listaNews.get(x);
            }
        }
        return listaNews.get(x);
    }

    private Context getContext() {
        return MyApplication.getContext();
    }

}