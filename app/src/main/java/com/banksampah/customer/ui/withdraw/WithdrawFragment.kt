package com.banksampah.customer.ui.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.banksampah.customer.R
import com.banksampah.customer.model.History
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.SingleResponse
import com.banksampah.customer.utils.TokenPreference
import kotlinx.android.synthetic.main.fragment_withdraw.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NumberFormatException

class WithdrawFragment : Fragment() {

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdraw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = TokenPreference.getInstance(requireContext()).getToken().toString()
        btn_send.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val amountString = edt_amount.text.toString()
        if (amountString.isEmpty()) {
            showMessage(getString(R.string.transaction_value_empty))
            return
        }
        try {
            val amount = amountString.toLong()
            withdraw(amount)
        } catch (e: NumberFormatException) {
            showMessage(getString(R.string.transaction_value_invalid))
        }
    }

    private fun withdraw(amount: Long) {
        showLoading(true)
        ApiClient.instances.withdraw("Bearer $token", amount).enqueue(object : Callback<SingleResponse<History>> {
            override fun onResponse(call: Call<SingleResponse<History>>, response: Response<SingleResponse<History>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    showMessage(getString(R.string.withdraw_success))
                    edt_amount.text.clear()
                } else {
                    showMessage(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<SingleResponse<History>>, t: Throwable) {
                showLoading(false)
                showMessage(t.message.toString())
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state)
            progress_bar?.visibility = View.VISIBLE
        else
            progress_bar?.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}