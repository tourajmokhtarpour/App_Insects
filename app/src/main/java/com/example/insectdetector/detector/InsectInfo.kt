package com.example.insectdetector.detector

data class InsectInfo(
    val name: String,
    val scientificName: String,
    val family: String,
    val description: String,
    val habitat: String,
    val diet: String,
    val lifecycle: String,
    val isDangerous: Boolean,
    val interestingFacts: String
)