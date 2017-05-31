package com.example.sheyla.cliente;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.Window;
        import android.view.WindowManager;

public class MainActivity extends Activity {
    ControlDeEscenas escenas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        escenas= new ControlDeEscenas(this);
        escenas.setKeepScreenOn(true);
        setContentView(escenas);


    }
    @Override
    public void onBackPressed() {

                super.onBackPressed();



    }
    @Override
    protected void onResume() {
        super.onResume();
        escenas = new ControlDeEscenas(this);
        escenas.setKeepScreenOn(true);
        setContentView(escenas);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


