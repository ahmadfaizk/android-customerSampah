package com.banksampah.customer.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.banksampah.customer.R
import com.banksampah.customer.model.History
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.MultiResponse
import com.banksampah.customer.utils.TokenPreference
import kotlinx.android.synthetic.main.fragment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = TokenPreference.getInstance(requireContext()).getToken().toString()
        adapter = HistoryAdapter()
        rv_history.layoutManager = LinearLayoutManager(context)
        rv_history.adapter = adapter

        requestData()
    }

    private fun requestData() {
        showLoading(true)
        ApiClient.instances.getHistory("Bearer $token").enqueue(object : Callback<MultiResponse<History>> {
            override fun onResponse(call: Call<MultiResponse<History>>, response: Response<MultiResponse<History>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    val data = response.body()?.data
                    data?.let { adapter.setData(it) }
                } else {
                    showMessage(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<MultiResponse<History>>, t: Throwable) {
                showLoading(false)
                showMessage(t.message.toString())
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar?.visibility = View.VISIBLE
            rv_history?.visibility = View.GONE
        } else {
            progress_bar?.visibility = View.GONE
            rv_history?.visibility = View.VISIBLE
        }
    }
}