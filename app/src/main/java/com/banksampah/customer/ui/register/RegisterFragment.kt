package com.banksampah.customer.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.banksampah.customer.R
import com.banksampah.customer.model.Token
import com.banksampah.customer.network.ApiClient
import com.banksampah.customer.network.response.SingleResponse
import com.banksampah.customer.ui.login.LoginFragment
import com.banksampah.customer.utils.PhoneNumberValidator
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
        setContactListener()
    }

    private fun validateForm() {
        val name = edt_full_name.text.toString()
        var phoneNumber = edt_phone_number.text.toString()

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            showMessage("Pastikan Anda Mengisi Semua Datanya")
        }
        if (!PhoneNumberValidator.validate(phoneNumber)) {
            showMessage("Format Nomor Handphone Slah")
            return
        }

        phoneNumber = PhoneNumberValidator.clean(phoneNumber)

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
                } else {
                    showMessage(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<SingleResponse<Token>>, t: Throwable) {
                showLoading(false)
                showMessage(t.message.toString())
            }
        })
    }

    private fun setContactListener() {
        edt_phone_number.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (edt_phone_number.right - edt_phone_number.compoundDrawables[2].bounds.width())) {
                        readContact()
                        return true
                    }
                }
                return false
            }
        })
    }

    @SuppressLint("IntentReset")
    private fun readContact() {
        val contact = Intent(Intent.ACTION_PICK)
        contact.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(contact, LoginFragment.REQUEST_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginFragment.REQUEST_CONTACT && resultCode == Activity.RESULT_OK) {
            getContactFromUri(data?.data)
        }
    }

    private fun getContactFromUri(uri: Uri?) {
        if (uri != null) {
            val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
            cursor?.moveToFirst()
            val name = cursor?.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = cursor?.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            edt_phone_number.setText(cleanNumber(phoneNumber))
            if (edt_full_name.text.isEmpty()) {
                edt_full_name.setText(name)
            }
        }
    }

    private fun cleanNumber(number: String?) : String? {
        return number?.replace("-", "")?.replace(" ", "")
    }

    private fun showLoading(state: Boolean) {
        if (state)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.GONE
    }
}