package id.android.workshop.genai.feature.textsummary

sealed interface TextSummaryUiState {

  data object Initial : TextSummaryUiState
  data object Loading : TextSummaryUiState

  data class Success(
    val output: String
  ) : TextSummaryUiState

  data class Error(
    val message: String
  ) : TextSummaryUiState
}