package id.android.workshop.genai.feature.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import id.android.workshop.genai.feature.chatbot.Participant.ERROR
import id.android.workshop.genai.feature.chatbot.Participant.MODEL
import id.android.workshop.genai.feature.chatbot.Participant.USER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatBotViewModel(
  private val generativeModel: GenerativeModel
) : ViewModel() {

  private val chat = generativeModel.startChat(
    history = listOf(
      content(role = "model") {
        text("Hai! Ada yang bisa Aku bantu?")
      }
    )
  )

  private val mUiState: MutableStateFlow<ChatBotUiState> =
    MutableStateFlow(
      ChatBotUiState(
        chat.history.map { content ->
          ChatBotMessage(
            text = content.parts.first().asTextOrNull() ?: "",
            participant = if (content.role.equals("user")) USER else MODEL,
            isPending = false
          )
        }
      )
    )

  val uiState: StateFlow<ChatBotUiState> =
    mUiState.asStateFlow()

  fun sendMessage(userMessage: String) {
    mUiState.value.addMessage(
      ChatBotMessage(
        text = userMessage,
        participant = USER,
        isPending = true
      )
    )

    viewModelScope.launch {
      try {
        val response = chat.sendMessage(userMessage)
        mUiState.value.replaceLastPendingMessage()

        response.text?.let { modelResponse ->
          mUiState.value.addMessage(
            ChatBotMessage(
              text = modelResponse,
              participant = MODEL,
              isPending = false
            )
          )
        }
      } catch (error: Exception) {
        mUiState.value.replaceLastPendingMessage()
        mUiState.value.addMessage(
          ChatBotMessage(
            text = error.localizedMessage ?: "Unknown Error!",
            participant = ERROR
          )
        )
      }
    }
  }
}