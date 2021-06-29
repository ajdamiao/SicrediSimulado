package com.example.sicredisimulado.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.sicredisimulado.R
import com.example.sicredisimulado.databinding.RviewEventsListBinding
import com.example.sicredisimulado.model.Events
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventsAdapter(private val events: ArrayList<Events>) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    inner class EventsViewHolder(val binding: RviewEventsListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = RviewEventsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        with(holder)
        {
            with(events[position]) {

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val netDate = Date(date.toLong() * 1000)
                val dateFinal = sdf.format(netDate).toString()

                binding.eventName.text = title
                binding.txtDate.text = dateFinal
                binding.txtPrice.text = "$price reais"

                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_image_not_found)
                    .into(binding.eventImage)

                holder.binding.rviewItem.setOnClickListener {

                    val bundle = Bundle()
                    bundle.putString("eventName", title)
                    bundle.putString("date", dateFinal)
                    bundle.putString("description", description)
                    bundle.putString("price", price.toString())
                    bundle.putString("image", image)
                    bundle.putString("eventId", id)
                    bundle.putDouble("lat", latitude)
                    bundle.putDouble("long", longitude)

                    Navigation.findNavController(itemView).navigate(R.id.eventDetailFragment, bundle)
                }
            }
        }
    }

    override fun getItemCount() = events.size
}