package id.android.workshop.genai.feature.chatbot

import androidx.compose.runtime.toMutableStateList

class ChatBotUiState(
  messages: List<ChatBotMessage> = emptyList()
) {

  private val mMessages: MutableList<ChatBotMessage> = messages.toMutableStateList()

  val messages: List<ChatBotMessage> = mMessages

  fun addMessage(message: ChatBotMessage) {
    mMessages.add(message)
  }

  fun replaceLastPendingMessage() {
    val lastMessage = mMessages.lastOrNull()
    lastMessage?.let {
      val newMessage = lastMessage.apply {
        isPending = false
      }
      mMessages.removeLast()
      mMessages.add(newMessage)
    }
  }
}