package ie.setu.mad_ca_repeat_recipe_assignment.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mad_ca_repeat_recipe_assignment.R
import ie.setu.mad_ca_repeat_recipe_assignment.adapters.RecipeListener
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.ActivityRecipeListBinding
import ie.setu.mad_ca_repeat_recipe_assignment.main.MainApp
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeModel
import ie.setu.mad_ca_repeat_recipe_assignment.adapters.RecipeAdapter

class RecipeListActivity : AppCompatActivity(), RecipeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRecipeListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var searchView: SearchView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadRecipe()
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.recipeSearch)
        searchView = searchItem.actionView as SearchView

        // Set up a listener for query text changes // referenced from https://www.tutorialspoint.com/how-to-implement-android-searchview-with-example
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearch(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
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

    override fun onRecipeClick(recipe: RecipeModel) {
        // Launch RecipeActivity to edit the selected recipe
        val launcherIntent = Intent(this, RecipeActivity::class.java)
        launcherIntent.putExtra("recipe_edit", recipe)
        refreshIntentLauncher.launch(launcherIntent)
    }

    // Register the refresh callback for handling startActivityForResult
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadRecipe() }
    }

    // Load recipes from the database and display them
    private fun loadRecipe() {
        val recipes = app.recipes.findAll()
        adapter = RecipeAdapter(recipes, this)
        binding.recyclerView.adapter = adapter
    }

    //Handle search
    private fun handleSearch(query: String?) {
        val filteredRecipes = app.recipes.findAll().filter { recipe ->
            recipe.title.contains(query.orEmpty(), true) // Case-insensitive search
        }
        adapter.updateData(filteredRecipes)
    }
}
