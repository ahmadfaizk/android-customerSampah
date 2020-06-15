package com.banksampah.customer.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.banksampah.customer.R
import com.banksampah.customer.model.Token
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.SingleResponse
import com.banksampah.customer.utils.TokenPreference
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            validateForm()
        }
        btn_register.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validateForm() {
        val phoneNumber = edt_phone_number.text.toString()
        val password = edt_password.text.toString()

        if (phoneNumber.isEmpty() || password.isEmpty()) {
            showMessage("Anda Belum Mengisi Nomor Handphone atau Password")
            return
        }
        login(phoneNumber, password)
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun login(phoneNumber: String, password: String) {
        showLoading(true)
        ApiClient.instances.login(phoneNumber, password).enqueue(object : Callback<SingleResponse<Token>> {
            override fun onResponse(call: Call<SingleResponse<Token>>, response: Response<SingleResponse<Token>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    val token = response.body()?.data
                    TokenPreference.getInstance(requireContext()).saveToken(token?.token)
                    showMessage("Berhasil Login")
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_mainActivity)
                    this@LoginFragment.activity?.finish()
                }
                showMessage(response.body()?.message.toString())
            }

            override fun onFailure(call: Call<SingleResponse<Token>>, t: Throwable) {
                showLoading(false)
                showMessage(t.message.toString())
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.GONE
    }
}