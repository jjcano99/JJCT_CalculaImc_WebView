package com.formacion.juanjosecanotena.jjct_calculaimc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BaseDatosUsuarios baseDatosUsuarios;
    Usuario usuario;
    SharedPreferences preferences;
    Funciones f ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANCIAMOS LAS FUNCIONES
        f = new Funciones();

        //SELECTORES
        final TextView tvParametrosIncorrectos = (TextView) findViewById(R.id.tvParametrosIncorrectos);
        final TextView tvNombreApe = (TextView) findViewById(R.id.tvNombreApe); //SOLO EN VERTICAL
        final TextView tvUltimoPeso = (TextView) findViewById(R.id.tvUltimoPeso); //SOLO EN VERTICAL
        final EditText etPeso = (EditText) findViewById(R.id.etPeso);
        final EditText etAltura = (EditText) findViewById(R.id.etAltura);
        Button bCalcular = (Button) findViewById(R.id.bCalcular);
        Button bCerrarSesion = (Button) findViewById(R.id.bCerrarSesion); //SOLO VERTICAL
        Button bSalir = (Button) findViewById(R.id.bSalir);//SOLO VERTICAL
        Button bBaja = (Button) findViewById(R.id.bBaja);//SOLO VERTICAL

        //DETECTAR ORIENTACION
        // 1 : ORIENTATION_PORTRAIT
        // 2 : ORIENTATION_LANDSCAPE
        int orientacion = this.getResources().getConfiguration().orientation;

        //REFERENCIAR BASE DE DATOS DE USUARIOS
        baseDatosUsuarios = new BaseDatosUsuarios(this,"miDB",null,1);

        //LEER PREFERENCIA idUsuario
        preferences = getSharedPreferences("Preferencias",MODE_PRIVATE);
        final int idUsuario = preferences.getInt("idUsuario",0);

        //LEER USUARIO COMPLETO DE LA BASE DE DATOS
        usuario = new Usuario();
        usuario=baseDatosUsuarios.seleccionarUsuario(idUsuario);

        //SI ESTÁ EN VERTICAL PONEMOS NOMBRE Y ÚLTIMO PESO
        float ultimoPeso = usuario.getUltimoPeso();
        float ultimaAltura = usuario.getAltura();
        if(orientacion == 1) {
            if (ultimoPeso > 0) { //SOLO EN VERTICAL
                tvUltimoPeso.setVisibility(View.VISIBLE);
                tvUltimoPeso.setText("Último peso: " + String.format("%.1f", ultimoPeso) + " Kg");
            }
            else {
                tvUltimoPeso.setVisibility(View.INVISIBLE);
            }
            tvNombreApe.setText(usuario.getNombre() + " " + usuario.getApellidos());
        }

        //PRESENTAMOS LA ÚLTIMA ALTURA
        if (ultimaAltura > 0) {
            etAltura.setText(String.format("%.1f", ultimaAltura*100));
        }

        //EVENTO BOTÓN CALCULAR
        bCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String peso = etPeso.getText().toString();
                String altura = etAltura.getText().toString();
                float valorPeso;
                float valorAltura;

                if (peso.length() > 0 && altura.length() > 0) {

                    try {
                        valorPeso = Float.parseFloat(peso);
                        valorAltura = Float.parseFloat(altura) / 100;

                        if (valorPeso > 0 && valorPeso < 200 && valorAltura > 0 && valorAltura < 2.50) {

                            //Actualizar la Base de Datos con el peso y la altura
                            usuario.setUltimoPeso(valorPeso);
                            usuario.setAltura(valorAltura);
                            baseDatosUsuarios.actualizarUsuario(usuario);

                            //Ir a la página de resultados
                            Intent intent = new Intent((Activity) MainActivity.this, Resultados.class);
                            intent.putExtra("PESO", peso);
                            intent.putExtra("ALTURA", altura);
                            startActivity(intent);
                        }

                    } catch (NumberFormatException nfe) {
                        tvParametrosIncorrectos.setVisibility(View.VISIBLE);
                    }
                }
            } //Fin onClick
        });//Fin bCalcular

        //EVENTOS ONCLICK PARA BORRAR ENTRADAS ANTERIORES
        etAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) v).setText("");
                tvParametrosIncorrectos.setVisibility(View.INVISIBLE);
            }
        });

        etPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) v).setText("");
                tvParametrosIncorrectos.setVisibility(View.INVISIBLE);
            }
        });

        //EVENTOS DE BOTONES QUE SOLO SALEN EN VERTICAL

        //BOTON CERRAR SESIÓN
        if(orientacion == 1) {
            bCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    f.cerrarSesion(MainActivity.this,preferences);
                }
            });

        //BOTON DARSE DE BAJA
            bBaja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    baseDatosUsuarios.bajaUsuario(idUsuario);
                    f.cerrarSesion(MainActivity.this,preferences);
                }
            });

        //BOTON SALIR
            bSalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    f.confirmarSalir(MainActivity.this);

                }
            });
        }
    }
}
