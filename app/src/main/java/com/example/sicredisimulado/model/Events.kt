package com.example.sicredisimulado.model

data class Events(
    val cupons: List<Any>,
    val date: Int,
    val description: String,
    val id: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val people: List<Any>,
    val price: Float,
    val title: String
)