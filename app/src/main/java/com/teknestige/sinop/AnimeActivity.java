package com.teknestige.sinop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teknestige.classes.CreateList;
import com.teknestige.classes.MyAdapter;
import com.teknestige.entidades.Anime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    String imgUserUrl = "http://192.168.1.28/ws_otaku/ws_images_users/";
    String imgAnimeUrl = "http://192.168.1.28/ws_otaku/ws_images_animes/";
    String imgNewUrl = "http://192.168.1.28/ws_otaku/ws_images_news/";
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


//        try {
//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_inicio);
//            ImageView pedra = (ImageView) headerView.findViewById(R.id.imageUserProfile);
//            pedra.setImageBitmap(LoadImageFromWebOperations(imgUserUrl+getUserEmail()+".png"));
//        }catch (NullPointerException e) {
////            ImageView pedra = (ImageView) findViewById(R.id.imageView4);
////            pedra.setImageResource(R.drawable.img2);
//        }

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


    public void arrayAnimes() throws IOException, JSONException {

        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(getApplicationContext());
        for (int i = 0; i < jsonAnimes.length(); i++) {
            JSONObject animeObject = jsonAnimes.getJSONObject(i);
            String nomeAnime = animeObject.getString("Nome");
            listaNomesAnimes.add(nomeAnime);
        }

        ArrayAdapter<String> adapterComment = new ArrayAdapter<String>(this, R.layout.coment_layout_item, R.id.textView11, listaNomesAnimes);
        ListView neoListView = (ListView) findViewById(R.id.comentRecycler);

        neoListView.setAdapter(adapterComment);

        ArrayAdapter<String> adapterAnswer = new ArrayAdapter<String>(this, R.layout.answer_layout_item, R.id.textView111, listaNomesAnimes);
        ListView neoNeoListView = (ListView) neoListView.findViewById(R.id.answersList);

        neoNeoListView.setAdapter(adapterAnswer);

    }

public void colocarLayoutChato() throws  IOException, JSONException{

}
    public String nomeClicado(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String nomeClicado = bundle.getString("nomeAnime");
        return nomeClicado;

    }

    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();


        return theimage;
    }
//
//    public void createRecyclerView(){
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.comentRecycler);
//        recyclerView.setHasFixedSize(true);
//
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
//        recyclerView.setLayoutManager(layoutManager);
//        ArrayList<CreateList> createLists = prepareData();
//        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
//        recyclerView.setAdapter(adapter);
//
//        String path = Environment.getRootDirectory().toString();
//        File f = new File(path);
//        File file[] = f.listFiles();
//        for (int i=0; i < file.length; i++)
//        {
//            CreateList createList = new CreateList();
//            createList.setImage_Location(file[i].getName());
//        }
//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                // Row is swiped from recycler view
//                // remove it from adapter
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//
//        // attaching the touch helper to recycler view
//        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
//    }

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

        ImageView pedra = (ImageView) header.findViewById(R.id.imageUserProfile);
        pedra.setImageBitmap(LoadImageFromWebOperations(imgUserUrl+getUserEmail()+".png"));
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
