package com.teknestige.sinop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.teknestige.classes.GradientBackgroundPainter;
import com.teknestige.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DbControler.BDHelper;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    BDHelper bdHelper = new BDHelper();
    Usuario usuario = new Usuario();
    boolean encontrado = false;
    private GradientBackgroundPainter gradientBackgroundPainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailLogado = sp.getString("emailLogado", null);
        String email = null;

        if (emailLogado != null) {


            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        //  attemptLogin();
                        return true;
                    }
                    return false;
                }


            });


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

//        final View targetView = findViewById(R.id.root_layout);
//
//        BackgroundPainter backgroundPainter = new BackgroundPainter();
//
//        int color1 = ContextCompat.getColor(this, R.color.colorAccent);
//        int color2 = ContextCompat.getColor(this, R.color.colorPrimary);
//
//        backgroundPainter.animate(targetView, color1, color2);


//        View backgroundImage = findViewById(R.id.root_layout);
//
//        final int[] drawables = new int[3];
//        drawables[0] = R.drawable.gradient_1;
//        drawables[1] = R.drawable.gradient_2;
//        drawables[2] = R.drawable.gradient_3;
//
//        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
//        gradientBackgroundPainter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gradientBackgroundPainter.stop();
    }

    public void irCadastro(View v) {

        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
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

    public void logar(View view) throws JSONException, IOException {
        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        EditText senhaText = (EditText) findViewById(R.id.password);
        String senha = senhaText.getText().toString();
        verificarUsuario(email.toUpperCase(), senha);
    }

    private void verificarUsuario(String emailDigitado, String senhaDigitada) throws IOException, JSONException {
        JSONArray jsons = bdHelper.selectAllFromUser(getApplicationContext());
        String biographUser = "";
        String nickUser = "";
        String dateUser = "";
        String qntUser = "";

        for (int i = 0; i < jsons.length(); i++) {
            JSONObject userObject = (JSONObject) jsons.get(i);
            String emailUser = userObject.getString("Email");
            String senhaUser = userObject.getString("senha");
            biographUser = userObject.getString("Biograph");
            nickUser = userObject.getString("Nickname");
            dateUser = userObject.getString("Data de cadastro");
            qntUser = userObject.getString("Quant. de tags");

            if (emailUser.toUpperCase().equals(emailDigitado) && senhaUser.equals(senhaDigitada)) {
                encontrado = true;
//                isPasswordValid(true);
                break;
            }
        }

        boolean isModerador = false;

        if (encontrado) {
            gerarAlertDialog("LOGIN", "Login efetuado com sucesso");

            JSONArray jsonModera = bdHelper.selectModerador(getApplicationContext(), emailDigitado);
            if (jsonModera.length() == 0) {
                isModerador = false;
            } else {
                isModerador = true;
            }
            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("emailLogado", emailDigitado);
            editor.putString("nickLogado", nickUser);
            editor.putString("biographLogado", biographUser);
            editor.putString("dateLogado", dateUser);
            editor.putString("qntLogado", qntUser);
            if (isModerador) {
                editor.putInt("isModera", 1);
            } else {
                editor.putInt("isModera", 0);
            }

            editor.apply();

            irParaMain();


        } else {
            gerarAlertDialog("LOGIN", "tá errado issae");
        }
    }

    private void irParaMain() {
        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }

    public void gerarAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }


    public String getUserEmail() throws IOException, JSONException {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailName = sp.getString("emailLogado", null);
        return emailName;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(View.INVISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

    }
}
