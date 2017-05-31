package com.example.sheyla.cliente;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Sheyla on 19/05/2017.
 */

public class EscenaMenu extends Escena {
    Elemento btnStart;
    Elemento btnSalir;
    Elemento btnRecords;
    Elemento titulo;
    String idioma;
    Toast avisoOperacion, avisoCodigo;


    public EscenaMenu(Context context) {
        super(context);
        fondo = redimensionaImagen(R.dimen.alto, "fondoMenuMovil.png");
        fondo = Bitmap.createScaledBitmap(fondo, ancho, alto, false);
        idioma = Locale.getDefault().getLanguage().toLowerCase();

        Log.i("Idioma", idioma);
        Bitmap a, b, c, d;
        a = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnRecords.png");
        b = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnStart.png");
        d = redimensionaImagen(R.dimen.titulo, "tituloMovil.png");
        if (idioma.equals("es") || idioma.equals("gl") || idioma.equals("eu") || idioma.equals("ca")) {
            c = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnSalir.png");
        } else {
            c = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnExit.png");
        }
        btnStart = new Elemento(b, ancho / 2 - b.getWidth() / 2, (int) (alto / 3));
        btnSalir = new Elemento(c, ancho / 2 - c.getWidth() / 2, (int) (alto / 1.5));
        btnRecords = new Elemento(a, ancho / 2 - a.getWidth() / 2, (int) (alto / 2));
        titulo = new Elemento(d, ancho / 2 - d.getWidth() / 2, (int) (alto / 18));
    }


    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    public void dibujar(Canvas c) {
        super.dibujar(c);
        c.drawColor(Color.GREEN);
        c.drawBitmap(fondo, 0, 0, null);
        c.drawBitmap(btnRecords.imagen, btnRecords.posicionX, btnRecords.posicionY, null);
        c.drawBitmap(btnStart.imagen, btnStart.posicionX, btnStart.posicionY, null);
        c.drawBitmap(btnSalir.imagen, btnSalir.posicionX, btnSalir.posicionY, null);
        c.drawBitmap(titulo.imagen, titulo.posicionX, titulo.posicionY, null);
    }

    @Override
    public int gestionPulsacion(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (btnStart.rectangulo.contains((int) e.getX(), (int) e.getY())) {
                return -1;
            }
            if (btnSalir.rectangulo.contains((int) e.getX(), (int) e.getY())) {

                return -3;
            }
            if (btnRecords.rectangulo.contains((int) e.getX(), (int) e.getY())) {
                return -2;
            }

        }
        return 0;
    }


}


