package com.example.sheyla.cliente;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Sheyla on 25/04/2017.
 */

public class BosquejosCodigo {
/*
    import android.app.Activity;
    import android.net.wifi.WifiInfo;
    import android.net.wifi.WifiManager;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Looper;
    import android.os.StrictMode;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;

    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.io.OutputStreamWriter;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.ConnectException;
    import java.net.InetAddress;
    import java.net.InetSocketAddress;

    import java.net.Socket;


    import android.util.Log;
    import android.widget.Toast;

    import javax.net.ServerSocketFactory;

    public class MainActivity extends Activity {
        TextView output;

        Button b1;
        Button bDer;
        Button bIzq;
        Button bArriba;
        Button bAbajo;
        public EditText introPuerto;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            output = (TextView) findViewById(R.id.textView);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .permitNetwork().build());
            b1 = (Button) findViewById(R.id.button);
            introPuerto = (EditText) findViewById(R.id.editText);



            bAbajo = (Button) findViewById(R.id.button12);
            bArriba = (Button) findViewById(R.id.button11);
            bIzq = (Button) findViewById(R.id.button13);
            bDer = (Button) findViewById(R.id.button14);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickB1(v);
                }
            });
            bArriba.setEnabled(true);
            bAbajo.setEnabled(true);
            bDer.setEnabled(true);
            bIzq.setEnabled(true);
        }

        public void onClickB1(View v) {
            if (introPuerto.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Introduce el numero que ves en la pantalla de juego",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Esta operación puede tardar varios minutos", Toast.LENGTH_LONG).show();
                Thread hilo = new Thread(new Conexion(this));
                hilo.start();
                b1.setEnabled(false);
            }

        }


        private void log(String string) {
            output.append(string + "\n");
        }

        class Conexion extends Thread {

            private final int MIN_PORT = 31300;
            private final int MAX_PORT = 32000;
            PrintWriter salida;
            com.example.sheyla.cliente.MainActivity act;
            int puerto=Integer.parseInt(introPuerto.getText()+"");
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            int posicion3=(ipAddress >> 16 & 0xff);
            int posicion4=(ipAddress >> 24 & 0xff);
            String ip = "192.168."+posicion3+".0";


            public Conexion(com.example.sheyla.cliente.MainActivity s) {
                act = s;
            }

            @Override
            public void run() {
                Log.i("fallo",introPuerto.getText()+"");

                conectar();

                act.bAbajo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        salida.println("Abajo");
                        Log.i("fallo", "Abajo");
                    }
                });
                act.bArriba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        salida.println("arriba");
                        Log.i("fallo", "arriba");
                    }
                });
                act.bDer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        salida.println("derecha");
                        Log.i("fallo", "derecha");
                    }
                });
                act.bIzq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        salida.println("Izquierda");
                        Log.i("fallo", "izquierda");
                    }
                });
            }


            private boolean conectaPuerto() {
                boolean error;
                // puerto = MIN_PORT;
                do {
                    error = false;
                    //  puerto++;
                    if (puerto > MAX_PORT) {

                        return false;
                    }
                    try {

                        Socket sk = new Socket(); //A veces se atasca -->consultar unknow host exception? --> consultar serverException?
                        sk.connect(new InetSocketAddress(ip, puerto), 500);

                        Log.i("fallo", "entra conecta");
                        BufferedReader entrada = new BufferedReader(
                                new InputStreamReader(sk.getInputStream()));


                        salida = new PrintWriter(
                                new OutputStreamWriter(sk.getOutputStream()), true);

                        salida.println("Hola");
                        Log.i("fallo", "entra conecta2");
                        String mensaje=entrada.readLine();
                        Log.i("fallo", "entra conecta3");
                        if (!mensaje.equalsIgnoreCase("Bienvenido al juego")) {
                            Log.i("fallo", "lo intentaste");
                            throw new ConnectException();

                        }
                        Log.i("fallo", "llega por aqui");



                        error = false;

                    } catch (ConnectException e) {
                        String mensajeError = e.toString();
                        Log.i("fallo", puerto + ": " + mensajeError);

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

            private boolean aumentaIp() {
                Log.i("fallo", "entra ip");
                String[] ip_troceada = ip.split("\\.");
                Log.i("fallo", ip + " longitud: " + ip_troceada.length);
                Log.i("fallo", "trocea");

                ip_troceada[3] = "" + (Integer.parseInt(ip_troceada[3]) + 1);

                if (Integer.parseInt(ip_troceada[3])==255) {
                    Log.i("fallo", "Compru  " + ip.equalsIgnoreCase("192.168.255.255"));
                    return false;

                }


                ip = "";
                for (String s : ip_troceada) {
                    ip += "." + s;
                }
                ip = ip.substring(1);
                return true;

            }


            private void conectar() {
                while (!conectaPuerto()) {
                    Log.i("fallo", "Compru3  " + ip.equalsIgnoreCase("192.168.255.255")); //No entra en los log de esta funcion, ni hace nada.

                    if (!aumentaIp()) {
                        Log.i("fallo", "no se encuentra conexion");
                        Log.i("fallo", "Compru2  " + ip.equalsIgnoreCase("192.168.255.255"));
                        break; // no llega ???????????????
                    }
                }

                Log.i("fallo", "Conexion realizada");


            }

        }
    }*/
}
