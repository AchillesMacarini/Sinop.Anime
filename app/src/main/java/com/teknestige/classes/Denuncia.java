package com.teknestige.classes;

import android.content.Context;

import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import DbControler.BDHelper;

public class Denuncia {
    private String emailDenun;
    private String cod;
    private String anime;
    private String comentario;
    Context c;

    BDHelper bdHelper = new BDHelper();
    Usuario usuario = new Usuario();

    ArrayList<Denuncia>  denunciasArray = new ArrayList<Denuncia>();

    public Denuncia() {
    }

    public String getEmailDenun() {
        return emailDenun;
    }

    public void setEmailDenun(String email) {
        this.emailDenun = email;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getAnime() {
        return anime;
    }

    public void setAnime(String anime) {
        this.anime = anime;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

  public void generateDenunciasArray(Context c) throws IOException, JSONException {
      JSONArray jsonsDenuncias = bdHelper.selectAllFromDenunComen(c);
      JSONArray jsonsComents = bdHelper.selectAllFromComents(c);

      for (int i=0; i<jsonsDenuncias.length(); i++){
          JSONObject denunObject = jsonsDenuncias.getJSONObject(i);
          String denunCod = denunObject.getString("comentario_idcomentario");
          String comenEmail = denunObject.getString("usuario_Email");
          denunciasArray.get(i).setEmailDenun(comenEmail);

          for (int j=0; j < jsonsComents.length(); j++){
              JSONObject comenObject = jsonsDenuncias.getJSONObject(i);
              String animeName = comenObject.getString("denunObject");
              String comenCod = comenObject.getString("idcomentario");
              if (denunCod.equals(comenCod)){
                  denunciasArray.get(i).setAnime(animeName);
              }
          }

      }
  }

  public ArrayList<String> returnEmailArrayDenuncias (Context c) throws IOException, JSONException {
        generateDenunciasArray(c);

        ArrayList<String> emailArrayDenuncias = new ArrayList<String>();

        for (int i=0; i<denunciasArray.size(); i++){

            emailArrayDenuncias.add(denunciasArray.get(i).getEmailDenun());

        }
        return emailArrayDenuncias;
  }

    public ArrayList<String> returnAnimeArrayDenuncias (Context c) throws IOException, JSONException {
        generateDenunciasArray(c);

        ArrayList<String> animeArrayDenuncias = new ArrayList<String>();

        for (int i=0; i<denunciasArray.size(); i++){

            animeArrayDenuncias.add(denunciasArray.get(i).getAnime());
        }
        return animeArrayDenuncias;
    }

}
