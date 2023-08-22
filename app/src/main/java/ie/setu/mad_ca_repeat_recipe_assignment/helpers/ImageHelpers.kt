// Modified Image Helper from placemark
package ie.setu.mad_ca_repeat_recipe_assignment.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.mad_ca_repeat_recipe_assignment.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    // Create an intent to open the document picker for image files
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    // Create a chooser for the intent and set the title of the chooser dialog using the button_recipe_image
    chooseFile = Intent.createChooser(chooseFile, R.string.button_recipe_image.toString())
    intentLauncher.launch(chooseFile)
}