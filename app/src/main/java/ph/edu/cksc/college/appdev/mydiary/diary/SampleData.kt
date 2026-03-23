import android.os.Build
import androidx.annotation.RequiresApi
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import java.time.LocalDateTime

object SampleDiaryEntries {
    // Sample conversation data
    @RequiresApi(Build.VERSION_CODES.O)
    val entries = mutableListOf(
        DiaryEntry(
            "1", 0, 1,
            "Lexi",
            "Test...Test...Test...",
            LocalDateTime.of(2024, 1, 1, 7, 30).toString()
        ),
        DiaryEntry(
            "2", 5, 2,
            "Lexi",
            """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(),
            LocalDateTime.of(2024, 1, 2, 8, 30).toString()

        ),
        DiaryEntry(
            "3", 1, 3,
            "Lexi",
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim(),
            LocalDateTime.of(2024, 1, 3, 9, 50).toString()

        ),
        DiaryEntry(
            "4", 2, 4,
            "Lex Luthor",
            "Searching for alternatives to XML layouts...",
            LocalDateTime.of(2024, 1, 5, 7, 30).toString()
        ),
        DiaryEntry(
            "5", 3, 5,
            "Lexi",
            """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim(),
            LocalDateTime.of(2024, 1, 7, 17, 30).toString()

        ),
        DiaryEntry(
            "6", 1, 1,
            "Lexi",
            "It's available from API 21+ :)",
            LocalDateTime.of(2024, 1, 8, 7, 30).toString()

        ),
        DiaryEntry(
            "7", 2, 2,
            "Lexi",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?",
            LocalDateTime.of(2024, 1, 9, 7, 30).toString()
        ),
        DiaryEntry(
            "8", 6, 3,
            "Lexi",
            "Android Studio next version's name is Arctic Fox",
            LocalDateTime.of(2024, 1, 10, 7, 30).toString()
        ),
        DiaryEntry(
            "8", 7, 4,
            "Lexi",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^",
            LocalDateTime.of(2024, 2, 1, 7, 30).toString()
        ),
        DiaryEntry(
            "9", 4, 5,
            "Lexi",
            "I didn't know you can now run the emulator directly from Android Studio",
            LocalDateTime.of(2024, 1, 2, 7, 30).toString()
        ),
        DiaryEntry(
            "7", 3, 1,
            "Lexi",
            "Compose Previews are great to check quickly how a composable layout looks like",
            LocalDateTime.of(2024, 1, 3, 7, 30).toString()
        ),
        DiaryEntry(
            "3", 2, 2,
            "Lexi",
            "Previews are also interactive after enabling the experimental setting",
            LocalDateTime.of(2024, 1, 4, 7, 30).toString()
        ),
        DiaryEntry(
            "1", 1, 3,
            "Lexi",
            "Have you tried writing build.gradle with KTS?",
            LocalDateTime.of(2024, 1, 5, 7, 30).toString()
        ),
    )
}