package com.teknestige.sinop;

import android.app.Dialog;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.teknestige.classes.CommentListAdapter;
import com.teknestige.classes.CreateList;
import com.teknestige.entidades.Anime;
import com.teknestige.entidades.Comment;
import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DbControler.BDHelper;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class AnimeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();
    Anime animes = new Anime();
    Comment comment = new Comment();
    ArrayList<String> listaAnimes = new ArrayList<String>();
    ArrayList<String> listaNomesAnimes = new ArrayList<String>();
    String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";
    String imgAnimeUrl = bdHelper.returnUrl()+"ws_images_animes/";
    String imgWideUrl =  bdHelper.returnUrl()+"ws_images_wide_anime/";
    String imgNewUrl = bdHelper.returnUrl()+"ws_images_news/";
    long startTime = System.nanoTime();
    ArrayList<String> coments = new ArrayList<String>();
    ArrayList<Comment> commentArrayList = new ArrayList<Comment>();

    String imgConUrl = bdHelper.returnUrl()+"ws_images_conquistas/";
    JSONArray jsonImagesCon = new JSONArray();
    ArrayList<String> jsonConquistas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
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
            arrayAnimes();
            construirAnime();
            settarAnimeView();
            buildComments();
            setCommentView();
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

//        ArrayAdapter<String> adapterComment = new ArrayAdapter<String>(this, R.layout.coment_layout_item, R.id.textView11, listaNomesAnimes);
//        ListView neoListView = (ListView) findViewById(R.id.comentRecycler);
//
//        neoListView.setAdapter(adapterComment);
    }

