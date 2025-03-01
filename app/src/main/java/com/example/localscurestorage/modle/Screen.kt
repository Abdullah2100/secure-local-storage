package com.example.localscurestorage.modle
import kotlinx.serialization.Serializable
sealed class Screen() {
    @Serializable
    data object CreatePage:Screen()

    @Serializable
    data object home:Screen()


    @Serializable
    data object homeGraph:Screen()
}