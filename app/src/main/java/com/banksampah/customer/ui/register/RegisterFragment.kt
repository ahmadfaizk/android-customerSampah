package com.banksampah.customer.ui.register

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
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_register.setOnClickListener {
            validateForm()
        }
    }

    private fun validateForm() {
        val name = edt_full_name.text.toString()
        val phoneNumber = edt_phone_number.text.toString()

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            showMessage("Pastikan Anda Mengisi Semua Datanya")
        }

        register(name, phoneNumber)
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun register(name: String, phoneNumber: String) {
        showLoading(true)
        ApiClient.instances.register(name, phoneNumber).enqueue(object : Callback<SingleResponse<Token>> {
            override fun onResponse(call: Call<SingleResponse<Token>>, response: Response<SingleResponse<Token>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    showMessage("Anda Berhasil Mendaftar\nSilahkan Tunggu SMS Balasan dari Operator Kami.")
                    view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
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