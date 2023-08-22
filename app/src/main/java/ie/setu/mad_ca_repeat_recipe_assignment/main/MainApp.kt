// Main App from placemark
package ie.setu.mad_ca_repeat_recipe_assignment.main

import android.app.Application
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeJSONStore
import ie.setu.mad_ca_repeat_recipe_assignment.models.RecipeStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var recipes: RecipeStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        recipes = RecipeJSONStore(applicationContext)
        i("Recipes started")
    }
}