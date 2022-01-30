package com.dvj.paises;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dvj.paises.databinding.FragmentJuegoBanderasBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JuegoBanderasFragment extends Fragment {

    private final int NUMERO_RESPUESTAS = 3;
    List<PreguntaPais> preguntas = new ArrayList<>();
    List<String> todasLasRespuestas = new ArrayList<>();
    private String respuestaCorrecta;

    FragmentJuegoBanderasBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtenerPreguntasYRespuestas(this.preguntas, this.todasLasRespuestas);
    }

    private void obtenerPreguntasYRespuestas(List<PreguntaPais> preguntas, List<String> todasLasRespuestas) {
        String[] paises = getResources().getStringArray(R.array.paises);
        for(String idPais : paises) {
            int RIdPais = getResources().getIdentifier(idPais, "array", getContext().getPackageName());
            String [] datosPais  = getResources().getStringArray(RIdPais);
            PreguntaPais p = new PreguntaPais(idPais, datosPais[0], datosPais[1]);
            preguntas.add(p);

            todasLasRespuestas.add(datosPais[0]);
        }
        Collections.shuffle(preguntas);
        Collections.shuffle(todasLasRespuestas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJuegoBanderasBinding.inflate(inflater,container,false);

        generarPregunta();

        binding.aceptar.setOnClickListener(view -> {
            int radioId = binding.radioGroup.getCheckedRadioButtonId();
            if(radioId != -1){
                RadioButton radio = getActivity().findViewById(radioId);
                CharSequence respuesta = radio.getText();
                CharSequence mensaje = respuesta.equals(respuestaCorrecta) ? "¡Acertaste!" : "¡Fallaste!";

                Snackbar.make(view, mensaje, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Siguiente", view1 -> {generarPregunta();})
                        .show();

                view.setEnabled(false);

                /**if(respuesta.equals(respuestaCorrecta)){
                    Toast.makeText(getContext(), "Correcta", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Incorrecta", Toast.LENGTH_SHORT).show();
                }**/
                //generarPregunta();
            } else {
                Toast.makeText(getContext(), "Marca una respuesta", Toast.LENGTH_SHORT).show();
            }

        });

        return binding.getRoot();
    }

    private void generarPregunta() {
        if(preguntas.size() > 0) {
            binding.aceptar.setEnabled(true);
            binding.radioGroup.clearCheck();
            PreguntaPais pregunta = preguntas.remove(0);

            binding.bandera.setImageResource(getImageId(pregunta.bandera)); // TODO llamar metodo obtener id bandera
            List<String> respuestas = generarRespuestasPosibles(pregunta);

            for(int i = 0; i<binding.radioGroup.getChildCount();i++){
                RadioButton radio = (RadioButton) binding.radioGroup.getChildAt(i);
                radio.setText(respuestas.get(i));

            }



            binding.respuesta1.setText(respuestas.get(0));
            binding.respuesta2.setText(respuestas.get(1));
            binding.respuesta3.setText(respuestas.get(2));


        }
    }


    private List<String> generarRespuestasPosibles(PreguntaPais pregunta) {
        respuestaCorrecta = pregunta.nombrePais;


        List<String> todasLasRespuestas = new ArrayList<>(this.todasLasRespuestas);
        todasLasRespuestas.remove(respuestaCorrecta);

        List<String> respuestas = new ArrayList<String>();
        respuestas.add(respuestaCorrecta);
        for(int i = 0; i<binding.radioGroup.getChildCount()-1;i++){
            respuestas.add(todasLasRespuestas.remove(new Random().nextInt(todasLasRespuestas.size())));
        }
        Collections.shuffle(respuestas);

        return respuestas;
    }

   /* private presentarPregunta() {
        binding.bandera.setImageResource();
    }*/

    class PreguntaPais {
        private String idPais;
        private String nombrePais;
        private String bandera;
        private List<String> respuestas;

        public PreguntaPais(String idPais, String nombrePais, String bandera) {
            this.idPais = idPais;
            this.nombrePais = nombrePais;
            this.bandera = bandera;
        }
    }

    private int getImageId(String imagePath) {
        String imageName = "bandera_" + imagePath.substring(imagePath.lastIndexOf("/")+1, imagePath.lastIndexOf("."));
        return getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
    }
}