package com.teknestige.sinop;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teknestige.classes.CreateList;
import com.teknestige.classes.ImageListAdapter;
import com.teknestige.classes.Item;
import com.teknestige.classes.MyAdapter;
import com.teknestige.classes.RecyclerItemTouchHelper;
import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import DbControler.BDHelper;

public class InicioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Item> cartList;
    private ImageListAdapter mAdapter;
    private DrawerLayout coordinatorLayout;
    private ArrayList<String> listaManchetes = new ArrayList<String>();

    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();
//    Constants constants = new Constants();

    String imgNewUrl = bdHelper.returnUrl()+"ws_images_news/";

    int i;
    JSONArray jsonManchetes;
    String[] image_titles = new String[i];
    int[] image_ids = new int[i];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        construirUsuario();

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user_nick_header);
        text.setText(usuario.getNickname());

        printNavHederUser();

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        int isModera = sp.getInt("isModera", 0);
        if (isModera==1){
            Menu menu = (Menu) navigationView.getMenu();
            menu.findItem(R.id.nav_modera).setVisible(true);
        }



        try {
            jsonManchetes = bdHelper.selectAllFromMancheteNoticia(getApplicationContext());

            listarManchetes();
            bdHelper.deleteFromUsuarioTesterino(getApplicationContext(), getUserEmail());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createRecyclerView();
    }

    public void listarManchetes() throws IOException, JSONException {
        for (int i = 0; i < 5; i++) {
            JSONObject animeObject = jsonManchetes.getJSONObject(i);
            String nomeAnime = animeObject.getString("Manchete");
            listaManchetes.add(nomeAnime);
        }

        image_titles = new String[listaManchetes.size()];
        image_ids = new int[listaManchetes.size()];

        for (int i = 0; i < listaManchetes.size(); i++){
            image_titles[i]=listaManchetes.get(i);
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
//        String imgUserUrl = String.valueOf(bdHelper.returnUrl())+"ws_images_users/";
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user_nick_header);
        text.setText(usuario.getNickname());


        String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";

        ImageView pedra = (ImageView) header.findViewById(R.id.imageUserProfile);
        Bitmap imagem = LoadImageFromWebUser(imgUserUrl+getUserEmail()+".png");

        if (imagem == null) {
            pedra.setImageResource(R.drawable.img2);
            pedra.setMaxWidth(85);
            pedra.setMaxHeight(85);

        } else {
            pedra.setImageBitmap(imagem);
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
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

        return true;
    }



    public void createRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(InicioActivity.this, createLists, true);
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ImageListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartList.get(viewHolder.getAdapterPosition()).getName();


            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
        }
    }
    public ImageView LoadImageFromWebOperations(String url) {
        try {
            ImageView i = null;
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            i.setImageBitmap(bitmap);
            return i;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles[i]);
            createList.setImage_ID(image_ids[i]);
            theimage.add(createList);
        }
        return theimage;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }


        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        MenuItem acc1 = (MenuItem) drawer.findViewById(R.id.nav_modera);
        int isModera = sp.getInt("isModera", 0);
        if (isModera==1){
            acc1.setVisible(true);
        }

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
