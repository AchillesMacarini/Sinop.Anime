package com.teknestige.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.teknestige.sinop.ListaAnimesActivity;
import com.teknestige.sinop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DbControler.BDHelper;

public class ListaNomesAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> listaAnimes = new ArrayList<String>();
    BDHelper bdHelper = new BDHelper();
    Context context;
    ArrayList<String> animeList;
    String imgAnimeUrl = bdHelper.returnUrl()+"ws_images_animes/";
    private ArrayList<String> animeFiltered;
    ArrayList<String> animeFilter;
    private Typeface typeface;
    ListaAnimesActivity activity;

    public ListaNomesAdapter(ListaAnimesActivity activity, ArrayList<String> animeList) {
        this.activity = activity;
        this.animeList = animeList;

        getFilter();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String rowItem = (String) getItem(position);

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


    @Override
    public int getCount() {
        return animeFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return animeFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<String> results = new ArrayList<>();
                if (animeFiltered == null)
                    animeFiltered = animeList;
                if (constraint != null) {
                    if (animeFiltered != null && animeFiltered.size() > 0) {
                        for (int i=0; i < animeList.size(); i++) {
                            if (animeList.get(i).toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(animeList.get(i));
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults (CharSequence constraint, FilterResults results){
                animeFiltered = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

/*private view holder class*/
class ViewHolder {
    ImageView imageView;
    TextView txtTitle;
}

public class AnimeFilter extends Filter {
    ArrayList<String> list = new ArrayList<String>();
    public ArrayList<String> listaNomes = new ArrayList<String>();
    ArrayList<Item> rowItems = new ArrayList<Item>();
    ListaAnimesActivity activity;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            ArrayList<String> tempList = new ArrayList<>();

            JSONArray jsonAnimes = null;
            try {
                jsonAnimes = bdHelper.selectAllFromAnime(activity.getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Item[] item = new Item[jsonAnimes.length()];

            for (int i = 0; i < jsonAnimes.length(); i++) {
                JSONObject animeObject = null;
                try {
                    animeObject = jsonAnimes.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String nomeAnime = null;
                try {
                    nomeAnime = animeObject.getString("Nome");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listaNomes.add(nomeAnime);
            }

            for (int i = 0; i < listaNomes.size(); i++) {
                if (listaNomes.get(i).toLowerCase().contains(constraint.toString().toLowerCase())) {
                    tempList.add(listaNomes.get(i));
                }
            }
            filterResults.count = tempList.size();
            filterResults.values = tempList;
        }else{
            filterResults.count = listaNomes.size();
            filterResults.values = listaNomes;
        }
        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults (CharSequence constraint, FilterResults results){
        animeFiltered = (ArrayList<String>) results.values;
        notifyDataSetChanged();
        }
    }
}