package com.example.sicredisimulado.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.sicredisimulado.MainActivity
import com.example.sicredisimulado.R
import com.example.sicredisimulado.databinding.FragmentEventDetailBinding
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

        setData()
        setupMap()

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
            .placeholder(R.drawable.ic_image_not_found)
            .resize(200, 200)
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
                        name = (popupInputDialogView!!.findViewById<View>(R.id.inputName) as EditText)
                        email = (popupInputDialogView!!.findViewById<View>(R.id.inputEmail) as EditText)

                        if (!name.text.isNullOrBlank() || !email.text.isNullOrBlank())  {
                            val postInfo = CheckInEvent(email.toString(), eventId, name.toString())
                            homeViewModel.postEvent(postInfo)
                            Navigation.findNavController(requireView()).navigate(R.id.detailsFragentToHome)
                        }
                        else {
                            invalidFieldDialog()
                        }
                        hideKeyboard()
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

    private fun setupMap() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap -> mMap = googleMap
            mapReady = true
            updateMap()
        }
    }

    private fun updateMap() {
        val lat = requireArguments().getDouble("lat")
        val long = requireArguments().getDouble("long")

        val marker = LatLng(lat, long)
        mMap!!.addMarker(MarkerOptions().position(marker).title("Position"))
        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    marker.latitude,
                    marker.longitude
                ), 16.0F
            )
        )
    }

    private fun hideKeyboard()
    {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }
}