package com.banksampah.customer.ui.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.banksampah.customer.R
import com.banksampah.customer.model.Complaint
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.SingleResponse
import com.banksampah.customer.utils.TokenPreference
import kotlinx.android.synthetic.main.fragment_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceFragment : Fragment() {

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = TokenPreference.getInstance(requireContext()).getToken().toString()
        btn_complain.setOnClickListener {
            validateForm()
        }
    }

    private fun validateForm() {
        val complaint = edt_complaint.text.toString().trim()

        if (complaint.isEmpty()) {
            showMessage("Text Keluhan Anda Kosong")
            return
        }
        complain(complaint)
    }

    private fun complain(complaint: String) {
        showLoading(true)
        ApiClient.instances.complain("Bearer $token", complaint).enqueue(object : Callback<SingleResponse<Complaint>> {
            override fun onResponse(call: Call<SingleResponse<Complaint>>, response: Response<SingleResponse<Complaint>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    showMessage("Berhasil Mengirimkan Keluhan")
                    edt_complaint.text.clear()
                } else {
                    showMessage(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<SingleResponse<Complaint>>, t: Throwable) {
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

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}