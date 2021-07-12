package com.example.sicredisimulado.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.sicredisimulado.MainActivity
import com.example.sicredisimulado.R
import com.example.sicredisimulado.databinding.FragmentEventDetailBinding
import com.example.sicredisimulado.exceptions.CustomException
import com.example.sicredisimulado.model.CheckInEvent
import com.example.sicredisimulado.viewmodel.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class EventDetailFragment : Fragment(R.layout.fragment_event_detail) {
   private lateinit var binding: FragmentEventDetailBinding
    private var popupInputDialogView: View? = null
    private var homeViewModel = HomeViewModel()
    private lateinit var name: EditText
    private lateinit var email: EditText
    private var mMap: GoogleMap? = null
    private var mapReady = false
    private var eventId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventDetailBinding.bind(view)

        checkInResponse()
        setupMap()
        setData()

        binding.btnCheckIn.setOnClickListener {
            checkInAlertDialog()
        }

        binding.btnShare.setOnClickListener {
            shareFunction()
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

        binding.txtDate.text = date
        binding.txtEventName.text = title
        binding.txtEventDescription.text = description
        binding.txtPrice.text = String.format(getString(R.string.txt_preco), price)

        Picasso.get()
            .load(imageURL)
            .resize(500, 500)
            .into(binding.eventImageDetails)
    }

    private fun shareFunction()
    {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Your body here"
        val shareSub = "Your subject here"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
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
                        name = (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.inputName) as EditText })
                        email = (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.inputEmail) as EditText })

                        if(email.text.contains("@"))
                        {
                            if (!name.text.isNullOrBlank() || !email.text.isNullOrBlank())  {
                                val postInfo = CheckInEvent(eventId, name.text.toString(), email.text.toString())
                                homeViewModel.postEvent(postInfo)
                            }
                            else {
                                invalidFieldDialog("Todos os campos devem ser preenchidos.")
                            }
                        }
                        else {
                            invalidFieldDialog("É preciso colocar um email valido.")
                        }
                        hideKeyboard()
                    }.show()
        }
    }

    private fun invalidFieldDialog(mensagem: String) {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                    .setTitle("Erro")
                    .setMessage(mensagem)
                    .setPositiveButton("OK"){ DialogInterface, _ ->
                        DialogInterface.dismiss()
                    }
        }?.show()
    }

    private fun setupMap() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap -> mMap = googleMap
            mapReady = true
            updateMap()
        }
    }

    private fun dialogConfirmCheckIn()
    {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                .setTitle("Sucesso")
                .setMessage("Check-in realizado com sucesso.")
                .setNegativeButton("OK"){ DialogInterface, _ ->
                    Navigation.findNavController(requireView())
                        .navigate(R.id.detailsFragentToHome)
                }
        }?.show()
    }

    private fun updateMap() {
        val lat = requireArguments().getDouble("lat")
        val long = requireArguments().getDouble("long")

        val marker = LatLng(lat, long)
        mMap.let {
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        marker.latitude,
                        marker.longitude
                    ), 16.0F
                )
            )
        }
    }

    private fun checkInResponse() {
        homeViewModel.eventPostResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Boolean -> {
                    if (response) dialogConfirmCheckIn()
                    else{ dialogErroCheckIn() }
                }

                is CustomException -> {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao fazer check-in, tente novamente",
                        Toast.LENGTH_LONG
                    ).show()
                    println("entrou response exception")
                }
            }
        })
    }

    private fun dialogErroCheckIn()
    {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                .setTitle("Erro")
                .setMessage("Erro ao fazer Check-in.")
                .setNegativeButton("OK"){ DialogInterface, _ ->
                    DialogInterface.dismiss()
                }
        }?.show()
    }

    private fun hideKeyboard()
    {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }
}