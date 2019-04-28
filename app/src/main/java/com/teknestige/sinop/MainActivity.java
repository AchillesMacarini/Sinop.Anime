package com.teknestige.sinop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.teknestige.entidades.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import DbControler.BDHelper;


public class MainActivity extends AppCompatActivity {

    BDHelper bdHelper = new BDHelper();
    Usuario usuario = new Usuario();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sp = getSharedPreferences("dadosCompartilhados" , Context.MODE_PRIVATE);
        String emailLogado = sp.getString("emailLogado" , null);
        String email = null;
        try {
            email = getUserEmail();
            buscarUsuario(email);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        irParaMain();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
        }
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
////            updateUI(account);
//        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

//    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
//    }

    public String getUserEmail() throws IOException, JSONException {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailName = sp.getString("emailLogado",null);
        return emailName;
    }

    private void buscarUsuario(String email) throws IOException, JSONException {
        JSONArray json = bdHelper.selectUserInfo(getApplicationContext(), email);
        JSONObject userObject = json.getJSONObject(0);
        usuario.setEmail(email);
        usuario.setBiograph(userObject.getString("Biograph"));
        usuario.setDataCadastro(userObject.getString("Data de cadastro"));
        usuario.setNickname(userObject.getString("Nickname"));
        usuario.setQtdTags(Integer.valueOf(userObject.getString("Quant. de tags")));
    }

    private void irParaMain() {
        SystemClock.sleep(100);

        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void goToRegister(View v){
        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }

}
