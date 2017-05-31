package com.example.sheyla.cliente;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Sheyla on 24/04/2017.
 */

public class Elemento {
    public Bitmap imagen;
    public int posicionX;
    public int posicionY;
    Boolean enabled;
    Rect rectangulo;

    public Elemento(Bitmap imagen, int posicionX, int posicionY) {
        this.imagen = imagen;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.rectangulo=new Rect(posicionX,posicionY,posicionX+imagen.getWidth(),posicionY+imagen.getHeight());
        this.enabled =true;
    }
}
