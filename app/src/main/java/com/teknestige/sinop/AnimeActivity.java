package com.teknestige.sinop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.teknestige.entidades.Anime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import DbControler.BDHelper;

public class AnimeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();
    Anime animes = new Anime();
    ArrayList<String> listaNomesAnimes = new ArrayList<String>();

    long startTime = System.nanoTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        construirUsuario();
        printNavHederUser();

        try {
            arrayAnimes();
            construirAnime();
            settarAnimeView();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void arrayAnimes() throws IOException, JSONException {

        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(getApplicationContext());
        for (int i = 0; i < jsonAnimes.length(); i++) {
            JSONObject animeObject = jsonAnimes.getJSONObject(i);
            String nomeAnime = animeObject.getString("Nome");
            listaNomesAnimes.add(nomeAnime);
        }
    }

    public String nomeClicado(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String nomeClicado = bundle.getString("nomeAnime");
        return nomeClicado;

    }

    public void construirAnime() throws IOException, JSONException {

        JSONArray jsonTodosAnimes = bdHelper.selectAllFromAnime(getApplicationContext());
        for (int i = 0; i < jsonTodosAnimes.length(); i++) {
            JSONObject animeTudosObject = jsonTodosAnimes.getJSONObject(i);
            String nomeAnime = animeTudosObject.getString("Nome");
            if (listaNomesAnimes.get(i).equals(nomeClicado())) {

                animes.setNome(nomeClicado());
                animes.setClassificacao(animeTudosObject.getString("Classificacao indicativa"));
//                animes.setData_lan(Date.valueOf(animeTudosObject.getString("Data de lancamento")));
                animes.setDiretor(animeTudosObject.getString("Diretor"));
                animes.setEstudio(animeTudosObject.getString("Estudio"));
                animes.setNum_ep(animeTudosObject.getInt("Numero de episodios"));
                animes.setSinopse(animeTudosObject.getString("Sinopse"));
                animes.setStatus(animeTudosObject.getString("Status do anime"));

            }
        }

    }

    public void settarAnimeView(){
        TextView nomeView = (TextView)findViewById(R.id.animeNameView);
        nomeView.setGravity(Gravity.CENTER);
        nomeView.setText(animes.getNome());
        System.out.println(animes.getNome());

//        TextView diretoriew = (TextView)findViewById(R.id.diretorView);
//        diretoriew.setText("Diretor: " + animes.getDiretor());

        TextView estudioView = (TextView)findViewById(R.id.estudioVIew);
        estudioView.setText("Estudio: " + animes.getEstudio());

//        TextView statusView = (TextView)findViewById(R.id.statusView);
//        statusView.setText("Status: " + animes.getStatus());

        TextView classificacaoView = (TextView)findViewById(R.id.classificacaoView);
        classificacaoView.setText("Classificação Indicativa: " + animes.getClassificacao());

        TextView episoView = (TextView)findViewById(R.id.numEpView);
        episoView.setText(String.valueOf(animes.getNum_ep()) + " episódios");

        TextView sinopseView = (TextView)findViewById(R.id.sinopseView);
        sinopseView.setText(animes.getSinopse());

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

                System.out.println("hoje é festa");
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


        @Override
    public void finish() {
            long endTime = System.nanoTime();
//        NumberFormat formatter = new DecimalFormat("#0.00");
            long totalTime = NANOSECONDS.toSeconds(endTime - startTime);
            System.out.println(totalTime + " seconds");
            Intent in = getIntent();
            Bundle b = in.getExtras();
            String abcd[] = b.getStringArray("myKey");
            if (totalTime < 3) {
                for (int i = 0; i < abcd.length; i++) {
                    try {
                        bdHelper.updateMinusRelacao(getApplicationContext() , abcd[i] , getUserEmail().toLowerCase());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                super.finish();
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anime, menu);
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
}
