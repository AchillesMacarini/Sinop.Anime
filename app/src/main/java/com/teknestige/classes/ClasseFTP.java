package com.teknestige.classes;

import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class ClasseFTP {
    FTPClient mFtp;
    private String TAG = "classeFTP";


    public FTPFile[] Dir(String Diretorio) {
        try {
            FTPFile[] ftpFiles = mFtp.listFiles(Diretorio);
            return ftpFiles;
        } catch (Exception e) {
            Log.e(TAG , "Erro: não foi possível  listar os   arquivos e pastas do diretório " + Diretorio + ". " + e.getMessage());
        }

        return null;
    }

    public boolean MudarDiretorio(String Diretorio)
    {
        try
        {
            mFtp.changeWorkingDirectory(Diretorio);
        }
        catch(Exception e)
        {
            Log.e(TAG, "Erro: não foi possível mudar o diretório para " + Diretorio);
        }
        return false;
    }

    public boolean Desconectar()
    {
        try
        {
            mFtp.disconnect();
            mFtp = null;
            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Erro: ao desconectar. " + e.getMessage());
        }

        return false;
    }

    public boolean Conectar()
    {
        try
        {
            mFtp = new FTPClient();

            mFtp.connect("10.21.80.199", 21);

            if (FTPReply.isPositiveCompletion(mFtp.getReplyCode())){
                boolean status = mFtp.login("admin", "123123");

                mFtp.setFileType(FTP.BINARY_FILE_TYPE);
                mFtp.enterLocalPassiveMode();

                return status;
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, "Erro: não foi possível conectar " + "localhost");
        }
        return false;
    }

    public boolean Upload(String diretorio, String nomeArquivo)
    {
        boolean status = false;
        try
        {
            File image = new File(Environment.getExternalStorageDirectory()+ diretorio, nomeArquivo);
            FileInputStream arqEnviar = new FileInputStream(image);
            mFtp.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
            mFtp.setFileType(FTPClient.STREAM_TRANSFER_MODE);
            mFtp.storeFile(nomeArquivo, arqEnviar);
            Desconectar();
            return status;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Erro: Falha ao efetuar Upload. " +  e.getMessage());
        }
        return status;
    }
}