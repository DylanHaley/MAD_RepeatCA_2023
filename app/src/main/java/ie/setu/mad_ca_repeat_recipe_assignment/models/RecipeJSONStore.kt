package ie.setu.mad_ca_repeat_recipe_assignment.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import timber.log.Timber
import com.google.gson.reflect.TypeToken
import ie.setu.mad_ca_repeat_recipe_assignment.helpers.exists
import ie.setu.mad_ca_repeat_recipe_assignment.helpers.read
import ie.setu.mad_ca_repeat_recipe_assignment.helpers.write
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE = "recipes.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<RecipeModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class RecipeJSONStore(private val context: Context) : RecipeStore {

    var recipes = mutableListOf<RecipeModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<RecipeModel> {
        logAll()
        return recipes
    }

    override fun create(recipe: RecipeModel) {
        recipe.id = generateRandomId()
        recipes.add(recipe)
        serialize()
    }


    override fun update(recipe: RecipeModel) {
        val recipesList = findAll() as ArrayList<RecipeModel>
        var foundRecipe: RecipeModel? = recipesList.find { v -> v.id == recipe.id }
        if (foundRecipe != null) {
            foundRecipe.title = recipe.title
            foundRecipe.description = recipe.description
            foundRecipe.image = recipe.image
            foundRecipe.rating = recipe.rating
            foundRecipe.longitude = recipe.longitude
            foundRecipe.latitude = recipe.latitude
            foundRecipe.isFavourite = recipe.isFavourite
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(recipes, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        recipes = gsonBuilder.fromJson(jsonString, listType)
    }

    override fun delete(recipe: RecipeModel) {
        recipes.remove(recipe)
        serialize()
    }

    private fun logAll() {
        recipes.forEach { Timber.i("$it") }
    }

}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}