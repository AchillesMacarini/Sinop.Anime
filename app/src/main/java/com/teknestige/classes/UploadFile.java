package com.teknestige.classes;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

import static org.apache.commons.net.ftp.FTPCommand.PORT;

public class UploadFile {

    public void doInBackground(String uri, String filename) {
        FTPClient client = new FTPClient();
        try {
            client.connect("localhost", PORT);
            client.login("admin", "123123");
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.storeFile(filename, new FileInputStream(new File(
                    uri)));

        } catch (Exception e) {
            Log.d("FTP", e.toString());
//            return false;
        }
    }


//    @Override
//    protected void onPostExecute(Boolean sucess) {
//        if (sucess) {
//            Toast.makeText("file sent").show();
//        } else {
//            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
//        }
//    }
}