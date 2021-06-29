package com.example.sicredisimulado.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.sicredisimulado.MainActivity
import com.example.sicredisimulado.R
import com.example.sicredisimulado.databinding.FragmentEventDetailBinding
import com.example.sicredisimulado.model.CheckInEvent
import com.example.sicredisimulado.util.Util
import com.example.sicredisimulado.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class EventDetailFragment : Fragment(R.layout.fragment_event_detail) {
   private lateinit var binding: FragmentEventDetailBinding
    private var popupInputDialogView: View? = null
    private var homeViewModel = HomeViewModel()
    private lateinit var name: EditText
    private lateinit var email: EditText
    private val util = Util()
    private var eventId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventDetailBinding.bind(view)

        setData()

        binding.btnCheckIn.setOnClickListener {
            checkInAlertDialog()
        }

        val main = activity as MainActivity
        main.setToolbarTitle("Detalhes evento")
    }

    private fun setData()
    {
        eventId = arguments?.getString("eventId").toString()
        val title = arguments?.getString("eventName")
        val date = arguments?.getString("date")
        val description = arguments?.getString("description")
        val price = arguments?.getString("price")
        val imageURL = arguments?.getString("image")

        binding.txtEventName.text = title
        binding.txtEventDescription.text = description
        binding.txtPrice.text = String.format(getString(R.string.txt_preco), price)

        Picasso.get()
            .load(imageURL)
            .placeholder(R.drawable.ic_image_not_found)
            .resize(200,200)
            .into(binding.eventImageDetails)
    }

    private fun checkInAlertDialog()
    {
        val layoutInflater = LayoutInflater.from(requireContext())
        popupInputDialogView = layoutInflater.inflate(R.layout.checkin_alert_dialog, null)
        showCheckInDialog()
    }

    private fun showCheckInDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.checkInAlertDialog)
                    .setView(popupInputDialogView)
                    .setPositiveButton("CANCELAR") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
                    .setNegativeButton("CONFIRMAR") { _, _ ->
                        name = (popupInputDialogView!!.findViewById<View>(R.id.inputName) as EditText)
                        email= (popupInputDialogView!!.findViewById<View>(R.id.inputEmail) as EditText)

                        if (!name.text.isNullOrEmpty() || !email.text.isNullOrEmpty())  {
                            val postInfo = CheckInEvent(name.toString(),email.toString(), eventId)
                            homeViewModel.postEvent(postInfo)
                            Navigation.findNavController(requireView()).popBackStack(R.id.eventDetailFragment, true)
                            util.hideKeyboard(requireContext(),requireView())
                        }
                        else {
                            invalidFieldDialog()
                        }
                    }.show()
        }
    }

    private fun invalidFieldDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                    .setTitle("Erro")
                    .setMessage("Todos os campos devem ser preenchidos")
                    .setPositiveButton("OK"){ DialogInterface, _ ->
                        DialogInterface.dismiss()
                    }
        }?.show()
    }
}