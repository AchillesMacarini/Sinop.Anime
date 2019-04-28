package com.teknestige.sinop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import DbControler.BDHelper;

public class ListaAnimesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener  {
    BDHelper bdHelper = new BDHelper();

    Usuario usuario = new Usuario();
    ArrayList<String> listaNomes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_animes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lista de Animes");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        construirUsuario();
        printNavHederUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        System.out.println("Até aqui okay");

        try {
            arrayAnimes();
            System.out.println("Deu certo");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Deu erro"+e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Deu erro 2");
        }
    }

    public void construirUsuario(){
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailUser = sp.getString("emailLogado",null);
        usuario.setEmail(emailUser);
        String nickUser = sp.getString("nickLogado",null);
        usuario.setNickname(nickUser);
        String biographUser = sp.getString("biographLogado",null);
        usuario.setBiograph(biographUser);
        String dateUser = sp.getString("dateLogado",null);
        usuario.setDataCadastro(dateUser);
        String qntUser = sp.getString("qntLogado",null);
        usuario.setQtdTags(Integer.valueOf(String.valueOf(qntUser)));
    }

    public String getUserEmail() {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailName = sp.getString("emailLogado",null);
        return emailName;
    }

    public void printNavHederUser(){
        nickNavHeaderUser();
        emailNavHeaderUser();
    }

    public void nickNavHeaderUser(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user_nick_header);
        text.setText(usuario.getNickname());
    }

    public void emailNavHeaderUser(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user_email_header);
        text.setText(usuario.getEmail().toLowerCase());
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
        getMenuInflater().inflate(R.menu.lista_animes, menu);
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

    public void arrayAnimes() throws IOException, JSONException //
     {

        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(getApplicationContext());


        for (int i = 0; i < jsonAnimes.length(); i++) {
            JSONObject animeObject = jsonAnimes.getJSONObject(i);
            String nomeAnime = animeObject.getString("Nome");
            listaNomes.add(nomeAnime);
        }
            listaNomes.add("nomeAnime");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_style, R.id.textview, listaNomes);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNomes);
         ListView neoListView = (ListView) findViewById(R.id.myAnimeList);

         neoListView.setOnItemClickListener(this);
        neoListView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String nomeAnime =  listaNomes.get(position);
        Bundle b = new Bundle();
        Intent intent = new Intent(this, AnimeActivity.class);
        b.putString("nomeAnime", nomeAnime.toString());
        intent.putExtras(b);
        startActivity(intent);
    }
}
