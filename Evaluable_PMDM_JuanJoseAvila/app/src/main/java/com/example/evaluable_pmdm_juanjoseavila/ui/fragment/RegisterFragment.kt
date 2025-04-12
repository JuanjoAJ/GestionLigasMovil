package com.example.evaluable_pmdm_juanjoseavila.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment:Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://ligasapp-d6424-default-rtdb.europe-west1.firebasedatabase.app/")
    }


    override fun onStart() {
        super.onStart()
        binding.btnRegister.setOnClickListener {
            if(binding.editMail.text.isNotEmpty() && binding.editPass.text.isNotEmpty())
            {
                database.reference.child("app").child("name")
                auth.createUserWithEmailAndPassword(binding.editMail.text.toString(), binding.editPass.text.toString())
                    .addOnCompleteListener { if(it.isSuccessful){
                        saveUser(binding.editMail.text.toString())
                        findNavController().navigate(R.id.action_registerFragment_to_leagueFragment)

                    }else {
                        Snackbar.make(binding.root, "Error en el registro", Snackbar.LENGTH_LONG).show()
                    }
                    }
            }else{
                Snackbar.make(binding.root, "Faltan datos", Snackbar.LENGTH_LONG).show()

            }

        }
    }

    private fun saveUser(mail:String) {
        database.reference.child("users").child(auth.currentUser!!.uid).child("mail").setValue(mail)
    }

}