package ie.setu.mad_ca_repeat_recipe_assignment.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ie.setu.mad_ca_repeat_recipe_assignment.R
import ie.setu.mad_ca_repeat_recipe_assignment.databinding.ActivityThemeBinding
import ie.setu.mad_ca_repeat_recipe_assignment.main.MainApp

class ThemeActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        // Set click listener for the theme button
        binding.btnTheme.setOnClickListener { chooseTheme() }
    }

    // Function to display a dialog for choosing the theme
    private fun chooseTheme() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_theme))

        // Array of theme options and initial checked item
        val styles = arrayOf("Light", "Dark")
        val checkedItem = 0

        // Set up single choice items in the dialog
        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
            when (which) {
                0 -> {
                    // Set the app theme to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight() // Apply the selected theme
                    dialog.dismiss() // Close the dialog
                }
                1 -> {
                    // Set the app theme to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight() // Apply the selected theme
                    dialog.dismiss() // Close the dialog
                }
            }
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}
