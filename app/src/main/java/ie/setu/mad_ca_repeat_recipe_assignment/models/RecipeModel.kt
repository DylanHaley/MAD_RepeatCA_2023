package ie.setu.mad_ca_repeat_recipe_assignment.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel (var id: Long = 0,
                        var title: String = "",
                        var description: String = "",
                        var details: String = "",
                        var image: Uri = Uri.EMPTY,
                        var rating: Float = 0f,
                        var latitude: Double = 0.0,
                        var longitude: Double = 0.0,
                        var isFavourite: Boolean = false) : Parcelable

@Parcelize
data class Location (var lat: Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f) : Parcelable