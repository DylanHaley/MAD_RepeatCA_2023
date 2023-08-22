package ie.setu.mad_ca_repeat_recipe_assignment.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.mad_ca_repeat_recipe_assignment.R
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.ActivityRecipeBinding
import ie.setu.mad_ca_repeat_recipe_assignment.helpers.showImagePicker
import ie.setu.mad_ca_repeat_recipe_assignment.main.MainApp
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeModel
import timber.log.Timber.i
import ie.setu.mad_ca_repeat_recipe_assignment.models.Location

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    var recipe = RecipeModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edit = false

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Recipe Activity started...")

        // Check if in edit mode and populate fields accordingly
        if (intent.hasExtra("recipe_edit")) {
            edit = true
            recipe = intent.extras?.getParcelable("recipe_edit")!!
            // Created separate function to handle the population of the fields in the recipe
            populateFieldsForEdit()
        }

        // Set click listener for Add Recipe button
        binding.btnAddRecipe.setOnClickListener {
            // Created separate function to handle data collection
            collectRecipeData()
            // Validate input and save recipe if input is valid
            if (isInputValid()) {
                saveRecipe()
            } else {
                showValidationSnackbar()
            }
        }

        // Set click listener for Add Image button
        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        // Set click listener for Add Location button
        binding.btnAddLocation.setOnClickListener {
            launchMapActivity()
        }

        // Set up the favorite button click listener
        binding.toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
            recipe.isFavourite = isChecked
            app.recipes.update(recipe) // Update the recipe's isFavourite status
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    // Populate fields for editing existing recipe
    private fun populateFieldsForEdit() {
        // Set UI components with existing recipe details
        binding.recipeTitle.setText(recipe.title)
        binding.recipeDescription.setText(recipe.description)
        binding.recipeDetails.setText(recipe.details)
        binding.recipeRating.rating = recipe.rating
        binding.btnAddRecipe.setText(R.string.save_recipe)
        binding.toggleFavorite.isChecked = recipe.isFavourite

        // Load recipe image using Picasso
        if (recipe.image != Uri.EMPTY) {
            binding.btnAddImage.setText(R.string.change_recipe_image)
            Picasso.get()
                .load(recipe.image)
                .into(binding.recipeImage)
        }
    }

    // Collect input data from views
    private fun collectRecipeData() {
        recipe.title = binding.recipeTitle.text.toString()
        recipe.description = binding.recipeDescription.text.toString()
        recipe.details = binding.recipeDetails.text.toString()
        recipe.rating = binding.recipeRating.rating
        recipe.isFavourite = binding.toggleFavorite.isChecked
    }

    // Validate input data
    private fun isInputValid(): Boolean {
        // Check if all required fields are filled and the recipe is marked as favorite
        return !recipe.title.isEmpty() &&
                !recipe.description.isEmpty() &&
                !recipe.details.isEmpty() &&
                !recipe.rating.isNaN()
//                recipe.isFavourite
    }

    // Save recipe to database
    private fun saveRecipe() {
        collectRecipeData()
        if (edit) {
            app.recipes.update(recipe)
        } else {
            app.recipes.create(recipe)
        }
        i("Save Button Pressed: $recipe")
        setResult(RESULT_OK)
        finish()
    }

    // Show validation snackbar
    private fun showValidationSnackbar() {
        Snackbar.make(
            binding.btnAddRecipe,
            R.string.enter_all_recipe_details,
            Snackbar.LENGTH_LONG
        ).show()
    }

    // Set up menu options
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.recipes.delete(recipe)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Register callback for image picker (split function from placemark into 2 separate functions)
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            handleImagePickerResult(result.data!!)
                        }
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    // Separate function to handle image picker result
    private fun handleImagePickerResult(data: Intent) {
        val selectedImage = data.data
        if (selectedImage != null) {
            i("Got Result $selectedImage")
            recipe.image = selectedImage
            Picasso.get()
                .load(recipe.image)
                .into(binding.recipeImage)
            binding.btnAddImage.setText(R.string.change_recipe_image)
        }
    }

    // Split map function fun from placemark into 2 separate functions same as imager picker
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            handleMapResult(result.data!!)
                        }
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    // Handle result from map activity
    private fun handleMapResult(data: Intent) {
        val location = data.extras?.getParcelable<Location>("location")!!
        i("Got Location $location")
        recipe.latitude = location.lat
        recipe.longitude = location.lng
    }

    // Launch map activity
    private fun launchMapActivity() {
        val location = Location(53.349805, -6.26031, 8f)
        val launcherIntent = Intent(this, MapActivity::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
}
