package ie.setu.mad_ca_repeat_recipe_assignment.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mad_ca_repeat_recipe_assignment.R
import ie.setu.mad_ca_repeat_recipe_assignment.adapters.RecipeAdapter
import ie.setu.mad_ca_repeat_recipe_assignment.adapters.RecipeListener
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.ActivityFavouriteListBinding
import ie.setu.mad_ca_repeat_recipe_assignment.main.MainApp
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeModel

class FavouriteListActivity : AppCompatActivity(), RecipeListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityFavouriteListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private var favoriteRecipes: MutableList<RecipeModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        setupRecyclerView()
        registerRefreshCallback()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                // Launch RecipeActivity to add a new recipe
                val launcherIntent = Intent(this, RecipeActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }

            R.id.theme_choose-> {
                // Launch ThemeActivity to change theme
                val launcherIntent = Intent(this, ThemeActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }

            R.id.favourite_list-> {
                // Launch ThemeActivity to change theme
                val launcherIntent = Intent(this, FavouriteListActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerRefreshCallback() {
        // Set up the result launcher to refresh the list when returning from RecipeActivity
        refreshIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                loadRecipe() // Reload favorite recipes if needed
            }
        }
    }

    private fun loadRecipe() {
        // Load your list of all recipes from a data source
        val allRecipes = app.recipes.findAll()

        // Filter the list to get only the favorite recipes
        favoriteRecipes = allRecipes.filter { it.isFavourite }.toMutableList()

        // Set the filtered list in the RecyclerView's adapter
        val adapter = RecipeAdapter(favoriteRecipes, this) // Pass the filtered favorite list
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupRecyclerView() {
        // Initialize the adapter and set up the RecyclerView
        val adapter = RecipeAdapter(favoriteRecipes, this) // Pass the filtered favorite list
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Implement the RecipeListener interface methods
    override fun onRecipeClick(recipe: RecipeModel) {
        // Launch RecipeActivity with the selected recipe in edit mode
        val launcherIntent = Intent(this, RecipeActivity::class.java)
        launcherIntent.putExtra("recipe_edit", recipe)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onResume() {
        super.onResume()
        loadRecipe() // Refresh the list of favorite recipes
    }
}
