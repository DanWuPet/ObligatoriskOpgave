package com.example.obligatoriskopgave

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.obligatoriskopgave.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.log


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.trim().toString()
            if (email.isEmpty()) {
                binding.editTextTextEmailAddress.error = "No email"
                return@setOnClickListener
            }
            val password = binding.editTextTextPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editTextTextPassword.error = "No password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                        Toast.makeText(requireContext(), "Logging In", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_FirstFragment_to_listFragment)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception
                        )
                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }

        binding.buttonSecond.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.trim().toString()
            if (email.isEmpty()) {
                binding.editTextTextEmailAddress.error = "No email"
                return@setOnClickListener
            }
            val password = binding.editTextTextPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editTextTextPassword.error = "No password"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("APPLE", "createUserWithEmail:success")
                        val user = auth.currentUser
                        //updateUI(user)
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("APPLE", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Registration failed: " + task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        //updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?): Any {
        Log.d("APPLE", "$user is logged in.")
        return user.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}