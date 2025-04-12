package com.example.evaluable_pmdm_juanjoseavila.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment:Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        binding.btnLogin.setOnClickListener{
            if(binding.editMail.text.isNotEmpty() && binding.editPass.text.isNotEmpty()){
        auth.signInWithEmailAndPassword(binding.editMail.text.toString(), binding.editPass.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
            findNavController().navigate(R.id.action_loginFragment_to_leagueFragment)

                }else
                {
                    Snackbar.make(binding.root, "Error en el login", Snackbar.LENGTH_LONG).show()
                }
            }


            }else
            {
                Snackbar.make(binding.root, "Faltan datos en el login", Snackbar.LENGTH_LONG).show()

            }
        }

        binding.btntoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }


}