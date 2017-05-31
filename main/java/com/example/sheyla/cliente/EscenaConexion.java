package com.example.sheyla.cliente;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Sheyla on 24/04/2017.
 */

public class EscenaConexion extends Escena {
    Elemento btnConectarse;
    Elemento lblInfo;
    Elemento txtCodigo;
    Elemento btnSalir;
    Paint pinturaTexto;
    StaticLayout layoutNombre;
    String idioma, codigoJugador = "";
    int posTextoY, posTextoX=0;
    Toast avisoOperacion, avisoCodigo;


    public EscenaConexion(Context context) {
        super(context);
        fondo = redimensionaImagen(R.dimen.alto, "fondoMenuMovil.png");
        fondo = Bitmap.createScaledBitmap(fondo, ancho, alto, false);
        idioma = Locale.getDefault().getLanguage().toLowerCase();
        avisoCodigo = Toast.makeText(context, "Introduce el numero que ves en la pantalla de juego", Toast.LENGTH_SHORT);
        avisoOperacion = Toast.makeText(context, "Esta operación puede tardar varios minutos", Toast.LENGTH_LONG);
        Log.i("Idioma", idioma);
        Bitmap a, b, c, d;
        c = redimensionaImagen(R.dimen.txtCodigo, "textBox.png");
        if (idioma.equals("es") || idioma.equals("gl") || idioma.equals("eu") || idioma.equals("ca")) {
            a = redimensionaImagen(R.dimen.lblInfoConexion, "introNum.png");
            b = redimensionaImagen(R.dimen.btnGrandes, "Botones/btnAuto.png");
            d = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnSalir.png");
        } else {
            b = redimensionaImagen(R.dimen.btnGrandes, "Botones/btnAutoIng.png");
            a = redimensionaImagen(R.dimen.lblInfoConexion, "introNumIng.png");
            d = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnExit.png");

        }
        lblInfo = new Elemento(a, ancho / 2 - a.getWidth() / 2, alto / 10);
        btnConectarse = new Elemento(b, ancho / 2 - b.getWidth() / 2, (int) (alto / 1.55));
        txtCodigo = new Elemento(c, ancho / 2 - c.getWidth() / 2, (int) (alto / 3));
        btnSalir = new Elemento(d, ancho / 2 - d.getWidth() / 2, (int) (alto / 1.2));
        pinturaTexto =new Paint();
        pinturaTexto.setTextSize(pixeles(R.dimen.tamañoTexto));
        pinturaTexto.setTextAlign(Paint.Align.CENTER);
        float textHeight = pixeles(R.dimen.tamañoTexto) ;
        posTextoY = (int)(txtCodigo.posicionY+(txtCodigo.imagen.getHeight()/2)+textHeight/2);
        Log.i("pos text Y",""+posTextoY);
        posTextoX = (int) ancho / 2;
        Log.i("pos text X",""+posTextoX);
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
        c.drawBitmap(btnConectarse.imagen, btnConectarse.posicionX, btnConectarse.posicionY, null);
        c.drawBitmap(lblInfo.imagen, lblInfo.posicionX, lblInfo.posicionY, null);
        c.drawBitmap(txtCodigo.imagen, txtCodigo.posicionX, txtCodigo.posicionY, null);
        c.drawBitmap(btnSalir.imagen, btnSalir.posicionX, btnSalir.posicionY, null);
        //c.save();
        //c.translate(posTextoX, posTextoY);
        c.drawText(codigoJugador,posTextoX,posTextoY,pinturaTexto);
        //c.restore();

    }

    @Override
    public int gestionPulsacion(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (btnConectarse.rectangulo.contains((int) e.getX(), (int) e.getY())) {
                if (codigoJugador.equals("")) {
                    avisoCodigo.show();

                } else {
                    avisoOperacion.show();
                    btnConectarse.enabled = false;
                    return -5;

                }
            }
            if (btnSalir.rectangulo.contains((int) e.getX(), (int) e.getY())) {
                ((Activity)context).finish();
            }
            if(txtCodigo.rectangulo.contains((int) e.getX(), (int) e.getY())) {
                showTeclado();
            }
        }
        return 0;
    }
    public boolean pulsaTeclas(int keyCode, KeyEvent event) {
        if ((event.getUnicodeChar() >= '0' && event.getUnicodeChar() <= '9')) {
//            efectos.play(sonidoPulsarBoton, 15, 15, 1, 0, 1);
            codigoJugador += "" + ((char) event.getUnicodeChar());
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_DEL) && codigoJugador.length() > 0) {
            codigoJugador = codigoJugador.substring(0, codigoJugador.length() - 1);
        }

        if (KeyEvent.KEYCODE_ENTER == keyCode) {
            hideTeclado();
            return true;
        }
        Log.i("Codigo jugador",codigoJugador);

        return false;
    }


}
