package com.example.sheyla.cliente;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Sheyla on 25/04/2017.
 */

public class ControlDeEscenas extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Context context;
    boolean funcionando;
    Movimiento movimiento;
    Escena escenaAcutal = null;
    String mensaje = "0", respuesta="";



    long tiempoDormido = 0; //Tiempo que va a dormir el hilo
    final int FPS = 50; // Nuestro objetivo
    final int TPS = 1000000000; //Ticks en un segundo para la función usada nanoTime()
    final int FRAGMENTO_TEMPORAL = TPS / FPS; // Espacio de tiempo en el que haremos todo de forma repetida
    // Tomamos un tiempo de referencia actual en nanosegundos más preciso que currenTimeMillis()
    long tiempoReferencia = System.nanoTime();


    public ControlDeEscenas(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        movimiento = new Movimiento(this);
        setFocusable(true);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        funcionando = true;


    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("hola", "surfaceChanged");
        if (escenaAcutal == null) {
            ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            escenaAcutal = new EscenaConexion(context);
        }
        synchronized (escenaAcutal) {
            escenaAcutal.prepare();
        }

        if (movimiento.getState() == Thread.State.NEW)
            movimiento.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        funcionando = false;
        try {
            movimiento.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gestorEscenas(int escena) {

        switch (escena) {
            case 1:

                break;
            case 2:

                break;


        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int respuesta = escenaAcutal.gestionPulsacion(event);
        gestorEscenas(respuesta);

        if(respuesta==-1){//ESCENA JUEGO
            mensaje="JUEGO";
        }
        if(respuesta==-2) {//ESCENA RECORDS
            mensaje="RECORDS";
        }
        if(respuesta==-3){//ESCENA SALIR
            mensaje="EXIT";
        }
        if(respuesta==-4){//ESCENA SALIR
            mensaje="START";
        }
        if (respuesta == -5) {//LANZA EL HILO CLIENTE
            Thread hilo = new Thread(new Conexion(this));
            hilo.start();
        }

        return true;

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (escenaAcutal.getClass() == EscenaConexion.class) {
            if (((EscenaConexion) escenaAcutal).pulsaTeclas(keyCode, event)) {
                gestorEscenas(1);
            }
        }
        return false;
    }

    public void reaccionaProtocolo(String respuesta){
        switch (respuesta){
            case "Bienvenido al juego":
                escenaAcutal=new EscenaMenu(context);
                break;
            case "JUEGO":
                escenaAcutal=new EscenaJuego(context);
                break;
        }

    }
}

class Movimiento extends Thread {
    ControlDeEscenas v;


    public Movimiento(ControlDeEscenas v) {
        this.v = v;
    }

    public void run() {
        long tiempoDormido = 0; //Tiempo que va a dormir el hilo
        final int FPS = 24; // Nuestro objetivo
        final int TPS = 1000000000; //Ticks en un segundo para la función usada nanoTime()
        final int FRAGMENTO_TEMPORAL = TPS / FPS; // Espacio de tiempo en el que haremos todo de forma repetida
        // Tomamos un tiempo de referencia actual en nanosegundos más preciso que currenTimeMillis()
        long tiempoReferencia = System.nanoTime();
        while (v.funcionando) {
            Canvas c = null;
            try {
                c = v.surfaceHolder.lockCanvas();
                synchronized (v.surfaceHolder) {
                    //if (v.escenaAcutal.comienza) {

                    v.escenaAcutal.actualizarFisica();
                    v.escenaAcutal.dibujar(c);
                    //} else {
                    //   c.drawColor(Color.BLACK);
                    // }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();

                if (c != null) {
                    c.drawColor(Color.RED);
                }
            } finally {
                if (c != null) {
                    v.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            /*// Calculamos el siguiente instante temporal donde volveremos a actualizar y pintar
            tiempoReferencia += FRAGMENTO_TEMPORAL;
            // El tiempo que duerme será el siguiente menos el actual (Ya ha terminado de pintar y actualizar)
            tiempoDormido = tiempoReferencia - System.nanoTime();
            //Si tarda mucho, dormimos.
            if (tiempoDormido > 0) {
                try {
                    Thread.sleep(tiempoDormido / 1000000); //Convertimos a ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }


    }


}

class Conexion extends Thread {
    ControlDeEscenas v;

    PrintWriter salida;
    BufferedReader entrada;
    int puerto;
    WifiManager wifiManager;
    int ipAddress;
    int posicion3;
    int posicion4;
    String ip;
    String ipIncompleta;


    public Conexion(ControlDeEscenas v) {
        this.v = v;
        this.puerto = Integer.parseInt(((EscenaConexion) v.escenaAcutal).codigoJugador);
        wifiManager = (WifiManager) v.context.getSystemService(WIFI_SERVICE);
        ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        posicion3 = (ipAddress >> 16 & 0xff);
        posicion4 = (ipAddress >> 24 & 0xff);


        ip = "192.168." + posicion3 + "0";
        ipIncompleta = "192.168." + posicion3 + ".";
        posicion4 = 0;
    }


    @Override
    public void run() {
        Log.i("fallo", "Entra conexion");

        conectar();

    }


    private boolean conectaPuerto() {
        boolean error;
        do {
            error = false;
            if (posicion4 > 255) {
                return false;
            }
            try {
                Socket sk = new Socket(); //A veces se atasca -->consultar unknow host exception? --> consultar serverException?
                sk.connect(new InetSocketAddress(ip, puerto), 15000);

                Log.i("soket", "entra conecta");
                entrada = new BufferedReader(
                        new InputStreamReader(sk.getInputStream()));


                salida = new PrintWriter(
                        new OutputStreamWriter(sk.getOutputStream()), true);

                salida.println("Hola");
                Log.i("fallo", "Mandado hola");
                v.respuesta = entrada.readLine();
                Log.i("fallo", "recibida respuesta");
                if (!v.respuesta.equalsIgnoreCase("Bienvenido al juego")) {
                    Log.i("fallo", "lo intentaste");
                    throw new ConnectException();

                }
                Log.i("fallo", "llega por aqui");


            } catch (ConnectException e) {
                String mensajeError = e.toString();
                Log.i("fallo", posicion4 + ": " + mensajeError);

                aumentaIp();


                error = true;

            } catch (IOException io) {
                Log.i("fallo", puerto + ": " + io.toString());
                error = true;
                aumentaIp();
            }

        } while (error);
        Log.i("fallo", puerto + ": Conectado");
        return true;
    }

    private void aumentaIp() {
        Log.i("fallo", "entra ip");
        posicion4++;
        ip = ipIncompleta + posicion4;
    }


    private void conectar() {
        if (conectaPuerto()) {
            v.escenaAcutal=new EscenaMenu(v.context);
            while (v.respuesta != null) {
                try {
                    if(!v.mensaje.equals("0")) {
                        salida.println(v.mensaje);
                        Log.i("CONEXION", "imprime");
                        Log.i("CONEXION", "esperando lectura");
                        v.respuesta = entrada.readLine();
                        Log.i("CONEXION", "leido");
                        v.mensaje = "0";
                        Log.i("mensaje", v.mensaje);
                        Log.i("respuesta", v.respuesta);
                        v.reaccionaProtocolo(v.respuesta);
                        if(v.mensaje.equals("EXIT")){
                            ((Activity)v.context).finish();
                        }
                    }
                } catch (IOException e) {
                e.printStackTrace();
                }
            }
            Log.i("CONEXION","FIN");


        } else {
            Log.i("fallo", "No se ha encntrado conexion");
        }

    }


}