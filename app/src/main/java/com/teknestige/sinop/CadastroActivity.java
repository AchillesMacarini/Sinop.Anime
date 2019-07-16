package com.teknestige.sinop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teknestige.classes.Constants;
import com.teknestige.classes.RealPathUtils;
import com.teknestige.entidades.Usuario;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import DbControler.BDHelper;

public class CadastroActivity extends AppCompatActivity {

    Usuario usuario = new Usuario();
    BDHelper bdHelper = new BDHelper();

    String data;
    String password;

    private ImageView imageView;
    private EditText editText;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        requestStoragePermission();
        imageView = (ImageView) findViewById(R.id.profileImage);
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

    public void uploadMultipart(String name) {
        try {
            new MultipartUploadRequest(getApplicationContext() , Constants.UPLOAD_URL)
                    .addFileToUpload(filePath , "image")
                    .addParameter("name" , name)
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception exc) {
            Toast.makeText(this , exc.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public void showFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent , "Selecione uma imagem") , PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode , resultCode , data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = RealPathUtils.getRealPathFromURI(this , data.getData());
            System.out.println("FILE PATH: " + filePath);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , data.getData());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this , "Permissão concedida!" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this , "Permissão negada!" , Toast.LENGTH_LONG).show();
            }
        }
    }


    public String data() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        data = sdf.format(Calendar.getInstance().getTime());
        System.out.println("testando: " + data);
        return data;
    }

    public boolean verifyPassword() {

        EditText senhaText = (EditText) findViewById(R.id.passI);
        String passI = senhaText.getText().toString().toLowerCase();

        EditText confirmarSenhaText = (EditText) findViewById(R.id.passII);
        String passII = confirmarSenhaText.getText().toString().toLowerCase();

        if (passI.equals(passII)) {
            password = passI;
            return true;
        } else {
            return false;
        }
    }

    private void cadastrar() throws IOException {
        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString().toLowerCase();

        EditText nickText = (EditText) findViewById(R.id.nickname);
        String nickname = nickText.getText().toString().toLowerCase();

        EditText bioText = (EditText) findViewById(R.id.biograph);
        String biograph = bioText.getText().toString().toLowerCase();

        ImageView wrong = (ImageView) findViewById(R.id.wrondDesu);

        if (verifyPassword() == true) {
            wrong.setVisibility(ImageView.INVISIBLE);
            bdHelper.insertIntoUsuarios(getApplicationContext() , email , nickname , biograph , password , data() , "0");
            uploadMultipart(email);
            buildShared(email, nickname, biograph, data(), "0");
        } else {
            wrong.setVisibility(ImageView.VISIBLE);
        }
    }


    public void next (View v){
        try {
            cadastrar();
            irParaMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildShared(String email, String nickUser, String biographUser, String dateUser, String qntUser){
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("emailLogado", email);
        editor.putString("nickLogado", nickUser);
        editor.putString("biographLogado", biographUser);
        editor.putString("dateLogado",dateUser);
        editor.putString("qntLogado", qntUser);
        editor.apply();
    }

    private void irParaMain() {
        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }

}
