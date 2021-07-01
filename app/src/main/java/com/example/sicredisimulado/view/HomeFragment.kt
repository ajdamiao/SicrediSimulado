package com.example.sicredisimulado.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sicredisimulado.R
import com.example.sicredisimulado.databinding.FragmentHomeBinding
import com.example.sicredisimulado.model.Events
import com.example.sicredisimulado.view.adapter.EventsAdapter
import com.example.sicredisimulado.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel = HomeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        homeViewModel.getEvents()
        eventsResponse()
    }

    private fun eventsResponse() {
        homeViewModel.eventsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ArrayList<Events> -> setupRecyclerView(response)
            }
        })
    }

    private fun setupRecyclerView(events: ArrayList<Events>) {
        binding.rviewMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rviewMain.adapter = EventsAdapter(events)
    }
}