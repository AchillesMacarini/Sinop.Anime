package com.teknestige.sinop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.teknestige.entidades.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DbControler.BDHelper;

public class NoticiaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    BDHelper bdHelper = new BDHelper();
    Noticia noticia = new Noticia();
    String imgNewUrl = bdHelper.returnUrl()+"ws_images_news/";

    ArrayList<String> listaNews = new ArrayList<String>();
    String newsNamae;
    Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            construirNoticia();
            setImageNew();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent intent = new Intent(this, InicioActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, ListaAnimesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, ListaNoticiasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, ListaAnimesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("emailLogado");
            editor.remove("nickLogado");
            editor.remove("biographLogado");
            editor.remove("dateLogado");
            editor.remove("qntLogado");
            editor.apply();


            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String nomeClicado(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String nomeClicado = bundle.getString("nomeNews");
        return nomeClicado;
    }

    public void construirNoticia() throws IOException, JSONException {
        JSONArray jsonNews = bdHelper.selectAllFromNoticias(getApplicationContext());
        for (int i = 0; i < jsonNews.length(); i++) {
            JSONObject newsObject = jsonNews.getJSONObject(i);
            String nomeManchete = newsObject.getString("Manchete");
            System.out.println(nomeClicado());
            if (nomeManchete.equals(nomeClicado())) {
                String conteudo = newsObject.getString("Conteudo");
                String data = newsObject.getString("Data");
                noticia.setManchete(nomeManchete);
                noticia.setConteudo(conteudo);
                noticia.setData(data);
            }
        }

        TextView titulo = (TextView) findViewById(R.id.tituloView);
        titulo.setText(noticia.getManchete());
        TextView conteudo = (TextView) findViewById(R.id.conteudoView);
        conteudo.setText(noticia.getConteudo());
        TextView data = (TextView) findViewById(R.id.dataView);
        data.setText(noticia.getData());


    }

    public void setImageNew() throws IOException, JSONException{

        try {
            System.out.println(imgNewUrl+returnIdImg(noticia.getManchete().toString())+".png");
            imagem = LoadImageFromWebOperations(imgNewUrl+returnIdImg(noticia.getManchete().toString())+".png");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView11);
        imageView.setImageBitmap(imagem);
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
        JSONArray jsonNews = bdHelper.selectAllFromNoticias(getApplicationContext());

        for (int i = 0; i < jsonNews.length(); i++) {
            JSONObject animeObject = jsonNews.getJSONObject(i);
            String name = animeObject.getString("Manchete");
            if (name.equals(noticia.getManchete())) {
                String id = animeObject.getString("noticia_imagem");
            newsNamae = id;

            }
        }
        return newsNamae;
    }


}