public void buildComments() throws  IOException, JSONException{
    JSONArray jsonComments = bdHelper.selectAllFromComentario(getApplicationContext(), nomeClicado());

    for (int i=0; i<jsonComments.length(); i++){
        JSONObject commentObject = jsonComments.getJSONObject(i);
        String coment = commentObject.getString("comentario_cont");
        coments.add(coment);
    }




    ListView neoListView = (ListView) findViewById(R.id.comentRecycler);
    CommentListAdapter adapter = new CommentListAdapter(AnimeActivity.this, nomeClicado(), commentArray());
    neoListView.setAdapter(adapter);
}

    public ArrayList<Comment> commentArray() throws IOException, JSONException {
        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(getApplicationContext());
        JSONArray jsonComments = bdHelper.selectAllFromComentario(getApplicationContext(), nomeClicado());
    for (int i=0; i<jsonComments.length(); i++){
        JSONObject animeObject = jsonAnimes.getJSONObject(i);
        JSONObject commentObject = jsonComments.getJSONObject(i);
        String coment = commentObject.getString("comentario_cont");
        String email = commentObject.getString("usuario_Email");
        String id = commentObject.getString("idcomentario");
        comment.setEmailCom(email);
        comment.setConteudo(coment);
        comment.setIdCom(id);

        commentArrayList.add(comment);
    }
        return commentArrayList;
}

    public String nomeClicado(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String nomeClicado = bundle.getString("nome");
        return nomeClicado;
    }

    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();


        return theimage;
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

    public void settarAnimeView() throws IOException, JSONException {
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

        WebView sinopseView = (WebView) findViewById(R.id.sinopseView);

        String text = "<html><body>"
                + "<p align=\"justify\"" +
                "style=\"font-family:Arial; color:white\">"
                + "<body style=\"background-color:#1d1f2d;\">"
                + animes.getSinopse()
                + "</p> "
                + "</body></html>";

        sinopseView.loadData(text, "text/html", "utf-8");
        System.out.println("olha aqui " + imgWideUrl+(returnIdImg(animes.getNome()))+".png");

        ImageView animeMain = findViewById(R.id.animeMain);
        animeMain.setImageBitmap(LoadImageFromWeb(imgAnimeUrl+(returnIdImg(animes.getNome()))+".png"));

        ImageView animeWide = findViewById(R.id.animeWide);
        animeWide.setImageBitmap(LoadImageFromWeb(imgWideUrl+(returnIdImg(animes.getNome()))+".png"));

    }





    public String returnIdImg(String tittle) throws IOException, JSONException {
        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(getApplicationContext());
        String id = new String();
        for (int i = 0; i < jsonAnimes.length(); i++) {
            JSONObject animeObject = jsonAnimes.getJSONObject(i);
            String name = animeObject.getString("Nome");
            listaAnimes.add(name);
        }

        for (int i = 0; i < jsonAnimes.length(); i++){
            if (listaAnimes.get(i).equals(tittle)){
                JSONObject animeObject = jsonAnimes.getJSONObject(i);
                id = animeObject.getString("anime_imagem");

            }
        }
        return id;
    }


    public Bitmap LoadImageFromWeb(String url) {
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


    public void btnHelpPressed(View v){
        minusUpdater();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, ListaAnimesActivity.class);
            startActivity(intent);
            super.onBackPressed();


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


    public void denunciar (Context context, String id, String email) {
        try {
            bdHelper.insertIntoDenunciaComentario(context, id, email);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void buildAlertDialog(String email) {
        //Como upar img???????????????????????
        // custom dialog
        final Dialog dialog = new Dialog(AnimeActivity.this);
        dialog.setContentView(R.layout.dialog_denuncia);
        dialog.setTitle("Title...");

        TextView userName = (TextView) dialog.findViewById(R.id.userName);
        userName.setText(email);

        // set the custom dialog components - text, image and button
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogClose2);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OHMAMAMAE");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void setCommentView (){
        String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";

        ImageView pedra = findViewById(R.id.imageView7);
        Bitmap imagem = LoadImageFromWebUser(imgUserUrl+getUserEmail()+".png");

        if (imagem != null) {
            pedra.setImageBitmap(imagem);
            pedra.setMinimumWidth(85);
            pedra.setMinimumHeight(85);
            pedra.setMaxWidth(85);
            pedra.setMaxHeight(85);
        }

    }

    @Override
    public void finish() {
        long endTime = System.nanoTime();
        long totalTime = NANOSECONDS.toSeconds(endTime - startTime);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String abcd[] = b.getStringArray("myKey");
        if (totalTime < 3) {
            for (int i = 0; i < abcd.length; i++) {
                    minusUpdater();
            }
            super.finish();
        }
    }

    public void minusUpdater(){
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String abcd[] = b.getStringArray("myKey");
            for (int i = 0; i < abcd.length; i++) {
                try {
                    bdHelper.updateMinusRelacao(getApplicationContext() , abcd[i] , getUserEmail().toLowerCase());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";

        ImageView pedra = (ImageView) header.findViewById(R.id.imageUserProfile);
        Bitmap imagem = LoadImageFromWebUser(imgUserUrl+getUserEmail()+".png");

        if (imagem == null) {
            pedra.setImageResource(R.drawable.img2);
            pedra.setMinimumWidth(105);
            pedra.setMinimumHeight(105);
            pedra.setMaxWidth(105);
            pedra.setMaxHeight(105);

        } else {
            pedra.setImageBitmap(imagem);
            pedra.setMinimumWidth(105);
            pedra.setMinimumHeight(105);
            pedra.setMaxWidth(105);
            pedra.setMaxHeight(105);

        }


    }


    public Bitmap LoadImageFromWebUser(String url) {
        try {

            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            ImageView i = new ImageView(this);
            i.setImageBitmap(bitmap);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

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
            Intent intent = new Intent(this, ConfiguracaoActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_modera) {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_denuncias);
            dialog.setTitle("Title...");

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogBurronOK);

            ListView denunciasList = (ListView) dialog.findViewById(R.id.listDenuncias);



            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }else if (id == R.id.nav_send) {
            editor.remove("emailLogado");
            editor.remove("nickLogado");
            editor.remove("biographLogado");
            editor.remove("dateLogado");
            editor.remove("qntLogado");
            editor.remove("isModera");
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
