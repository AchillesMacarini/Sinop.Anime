//package com.teknestige.classes;
//
//import android.content.Context;
//import android.widget.Filter;
//
//import com.teknestige.entidades.Usuario;
//import com.teknestige.sinop.ListaAnimesActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import DbControler.BDHelper;
//
//public class AnimeFilter extends Filter {
//    BDHelper bdHelper = new BDHelper();
//    Usuario usuario = new Usuario();
//    ArrayList<String> list = new ArrayList<String>();
//    public ArrayList<String> listaNomes = new ArrayList<String>();
//    ArrayList<Item> rowItems = new ArrayList<Item>();
//    ListaAnimesActivity activity;
//    Context c;
//
//    @Override
//    protected FilterResults performFiltering(CharSequence constraint) {
//        FilterResults filterResults = new FilterResults();
//
//        if (constraint != null && constraint.length() > 0) {
//            ArrayList<String> tempList = new ArrayList<>();
//
//            JSONArray jsonAnimes = null;
//            try {
//                jsonAnimes = bdHelper.selectAllFromAnime(activity.getApplicationContext());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Item[] item = new Item[jsonAnimes.length()];
//
//            for (int i = 0; i < jsonAnimes.length(); i++) {
//                JSONObject animeObject = null;
//                try {
//                    animeObject = jsonAnimes.getJSONObject(i);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String nomeAnime = null;
//                try {
//                    nomeAnime = animeObject.getString("Nome");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                listaNomes.add(nomeAnime);
//            }
//
//                for (int i = 0; i < listaNomes.size(); i++) {
//                    if (listaNomes.get(i).toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        tempList.add(listaNomes.get(i));
//                    }
//                }
//                filterResults.count = tempList.size();
//                filterResults.values = tempList;
//            }else{
//            filterResults.count = listaNomes.size();
//            filterResults.values = listaNomes;
//        }
//        return filterResults;
//        }
//
//    public ArrayList<String> getListaNomes(){
//        JSONArray jsonAnimes = null;
//        try {
//            jsonAnimes = bdHelper.selectAllFromAnime(activity.getApplicationContext());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Item[] item = new Item[jsonAnimes.length()];
//
//        for (int i = 0; i < jsonAnimes.length(); i++) {
//            JSONObject animeObject = null;
//            try {
//                animeObject = jsonAnimes.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String nomeAnime = null;
//            try {
//                nomeAnime = animeObject.getString("Nome");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            listaNomes.add(nomeAnime);
//        }
//
//
//
//        return listaNomes;
//    }
//
//
//    @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults (CharSequence constraint , FilterResults results){
//        filt
//        }
//    }
