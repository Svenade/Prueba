package com.example.sheyla.cliente;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Sheyla on 21/05/2017.
 */

public class EscenaJuego extends Escena implements SensorEventListener {
    SensorManager sensorManager;
    private Sensor accelerometer;
    Paint texto;
    boolean calibrated;
    String idioma;
    Elemento btnCalibrar;
    private int x, y, z;
    private int calibratedX = 0;
    private int calibratedY = 0;
    private int calibratedZ = 0;
    private int minX=0, maxX=0;
    private int minY=0, maxY=0;
    private int minZ=0, maxZ=0;


    public EscenaJuego(Context context) {
        super(context);
        fondo = redimensionaImagen(R.dimen.alto, "fondoMenuMovil.png");
        fondo = Bitmap.createScaledBitmap(fondo, ancho, alto, false);
        texto = new Paint();
        texto.setColor(Color.WHITE);
        texto.setTextSize(pixeles(R.dimen.tamañoTexto));
        calibrated = false;
        idioma = Locale.getDefault().getLanguage().toLowerCase();
        Bitmap a;
        if (idioma.equals("es") || idioma.equals("gl") || idioma.equals("eu") || idioma.equals("ca")) {
            a = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnCalibrar.png");
        } else {
            a = redimensionaImagen(R.dimen.btnPequeños, "Botones/btnCalibrate.png");
        }
        btnCalibrar = new Elemento(a, ancho / 2 - a.getWidth() / 2, alto / 2 - a.getHeight() / 2);


        sensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = (int) event.values[0];
        y = (int) event.values[1];
        z = (int) event.values[2];//Z??
        if (event.values[0] > maxX) {
            Log.i("If", "max X: " + (x - calibratedX) + "   " + calibratedX);
        }
        if(event.values[0] < minX) {
            Log.i("If", "min X: " + (x - calibratedX) + "   " + calibratedX);
        }
        if (event.values[1] < minY){
            Log.i("If", "min Y: " + (y - calibratedY) + "   " + calibratedY);
        }
        if(event.values[1] > maxY) {
            Log.i("If", "max Y: " + (y - calibratedY) + "   " + calibratedY);
        }
        if (event.values[2] < minZ){
            Log.i("If", "min Z: " + (z - calibratedZ) + "   " + calibratedZ);
        }
        if(event.values[2] > maxZ) {
            Log.i("If", "max Z: " + (z - calibratedZ) + "   " + calibratedZ);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void dibujar(Canvas c) {
        super.dibujar(c);
        c.drawBitmap(fondo, 0, 0, null);
        c.drawBitmap(btnCalibrar.imagen, btnCalibrar.posicionX, btnCalibrar.posicionY, null);
    }

    @Override
    public int gestionPulsacion(MotionEvent e) {
        if (btnCalibrar.rectangulo.contains((int) e.getX(), (int) e.getY())) {
            calibrated = true;
            calibratedX = x;
            minX=calibratedX-7;
            maxX=calibratedX+7;
            calibratedY = y;
            minY=calibratedY-7;
            maxY=calibratedY+7;
            calibratedZ = z;
            minZ=calibratedZ-7;
            maxZ=calibratedZ+7;
            return -4;
        }

        return super.gestionPulsacion(e);
    }
}
