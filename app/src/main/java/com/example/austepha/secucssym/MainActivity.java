package com.example.austepha.secucssym;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.key;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLoad (View v) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException {
        EditText mdp = (EditText) findViewById(R.id.mdp);
        EditText texte = (EditText) findViewById(R.id.texte);
        String untexte;
        Context context = v.getContext();
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(mdp.getText().toString().getBytes());
        byte[] digest = md.digest();
        byte[] skfromdigest = Arrays.copyOf(digest,16);
        //creation secret key
        SecretKey sk=new SecretKeySpec(skfromdigest,"AES");
        //création secret key
        //le cipher crée la clé

        Cipher c = Cipher.getInstance("AES");
        c.init (javax.crypto.Cipher.DECRYPT_MODE, sk);
        File file = new File (v.getContext().getFilesDir()+File.separator+"macle"); //retrouve le nom du fichier
        ObjectInputStream oos = new ObjectInputStream (new CipherInputStream(new FileInputStream(file), c));
        untexte = (String)oos.readObject(); //string car sinon il retourne un objet et donc pas précis, récupère texte
        texte.setText(untexte); // on met le texte dans un editTexte




    }

    public void onClickSave(View v) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {

        EditText mdp = (EditText) findViewById(R.id.mdp);
        EditText texte = (EditText) findViewById(R.id.texte);
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(mdp.getText().toString().getBytes());
        byte[] digest = md.digest();
        byte[] skfromdigest = Arrays.copyOf(digest,16);
        //creation secret key
        SecretKey sk=new SecretKeySpec(skfromdigest,"AES");
        //création secret key
        //le cipher crée la clé

        Cipher c = Cipher.getInstance("AES");
        c.init (javax.crypto.Cipher.ENCRYPT_MODE, sk);
        File file = new File (v.getContext().getFilesDir()+File.separator+"macle");
        ObjectOutputStream oos = new ObjectOutputStream (new CipherOutputStream(new FileOutputStream(file), c));
        oos.writeObject(texte.getText().toString());
        oos.flush();
        oos.close();



    }
}
