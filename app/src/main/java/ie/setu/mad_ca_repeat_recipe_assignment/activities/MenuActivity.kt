package ie.setu.mad_ca_repeat_recipe_assignment.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.ActivityMenuBinding
import ie.setu.mad_ca_repeat_recipe_assignment.main.MainApp

class MenuActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        // Set click listener for the "Recipes" button
        binding.btnRecipes.setOnClickListener {
            // Launch RecipeListActivity
            val intent = Intent(this, RecipeListActivity::class.java)
            startActivity(intent)
        }
        
    }
}
