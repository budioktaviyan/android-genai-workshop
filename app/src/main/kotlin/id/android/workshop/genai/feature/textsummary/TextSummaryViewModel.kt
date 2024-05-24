package id.android.workshop.genai.feature.textsummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import id.android.workshop.genai.feature.textsummary.TextSummary.NORMAL
import id.android.workshop.genai.feature.textsummary.TextSummary.STREAM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class TextSummary {
  NORMAL,
  STREAM
}

class TextSummaryViewModel(
  private val generativeModel: GenerativeModel
) : ViewModel() {

  private val mUiState: MutableStateFlow<TextSummaryUiState> =
    MutableStateFlow(TextSummaryUiState.Initial)

  val uiState: StateFlow<TextSummaryUiState> =
    mUiState.asStateFlow()

  fun summarize(
    inputText: String,
    type: TextSummary
  ) {
    mUiState.value = TextSummaryUiState.Loading

    val prompt = "Tolong bantu untuk memberikan ringkasan teks berikut untuk Saya: $inputText"
    viewModelScope.launch {
      try {
        when (type) {
          NORMAL -> {
            val response = generativeModel.generateContent(prompt)

            response.text?.let { content ->
              mUiState.value = TextSummaryUiState.Success(
                output = content
              )
            }
          }
          STREAM -> {
            var outputContent = ""

            generativeModel.generateContentStream(prompt).collect { response ->
              outputContent = outputContent.plus(response.text)
              mUiState.value = TextSummaryUiState.Success(
                output = outputContent
              )
            }
          }
        }
      } catch (error: Exception) {
        mUiState.value = TextSummaryUiState.Error(
          message = error.localizedMessage ?: "Unknown Error!"
        )
      }
    }
  }
}