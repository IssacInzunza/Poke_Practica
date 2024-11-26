package com.example.practica11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class FragmentLogin extends Fragment {

    private NavController navGraph;
    private Button botonRegistrarse;
    private Button botonLogin;
    private EditText usernameEditText, passwordEditText;
    private SharedPreferences sharedPreferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentLogin() {
        // Required empty public constructor
    }

    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        usernameEditText = rootView.findViewById(R.id.usernameEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        botonRegistrarse = rootView.findViewById(R.id.createAccountButton);
        botonLogin = rootView.findViewById(R.id.loginButton);


        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);


        navGraph = Navigation.findNavController(getActivity(), R.id.nav_graph);


        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();


                if (validateLogin(enteredUsername, enteredPassword)) {

                    //Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(getActivity(), BienvenidaActivity.class);


                    intent.putExtra("username", enteredUsername);


                    startActivity(intent);


                    getActivity().finish();
                } else {

                    Toast.makeText(getActivity(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });



        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navGraph.navigate(R.id.action_fragmentLogin_to_registroFragment);
            }
        });


        return rootView;
    }

    // Método para validar las credenciales de inicio de sesión
    private boolean validateLogin(String username, String password) {
        // Recuperamos los datos guardados en SharedPreferences
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Comprobamos si el nombre de usuario y la contraseña coinciden
        return username.equals(savedUsername) && password.equals(savedPassword);
    }
}
