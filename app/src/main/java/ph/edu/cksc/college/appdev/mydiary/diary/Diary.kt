package ph.edu.cksc.college.appdev.mydiary.diary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class DiaryEntry(
    val id: String = "",
    val mood: Int = 0,
    val star: Int = 1,
    val title: String = "",
    val content: String = "",
    val dateTime: String = LocalDateTime.now().toString(),
    val photoUrls: List<String> = emptyList(),
    val voiceMemoUrls: List<String> = emptyList()
)

data class Mood(
    val mood: String,
    val icon: ImageVector,
    val color: Color
)

val moodList = listOf(
    Mood("Happy", Icons.Filled.SentimentSatisfied, Color(0xffd4a302)),
    Mood("Excited", Icons.Filled.SentimentVerySatisfied, Color(0xff109900)),
    Mood("Love", Icons.Filled.Favorite, Color(0xffee0000)),
    Mood("Hungry", Icons.Filled.RamenDining, Color(0xfffc7b03)),
    Mood("Angry", Icons.Filled.SentimentDissatisfied, Color(0xffff0000)),
    Mood("Furious", Icons.Filled.SentimentVeryDissatisfied, Color(0xffee00ee)),
    Mood("Sleepy", Icons.Filled.Hotel, Color(0xff0468bf)),
    Mood("Sad", Icons.Filled.MoodBad, Color(0xff5a5ae8)),
    Mood("Gloomy", Icons.Filled.Cloud, Color(0xff888888)),
    Mood("Blocked", Icons.Filled.Block, Color(0xffdd0000))
)

val starList = listOf(1, 2, 3, 4, 5)