package id.android.workshop.genai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import id.android.workshop.genai.feature.textsummary.TextSummaryViewModel

@Suppress("UNCHECKED_CAST")
val ViewModelFactory = object : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(
    modelClass: Class<T>,
    extras: CreationExtras
  ): T {
    val config = generationConfig {
      temperature = 0.9f
    }

    return with(modelClass) {
      when {
        isAssignableFrom(TextSummaryViewModel::class.java) -> {
          val model = GenerativeModel(
            modelName = "gemini-1.0-pro",
            apiKey = BuildConfig.gmnApiKey,
            generationConfig = config
          )
          TextSummaryViewModel(generativeModel = model)
        }
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
      }
    } as T
  }
}