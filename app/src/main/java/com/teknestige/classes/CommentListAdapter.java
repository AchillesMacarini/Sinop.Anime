package com.teknestige.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.teknestige.entidades.Anime;
import com.teknestige.entidades.Comment;
import com.teknestige.sinop.AnimeActivity;
import com.teknestige.sinop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DbControler.BDHelper;

public class CommentListAdapter extends BaseAdapter {
    // ArrayList<String> name, company, email, id, status;
    Comment comment;
    ArrayList<Comment> commentArrayList;
    Context c;
    String anime;
    BDHelper bdHelper = new BDHelper();


    String imgConUrl = bdHelper.returnUrl()+"ws_images_conquistas/";
    JSONArray jsonImagesCon = new JSONArray();
    ArrayList<String> jsonConquistas = new ArrayList<String>();


    public CommentListAdapter(Context c, String anime, ArrayList<Comment> list) {
        this.c = c;
        this.anime = anime;
        this.commentArrayList = list;

        try {
            buildComments();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return commentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View row = null;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        if(position==0){
//
//            if (convertView == null) {
//                row = inflater.inflate(R.layout.create_comment_layout, parent,
//                        false);
//            } else {
//                row = convertView;
//            }
//        }
//
//        else if (position > 0) {
            final Comment comment = commentArrayList.get(position);

            if (convertView == null) {
                row = inflater.inflate(R.layout.coment_layout_item, parent,
                        false);
            } else {
                row = convertView;
            }

            final Context newCont = c;
            final AnimeActivity animeActivity = new AnimeActivity();
            TextView conteudo = (TextView) row.findViewById(R.id.conteudoCommentView);
            conteudo.setText(comment.getConteudo());
            final TextView email = (TextView) row.findViewById(R.id.emailCommentView);
            email.setText(comment.getEmail());
            final String getEmail = comment.getEmail();
            final String getId = comment.getIdCom();
            Button denunciar = (Button) row.findViewById(R.id.denunBtn);
            TextView likesView = row.findViewById(R.id.likesView);
            likesView.setText(comment.getLikes() + " likes");

            String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";

            ImageView imageView = (ImageView) row.findViewById(R.id.imageView7);
            imageView.setImageBitmap(LoadImageFromWebUser(imgUserUrl+comment.getEmail()+".png"));

            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(newCont);
                    dialog.setContentView(R.layout.dialog_see_profile);

                    String imgUserUrl = bdHelper.returnUrl()+"ws_images_users/";

                    String biographUser = "";
                    String nickUser = "";

                    try {
                        JSONArray jsons = bdHelper.selectAllFromUser(c);

                        for (int i = 0; i < jsons.length(); i++) {
                            JSONObject userObject = (JSONObject) jsons.get(i);
                            String emailUser = userObject.getString("Email");
                            biographUser = userObject.getString("Biograph");
                            nickUser = userObject.getString("Nickname");

                            if (emailUser.toUpperCase().equals(comment.getEmail().toUpperCase())) {

                                TextView emailuser = (TextView) dialog.findViewById(R.id.nameNickView);
                                emailuser.setText(nickUser);

                                TextView nickuser = (TextView) dialog.findViewById(R.id.descriptionView);
                                nickuser.setText(biographUser);

                                ImageView imageView1 = (ImageView) dialog.findViewById(R.id.prophoto);
                                imageView1.setImageBitmap(LoadImageFromWebUser(imgUserUrl+comment.getEmail()+".png"));


                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.show();
                }
            });

            denunciar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(newCont);
                    dialog.setContentView(R.layout.dialog_denuncia);
                    dialog.setTitle("Title...");

                    TextView emailuser = (TextView) dialog.findViewById(R.id.userName);
                    emailuser.setText(comment.getEmail() + "?");
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogClose2);

                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        animeActivity.denunciar(newCont, comment.getIdCom(), comment.getEmail());

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

        return row;
    }


    public Bitmap LoadImageFromWebUser(String url) {
        try {
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
    public ImageView LoadImageFromWebOperations(String url) {
        try {

            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            ImageView i = new ImageView(c);
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




    public void buildComments() throws IOException, JSONException {
        System.out.println(c + " - " + anime);
//        JSONArray jsonComments = bdHelper.selectAllFromComentario(c, anime);
//        JSONArray jsonAnimes = bdHelper.selectAllFromAnime(c);
        for (int i=0; i<commentArrayList.size(); i++){
//            JSONObject animeObject = jsonAnimes.getJSONObject(i);
//            JSONObject commentObject = jsonComments.getJSONObject(i);
//            String coment = commentObject.getString("comentario_cont");
//            String email = commentObject.getString("usuario_Email");
//            String id = commentObject.getString("idcomentario");
//            comment.setEmailCom("capivarar@coisor.com");
//            comment.setConteudo(coment);
//            comment.setIdCom(id);

//            commentArrayList.add(comment);
     System.out.println("aaaaa");

        }
    }


}
