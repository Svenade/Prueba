package com.example.sheyla.cliente;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sheyla on 24/04/2017.
 */

public class Escena {
    Bitmap fondo;
    int ancho, alto;
    Context context;
    Typeface fuente;
    Paint texto;
    boolean apaisado;
    InputMethodManager imm;

    public Escena(Context context) {
        this.context = context;
        final WindowManager w = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ancho = realDisplayMetrics.heightPixels;
            alto = realDisplayMetrics.widthPixels;
            apaisado = true;
        } else {
            apaisado = false;
            alto = realDisplayMetrics.heightPixels;
            ancho = realDisplayMetrics.widthPixels;
        }
    }
    public int pixeles(int recurso) {
        int anchoSupuesto = 843;

        return this.ancho * (context.getResources().getDimensionPixelSize(recurso)) / anchoSupuesto;
    }
    public Bitmap redimensionaImagen(int tamano, String imagen) {
        Bitmap f = getBitmapFromAssets(imagen);
        int altoNuevo = pixeles(tamano);
        Log.i("nuevo", "altonuevo: " + altoNuevo);
        int anchoNuevo = f.getWidth() * altoNuevo / f.getHeight();

        Log.i("nuevo", "X: " + anchoNuevo + ":Y: " + altoNuevo);
        f = Bitmap.createScaledBitmap(f, anchoNuevo, altoNuevo, false);
        return f;
    }
    public Bitmap getBitmapFromAssets(String archivo) {
        try {
            InputStream is=context.getAssets().open(archivo);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.i("Fallo assets",e.getMessage());
            return null;
        }
    }
    public void actualizarFisica(){

    }
    public  void dibujar(Canvas c){

    }
    public int gestionPulsacion(MotionEvent e){
    return 0;
    }
    public void prepare(){

    }
    public void showTeclado() {
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void hideTeclado() {
        imm.toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public boolean pulsaTeclas(int keyCode, KeyEvent event) {
        if ((event.getUnicodeChar() >= 'a' && event.getUnicodeChar() <= 'z') || (event.getUnicodeChar() >= 'A' && event.getUnicodeChar() <= 'Z')) {

        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_DEL)) {

        }

        if (KeyEvent.KEYCODE_ENTER == keyCode) {

            hideTeclado();
        }
        return false;

    }
    public int cambiosExternos(){
        return 0;
    }


}
