package com.banksampah.customer.ui.profile.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.banksampah.customer.R
import com.banksampah.customer.model.User
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.SingleResponse
import com.banksampah.customer.utils.TokenPreference
import kotlinx.android.synthetic.main.fragment_profile_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileUpdateFragment : Fragment() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private var user: User? = null
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = TokenPreference.getInstance(requireContext()).getToken().toString()
        user = arguments?.getParcelable(EXTRA_USER)
        populateData()

        btn_update.setOnClickListener {
            validateForm()
        }
    }

    private fun populateData() {
        edt_full_name.setText(user?.name)
        edt_phone_number.setText(user?.phoneNumber)
        edt_address.setText(user?.address)
    }

    private fun validateForm() {
        val name = edt_full_name.text.toString().trim()
        val phoneNumber = edt_phone_number.text.toString().trim()
        val address = edt_address.text.toString().trim()

        if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            showMessage("Anda Belum Mengisi Semua Datanya")
            return
        }
        update(name, phoneNumber, address)
    }

    private fun update(name: String, phoneNumber: String, address: String) {
        showLoading(true)
        ApiClient.instances.updateUser("Bearer $token", name, phoneNumber, address).enqueue(object : Callback<SingleResponse<User>> {
            override fun onResponse(call: Call<SingleResponse<User>>, response: Response<SingleResponse<User>>) {
                showLoading(false)
                val error = response.body()?.error
                if (error != null && !error) {
                    view?.findNavController()?.navigate(R.id.action_profileUpdateFragment_to_nav_profile)

                    showMessage("Berhasil Mengubah Data Profil")
                } else {
                    showMessage(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<SingleResponse<User>>, t: Throwable) {
                showLoading(false)
                showMessage(t.message.toString())
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}