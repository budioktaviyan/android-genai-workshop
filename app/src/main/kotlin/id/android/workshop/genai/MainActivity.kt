package id.android.workshop.genai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import id.android.workshop.genai.feature.chatbot.ChatBotViewModel
import id.android.workshop.genai.ui.screen.ChatList
import id.android.workshop.genai.ui.screen.MessageInput
import id.android.workshop.genai.ui.theme.GenAIWorkshopTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      GenAIWorkshopTheme {
        val chatBotViewModel: ChatBotViewModel = viewModel(
          factory = ViewModelFactory
        )

        val chatBotUiState by chatBotViewModel.uiState.collectAsState()
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
          bottomBar = {
            MessageInput(
              onSendMessage = { inputText ->
                chatBotViewModel.sendMessage(inputText)
              },
              resetScroll = {
                coroutineScope.launch {
                  listState.scrollToItem(0)
                }
              }
            )
          }
        ) { paddingValues ->
          Column(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
          ) {
            ChatList(
              chatBotMessages = chatBotUiState.messages,
              listState = listState
            )
          }
        }
      }
    }
  }
}