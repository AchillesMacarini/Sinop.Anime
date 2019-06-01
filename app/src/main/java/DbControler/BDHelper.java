package DbControler;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

      import org.apache.commons.net.ftp.FTP;
     import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BDHelper {

    private static String URL_GLOBAL_DB = "http://192.168.1.18/ws_otaku/";

    public JSONArray selectUserInfo(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_usuario_email_logado.php?email='"+email+"'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }
    public String returnUrl(){
        return URL_GLOBAL_DB;
    }

    public void goforIt(String uri){

        FTPClient con = null;
        /*********  work only for Dedicated IP ***********/
         final String FTP_HOST= "localhost";

        /*********  FTP USERNAME ***********/
        final String FTP_USER = "admin";
        try
        {
            con = new FTPClient();
            con.connect(URL_GLOBAL_DB);

            if (con.login("admin", "123123"))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = uri;

                FileInputStream in = new FileInputStream(new File(data));
                boolean result = con.storeFile(uri, in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void insertIntoTesterino(Context context, String conteudo) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {

        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_testerino.php?conteudo="+conteudo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();

        } else {

        }
    }

    public void deleteFromUsuarioTesterino(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_delete/ws_delete_from_usuario_testerino.php?email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();

        } else {

        }
    }

    public void insertIntoTesterinoUsuario(Context context, String conteudo, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {

        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_usuario_testerino.php?conteudo="+conteudo+"&email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();

        } else {

        }
    }

    public JSONArray selectAllFromTesterino(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_testerino.php?email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;

    }

    public JSONArray selectAllFromAnime(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_anime.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;

    }

    public JSONArray selectAllFromPesquisinha(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_pesquisinha.php?email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;

    }

    public JSONArray selectAllFromForbidden(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_palavras_proibidas.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }


    public JSONArray selectAllFromNoticias(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_noticia.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public int insertIntoUsuarios(Context context, String email, String nickname, String biograph, String senha, String dataCadastro, String qtdTags) throws IOException {
        if (!checkNetworkConnection(context)) {
            return 0;
        }
        checkThreadPolicy();
        String values = "Email="+email+"&"+"Nickname="+nickname+"&"+"Biograph="+biograph+"&"+"senha="+senha+"&"+"Data_cadastro="+dataCadastro+"&"+"Quant_tags="+qtdTags;
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_usuario.php?"+values);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
            return 0;
        } else {
            return 1;
        }
    }

    public JSONArray selectAllFromAnimeTag(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_anime_tag.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public JSONArray searchFromAnimeTag(Context context, String tagAnime) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_pesquisa_anime_tag.php?tagAnime='"+tagAnime+"'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;

        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public JSONArray selectAllFromUser(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_usuario.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        System.out.println("testando");
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }


    public void updateUsuarios(Context context, String email) throws IOException {
        if (!checkNetworkConnection(context)) {
            return;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_update/ws_update_usuarios.php?email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateRelacao(Context context, String anime, String tag) throws IOException {
        if (!checkNetworkConnection(context)) {
            return;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_update/ws_update_relacao_anime_tag.php?anime="+anime+"&tag="+tag);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateMinusRelacao(Context context, String anime, String tag) throws IOException {
        if (!checkNetworkConnection(context)) {
            return;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_update/ws_update_relacao_anime_tag.php?anime="+anime+"&tag="+tag);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
        }
    }


    private boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private void checkThreadPolicy(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
