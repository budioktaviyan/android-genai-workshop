package id.android.workshop.genai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.ai.client.generativeai.GenerativeModel
import id.android.workshop.genai.ui.theme.GenAIWorkshopTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

  private val generativeModel = GenerativeModel(
    modelName = "gemini-1.0-pro",
    apiKey = BuildConfig.gmnApiKey
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      GenAIWorkshopTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
          ) {
            Greeting(
              name = "Budi",
              modifier = Modifier
                .padding(innerPadding)
                .align(Alignment.CenterHorizontally)
            )
          }
        }
      }
    }
    runBlocking {
      val prompt = "Write a story about a magic backpack"
      val response = generativeModel.generateContent(prompt)
      Log.d("GENAI", "${response.text}")
    }
  }
}

@Composable
fun Greeting(
  name: String,
  modifier: Modifier = Modifier
) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  GenAIWorkshopTheme {
    Greeting(name = "Budi")
  }
}