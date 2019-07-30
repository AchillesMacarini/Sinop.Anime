package com.teknestige.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teknestige.sinop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import DbControler.BDHelper;

public class CustomListViewAdapter extends ArrayAdapter<String> {

    private ArrayList<String> listaAnimes = new ArrayList<String>();
    BDHelper bdHelper = new BDHelper();
    Context context;
    String imgAnimeUrl = bdHelper.returnUrl()+"ws_images_animes/";

    public CustomListViewAdapter(Context context, int resourceId, //resourceId=your layout
                                 ArrayList<String> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_style, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.animeTitle);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView9);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem);

        Bitmap imagem = null;
        try {
//            if (!returnIdImg(Item.getTitle()).equals("null")) {
                imagem = LoadImageFromWebOperations(imgAnimeUrl + returnIdImg(rowItem) + ".png");
                System.out.println(imgAnimeUrl + returnIdImg(rowItem) + ".png");

//            }
            } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(imagem);
        holder.imageView.setMaxHeight(65);
        holder.imageView.setMinimumHeight(65);
        holder.imageView.setMaxWidth(65);
        holder.imageView.setMinimumWidth(65);
        return convertView;
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

    public String returnIdImg(String tittle) throws IOException, JSONException {
        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(context);
        String id = new String();
        for (int i = 0; i < jsonAnimes.length(); i++) {
            JSONObject animeObject = jsonAnimes.getJSONObject(i);
            String name = animeObject.getString("Nome");
            listaAnimes.add(name);
        }
        for (int i = 0; i < listaAnimes.size(); i++){
            if (listaAnimes.get(i).equals(tittle)){
                JSONObject animeObject = jsonAnimes.getJSONObject(i);
                id = animeObject.getString("anime_imagem");

            }
        }
        return id;
    }


}