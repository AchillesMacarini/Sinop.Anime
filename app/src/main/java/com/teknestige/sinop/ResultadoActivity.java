package com.teknestige.sinop;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.teknestige.classes.CreateList;
import com.teknestige.classes.MyAdapter;
import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tartarus.snowball.ext.portugueseStemmer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import DbControler.BDHelper;

public class ResultadoActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();
    ArrayList<String> nomesParecidos = new ArrayList<String>();
    ArrayList<String> listaNomes = new ArrayList<String>();
    ArrayList<String> arrayPesquisas = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        int isModera = sp.getInt("isModera", 0);
        if (isModera==1){
            Menu menu = (Menu) navigationView.getMenu();
            menu.findItem(R.id.nav_modera).setVisible(true);
        }

        construirUsuario();
        printNavHederUser();


        try {
            removeDangerWords();
            createRecyclerView();
            System.out.println("deu cero");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("deu ero");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("deu ero2");
        }
    }


    public void removeDangerWords() throws IOException, JSONException {
        String s = handleIntent(getIntent());
        portugueseStemmer stemmer = new portugueseStemmer();
        String[] words = s.split("\\s+");

        JSONArray jsonForbidden = bdHelper.selectAllFromForbidden(getApplicationContext());
        String [] palavras = new String[jsonForbidden.length()];

        if (jsonForbidden != null) {
            for (int i=0;i<jsonForbidden.length();i++){
                JSONObject userObject = jsonForbidden.getJSONObject(i);
                palavras[i] = (userObject.getString("id_palavras_proibidas"));
            }
        }


        for (int i = 0; i < words.length; i++) {

            words[i] = words[i].replaceAll("[^\\w]", "");
            list.add(words[i]);
            for (int j=0; j<palavras.length; j++) {
//                stemmer.setCurrent(list.get(i));
                if (words[i].toLowerCase().equals(palavras[j])) {
                    list.remove(words[i]);
                }
            }
        }

        for (int j=0; j < list.size(); j ++){
                bdHelper.insertIntoTesterinoUsuario(getApplicationContext(), list.get(j), getUserEmail().toLowerCase());
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private String handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            return query;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                bdHelper.deleteFromUsuarioTesterino(getApplicationContext(), getUserEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onBackPressed();
        }
    }

//    public void makeAnimeArray() throws IOException, JSONException{
//
//        JSONArray jsonPesquisas = bdHelper.selectAllFromPesquisinha(getApplicationContext(), getUserEmail());
//        JSONArray jsonTesterino = bdHelper.selectAllFromTesterino(getApplicationContext(), getUserEmail());
//
//        if (jsonTesterino != null) {
//
//            for (int i=0;i<jsonTesterino.length();i++){
//                JSONObject userObject = jsonTesterino.getJSONObject(i);
//                arrayPesquisas.add(userObject.getString("Testerino_conteudo"));
//            }
//        }
//        List<Item> rowItems = new ArrayList<Item>();
//        Item[] item = new Item[jsonPesquisas.length()];
//
//
//        for (int i = 0; i < jsonPesquisas.length(); i++) {
//            JSONObject animeObject = jsonPesquisas.getJSONObject(i);
//            String nomeAnime = animeObject.getString("Anime_Nome");
//            listaNomes.add(nomeAnime);
//
//            item[i] = new Item();
//            item[i].setTitle(nomeAnime);
//
//            rowItems.add(item[i]);
//
//        }
//
//        if (listaNomes.isEmpty()){
//            Intent voltar = new Intent(this, MainActivity.class);
//            startActivity(voltar);
//        }
//
//        ListView neoListView = (ListView) findViewById(R.id.listViewResultado);
//            CustomListViewAdapter adapter = new CustomListViewAdapter(this ,
//                    R.layout.list_style , listaNomes);
//            neoListView.setOnItemClickListener(this);
//            neoListView.setAdapter(adapter);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String nomeAnime =  listaNomes.get(position);
        Bundle b = new Bundle();
        Intent intent = new Intent(this, AnimeActivity.class);
        b.putString("nomeAnime", nomeAnime.toString());
        intent.putExtras(b);

        for (int i=0; i<arrayPesquisas.size(); i++) {
            try {
                bdHelper.updateRelacao(getApplicationContext(), nomeAnime, arrayPesquisas.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra("myKey", arrayPesquisas);
        startActivity(intent);
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

    public void createRecyclerView() throws IOException, JSONException {
        JSONArray jsonPesquisas = bdHelper.selectAllFromPesquisinha(getApplicationContext(), getUserEmail());
        JSONArray jsonTesterino = bdHelper.selectAllFromTesterino(getApplicationContext(), getUserEmail());

        if (jsonTesterino != null) {

            for (int i=0;i<jsonTesterino.length();i++){
                JSONObject userObject = jsonTesterino.getJSONObject(i);
                arrayPesquisas.add(userObject.getString("Testerino_conteudo"));
            }
        }
        ArrayList<CreateList> rowItems = new ArrayList<CreateList>();
        CreateList[] item = new CreateList[jsonPesquisas.length()];


        for (int i = 0; i < jsonPesquisas.length(); i++) {
            JSONObject animeObject = jsonPesquisas.getJSONObject(i);
            String nomeAnime = animeObject.getString("Anime_Nome");
            listaNomes.add(nomeAnime);

            item[i] = new CreateList();
            item[i].setImage_title(nomeAnime);

            rowItems.add(item[i]);

        }

        if (listaNomes.isEmpty()){
            Intent voltar = new Intent(this, MainActivity.class);
            startActivity(voltar);
        }



        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.listViewResultado);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
//        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(ResultadoActivity.this, rowItems, false);
        recyclerView.setAdapter(adapter);

        String path = Environment.getRootDirectory().toString();
        File f = new File(path);
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            CreateList createList = new CreateList();
            createList.setImage_Location(file[i].getName());
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
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

    public void alertDialog (String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        DialogInterface.OnClickListener btn = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        };

        builder.setPositiveButton("Ok", btn);
        builder.create().show();
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
