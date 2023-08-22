package ie.setu.mad_ca_repeat_recipe_assignment.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.CardRecipeBinding
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeModel

interface RecipeListener {
    fun onRecipeClick(recipe: RecipeModel)
}

class RecipeAdapter constructor(
    private var recipes: List<RecipeModel>,
    private val listener: RecipeListener
) :
    RecyclerView.Adapter<RecipeAdapter.MainHolder>() {

    // Create and return a view holder for the recipe item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recipe = recipes[position] // Get the recipe at the current position
        holder.bind(recipe, listener)
    }

    // Update the data and refresh the adapter
    fun updateData(newData: List<RecipeModel>) {
        recipes = newData
        notifyDataSetChanged()
    }

    // Return the number of recipes in the list
    override fun getItemCount(): Int = recipes.size

    // ViewHolder class for a recipe item
    class MainHolder(private val binding: CardRecipeBinding) :
        RecyclerView.ViewHolder(binding.root), OnMapReadyCallback {

        private lateinit var mapView: MapView
        private lateinit var map: GoogleMap
        private lateinit var currentRecipe: RecipeModel // Store the current recipe

        // Bind data to the view holder
        fun bind(recipe: RecipeModel, listener: RecipeListener) {
            currentRecipe = recipe // Set the current recipe
            binding.recipeName.text = recipe.title
            binding.recipeDescription.text = recipe.description
            Picasso.get().load(recipe.image).resize(200, 200).into(binding.recipeImage)
            binding.recipeRating.rating = recipe.rating

            binding.root.setOnClickListener { listener.onRecipeClick(recipe) }

            mapView = binding.recipeMapView
            mapView.onCreate(Bundle())
            mapView.getMapAsync(this)
        }

        // Callback when the map is ready
        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap

            // Ensure the mapView is properly initialized
            mapView.onResume()

            val location = LatLng(currentRecipe.latitude, currentRecipe.longitude)
            map.addMarker(MarkerOptions().position(location).title(currentRecipe.title))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8.0f))
        }
    }

}
