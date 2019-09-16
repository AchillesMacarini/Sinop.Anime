package com.teknestige.classes;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teknestige.entidades.Comment;
import com.teknestige.sinop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter {
    // ArrayList<String> name, company, email, id, status;
    Comment comment;
    ArrayList<Comment> commentArrayList;
    Context c;
    String anime;
    JSONArray jsonAnimes;
    JSONArray jsonComments;

    public CommentListAdapter(Context c, String anime, ArrayList<Comment> list) {
        this.c = c;
        this.anime = anime;
        this.commentArrayList = list;
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

    public void buildAlertDialog() {
        //Como upar img???????????????????????
        // custom dialog
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.dialog_denuncia);
        dialog.setTitle("Title...");

        TextView userName = (TextView) dialog.findViewById(R.id.userName);
        userName.setText(comment.getEmail());

        // set the custom dialog components - text, image and button
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogProfileOK);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View row = null;
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = inflater.inflate(R.layout.coment_layout_item, parent,
                    false);
        } else {
            row = convertView;
        }

        Comment comment = commentArrayList.get(position);
        TextView conteudo = (TextView) row.findViewById(R.id.conteudoCommentView);
        conteudo.setText(comment.getConteudo());
        TextView email = (TextView) row.findViewById(R.id.emailCommentView);
        email.setText(comment.getEmail());

        Button denunciar = (Button) row.findViewById(R.id.denunBtn);
        denunciar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildAlertDialog();
        }
    });

        return row;
    }

    public ArrayList<Comment> buildComments() throws IOException, JSONException {
        System.out.println(c + " - " + anime);

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


}
