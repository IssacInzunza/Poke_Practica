package com.example.practica11;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class RegistroFragment extends Fragment {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private SharedPreferences sharedPreferences;

    public RegistroFragment() {
        // Required empty public constructor
    }

    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registro, container, false);

        // Inicializamos los campos de entrada y el botón
        etUsername = rootView.findViewById(R.id.etUsername);
        etEmail = rootView.findViewById(R.id.etEmail);
        etPassword = rootView.findViewById(R.id.etPassword);
        btnRegister = rootView.findViewById(R.id.btnRegister);

        // Inicializamos SharedPreferences para almacenar los datos del usuario
        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Establecemos el listener del botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos los valores de los campos
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Validamos los campos
                if (validateInput(username, email, password)) {
                    // Guardamos los datos en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    // Mostramos un mensaje de éxito
                    Toast.makeText(getActivity(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

                    // Limpiamos los campos
                    etUsername.setText("");
                    etEmail.setText("");
                    etPassword.setText("");
                }
            }
        });

        return rootView;
    }

    // Método para validar la entrada del usuario
    private boolean validateInput(String username, String email, String password) {
        // Validamos que los campos no estén vacíos
        if (username.isEmpty()) {
            etUsername.setError("El nombre de usuario es obligatorio");
            return false;
        }
        if (email.isEmpty()) {
            etEmail.setError("El correo electrónico es obligatorio");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            return false;
        }

        // Validamos el formato del correo electrónico
        if (!email.contains("@") || !email.contains(".")) {
            etEmail.setError("Por favor ingresa un correo electrónico válido");
            return false;
        }

        return true;
    }
}
