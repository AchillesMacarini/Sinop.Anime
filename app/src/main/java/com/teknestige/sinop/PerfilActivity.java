package com.teknestige.sinop;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import DbControler.BDHelper;

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();

    private Uri mImageCaptureUri;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String imgConUrl = bdHelper.returnUrl()+"ws_images_conquistas/";
    JSONArray jsonImagesCon = new JSONArray();
    ArrayList<String> jsonConquistas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog();
                int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                if (isStoragePermissionGranted()){
                    if (permission == PermissionChecker.PERMISSION_GRANTED) {
                        // good to go
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent , CAMERA_REQUEST);
                    } else {
                        // permission not granted, you decide what to do
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                }
            }
        });

        construirUsuario();
        printNavHederUser();
        try {
            buildTableDyn();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setInfoUser();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    }

    public void setInfoUser() throws IOException, JSONException {

        ImageView pedra = (ImageView) findViewById(R.id.prophoto);

        String imgUserUrl = bdHelper.returnUrl() + "ws_images_users/";

        Bitmap imagem = LoadImageFromWebUser(imgUserUrl + getUserEmail() + ".png");

        if (imagem == null) {
            pedra.setImageResource(R.drawable.img2);
        } else {
            pedra.setImageBitmap(imagem);
        }


        TextView nickname = (TextView) findViewById(R.id.nickname_edit);
        TextView biogra = (TextView) findViewById(R.id.biography_edit);
        TextView especialization = (TextView) findViewById(R.id.especialization);
        TextView tagCount = (TextView) findViewById(R.id.tag_num);
        TextView comCount = (TextView) findViewById(R.id.com_num);

        nickname.setText(usuario.getNickname());
        biogra.setText(usuario.getBiograph());
        especialization.setText("Moderador");

        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> comments = new ArrayList<>();

        JSONArray tagsJson = bdHelper.selectTagComent(getApplicationContext() , usuario.getEmail());

        for (int i = 0; i < tagsJson.length(); i++) {
            JSONObject animeObject = tagsJson.getJSONObject(i);
            String tag = animeObject.getString("tags");
            tags.add(tag);

            String com = animeObject.getString("coment");
            comments.add(com);

        }

        tagCount.setText(tags.get(0) + " TAGS");
        if (Integer.valueOf(comments.get(0)) == 1) {
            comCount.setText(comments.get(0) + " COMENTÁRIO");
        } else {
            comCount.setText(comments.get(0) + " COMENTÁRIOS");
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//            resume tasks needing this Manifest.permission
        }
    }

    public void onClicar(View v) {
// TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }
//https://stackoverflow.com/questions/14728157/dynamic-gridlayout
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    ImageView image = (ImageView) findViewById(R.id.prophoto);

        if (resultCode == RESULT_OK){
        Uri targetUri = data.getData();
        Bitmap bitmap;
        try {
        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
        imageView.setImageBitmap(bitmap);
        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            System.out.println("acho que deu");
        } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
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


    public void buildTableDyn() throws IOException, JSONException {
        {
            JSONArray jsnConquista = bdHelper.selectAllFromConquista(getApplicationContext(), getUserEmail());
            jsonImagesCon = bdHelper.selectAllFromConquistaImagem(getApplicationContext());
            ArrayList<String> newImgs = new ArrayList<String>();

            for (int i=0; i < jsnConquista.length(); i++){
                JSONObject newsObject = jsnConquista.getJSONObject(i);

                String nomeConquista = newsObject.getString("Conquista_Nome");

                jsonConquistas.add(nomeConquista);

                for (int j=0; j<jsonImagesCon.length(); j++) {
                    JSONObject imgObject = jsonImagesCon.getJSONObject(j);
                    String verificaConquista = imgObject.getString("Nome");
                    String idConquista = imgObject.getString("conquista_imagem");

                    if (nomeConquista.equals(verificaConquista)) {
                        newImgs.add(idConquista);
                    }
                }
            }


            GridLayout gl = (GridLayout) findViewById(R.id.gridLay);
            gl.setBackgroundResource(R.color.background);
            GridLayout.LayoutParams gridParam;
            System.out.println(imgConUrl+"01.png");
            for (int i = 0; i < jsonConquistas.size(); i++) {
                gl.addView(LoadImageFromWebOperations(imgConUrl+newImgs.get(i)+".png"));
            }
        }
    }

    public ImageView LoadImageFromWebOperations(String url) {
        try {

            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            ImageView i = new ImageView(this);
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

    public void buildAlertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        dialog.setTitle("Title...");
        EditText nickEdit = (EditText) dialog.findViewById(R.id.nickname_edit);
        final String nicknameEdit = nickEdit.getText().toString();
        EditText bioEdit = (EditText) dialog.findViewById(R.id.biography_edit);
        final String biographEdit = bioEdit.getText().toString();
        final EditText passEdit = (EditText) dialog.findViewById(R.id.pass_edit);
        final String passwordEdit = passEdit.getText().toString();
        EditText confPassEdit = (EditText) dialog.findViewById(R.id.pass_confirm);
        final String confirmPassEdit = confPassEdit.getText().toString();
        // set the custom dialog components - text, image and button
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogProfileOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OHMAMAMAE");
                if (passwordEdit.isEmpty() && confirmPassEdit.isEmpty()){
                    if (biographEdit.isEmpty()){
                        try {
                            System.out.println("ta ino");
                            bdHelper.updateUsuarios(getApplicationContext(), usuario.getEmail(), nicknameEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (nicknameEdit.isEmpty()){
                        //
                        try {
                            bdHelper.updateUsuariosBiograph(getApplicationContext(), usuario.getEmail(), biographEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        //os dois
                        try {
                            bdHelper.updateUsuariosBiograph(getApplicationContext(), usuario.getEmail(), biographEdit);
                            bdHelper.updateUsuarios(getApplicationContext(), usuario.getEmail(), nicknameEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (biographEdit.isEmpty()){
                    //apenas nick e senha
                    if (passwordEdit.isEmpty()){
                        //erro
                    }
                    else if (confirmPassEdit.isEmpty()){
                        //erroTb
                    }
                    else if (passwordEdit.equals(confirmPassEdit)){
                        try {
                            bdHelper.updateUsuarios(getApplicationContext(), usuario.getEmail(), nicknameEdit);
                            bdHelper.updateUsuariosSenha(getApplicationContext(), usuario.getEmail(), passwordEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (nicknameEdit.isEmpty()){
                    //apenas bio e senha
                    if (passwordEdit.isEmpty()){
                        //erro
                    }
                    else if (confirmPassEdit.isEmpty()){
                        //erroTb
                    }
                    else if (passwordEdit.equals(confirmPassEdit)){
                        try {
                            bdHelper.updateUsuariosBiograph(getApplicationContext(), usuario.getEmail(), biographEdit);
                            bdHelper.updateUsuariosSenha(getApplicationContext(), usuario.getEmail(), passwordEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (biographEdit.isEmpty() && nicknameEdit.isEmpty()){
                    //apenas senha

                    if (passwordEdit.isEmpty()){
                        //erro
                    }
                    else if (confirmPassEdit.isEmpty()){
                        //erroTb
                    }
                    else if (passwordEdit.equals(confirmPassEdit)){
                        try {
                            bdHelper.updateUsuariosSenha(getApplicationContext(), usuario.getEmail(), passwordEdit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
                else if (!biographEdit.isEmpty() && !nicknameEdit.isEmpty() && !passwordEdit.isEmpty() && !confirmPassEdit.isEmpty()){
                    //tudo
                    if (passwordEdit.equals(confirmPassEdit)){

                    }
                }


                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void printNavHederUser(){
        construirUsuario();

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


    public String getUserEmail() {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailName = sp.getString("emailLogado",null);
        return emailName;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);
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
