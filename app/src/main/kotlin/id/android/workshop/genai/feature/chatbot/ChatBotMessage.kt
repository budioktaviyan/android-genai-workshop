package id.android.workshop.genai.feature.chatbot

import id.android.workshop.genai.feature.chatbot.Participant.USER
import java.util.UUID

enum class Participant {
  USER,
  MODEL,
  ERROR
}

data class ChatBotMessage(
  val id: String = UUID.randomUUID().toString(),
  var text: String = "",
  val participant: Participant = USER,
  var isPending: Boolean = false
)