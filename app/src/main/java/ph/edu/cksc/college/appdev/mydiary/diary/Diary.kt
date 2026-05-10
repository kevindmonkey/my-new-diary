package ph.edu.cksc.college.appdev.mydiary.diary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
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
    val voiceMemoUrls: List<String> = emptyList(),
    val viewCount: Int = 0
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
    Mood("Blocked", Icons.Filled.Block, Color(0xffdd0000)),
    Mood("Party", Icons.Filled.Celebration, Color(0xFFFFD700)),
    Mood("Playful", Icons.Filled.Pets, Color(0xFF8B4513)),
    Mood("Energetic", Icons.Filled.LocalFireDepartment, Color(0xFFFF4500)),
    Mood("Calm", Icons.Filled.Nightlight, Color(0xFF3F51B5)),
    Mood("Busy", Icons.Filled.Work, Color(0xFF607D8B)),
    Mood("Studious", Icons.Filled.School, Color(0xFF795548)),
    Mood("Active", Icons.AutoMirrored.Filled.DirectionsRun, Color(0xFF4CAF50)),
    Mood("Musical", Icons.Filled.MusicNote, Color(0xFFE91E63)),
    Mood("Creative", Icons.Filled.Palette, Color(0xFF009688)),
    Mood("Coffee", Icons.Filled.Coffee, Color(0xFF6F4E37)),
    Mood("Chill", Icons.Filled.BeachAccess, Color(0xFF00BCD4)),
    Mood("Ideas", Icons.Filled.Lightbulb, Color(0xFFFFEB3B)),
    Mood("Strong", Icons.Filled.FitnessCenter, Color(0xFF757575)),
    Mood("Techy", Icons.Filled.Devices, Color(0xFF9C27B0)),
    Mood("Explore", Icons.Filled.Explore, Color(0xFF3F51B5)),
    Mood("Lucky", Icons.Filled.Casino, Color(0xFFE91E63)),
    Mood("Movie", Icons.Filled.Movie, Color(0xFF607D8B)),
    Mood("Reading", Icons.Filled.Book, Color(0xFF795548)),
    Mood("Gaming", Icons.Filled.Gamepad, Color(0xFF4CAF50)),
    Mood("Coding", Icons.Filled.Code, Color(0xFF2196F3)),
    Mood("Nature", Icons.Filled.Park, Color(0xFF4CAF50)),
    Mood("Travel", Icons.Filled.Flight, Color(0xFF2196F3)),
    Mood("Foodie", Icons.Filled.Restaurant, Color(0xFFFF9800)),
    Mood("Shopping", Icons.Filled.ShoppingBag, Color(0xFFE91E63)),
    Mood("Healthy", Icons.Filled.SelfImprovement, Color(0xFF00BCD4)),
    Mood("Tired", Icons.Filled.BatteryAlert, Color(0xFFF44336)),
    Mood("Focused", Icons.Filled.FilterCenterFocus, Color(0xFF3F51B5)),
    Mood("Peaceful", Icons.Filled.Spa, Color(0xFF8BC34A)),
    Mood("Social", Icons.Filled.Groups, Color(0xFF9C27B0)),
    Mood("Dreaming", Icons.Filled.AutoAwesome, Color(0xFFFFC107))
)

val starList = listOf(1, 2, 3, 4, 5)
