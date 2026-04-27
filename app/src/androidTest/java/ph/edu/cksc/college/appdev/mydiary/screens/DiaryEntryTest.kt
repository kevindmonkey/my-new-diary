package ph.edu.cksc.college.appdev.mydiary.screens


import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryComponent
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import java.time.LocalDateTime

class DiaryEntryTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Before
    fun setUp() {
        // Start the app
        composeTestRule.setContent {
            DiaryEntryComponent(viewModel = object: DiaryEntryViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var diaryEntry = mutableStateOf(DiaryEntry())
                init {
                    diaryEntry.value = DiaryEntry(
                        "0",
                        0,
                        1,
                        "Lexi",
                        "Test...Test...Test...",
                        LocalDateTime.of(2026, 1, 1, 7, 30).toString()
                    )
                }
                override var modified: Boolean = true

                override fun onTitleChange(newValue: String) {
                    diaryEntry.value = diaryEntry.value.copy(title = newValue)
                }
                override fun onContentChange(newValue: String) {
                    diaryEntry.value = diaryEntry.value.copy(content = newValue)
                }
                override fun onMoodChange(newValue: Int) {
                    diaryEntry.value = diaryEntry.value.copy(mood = newValue)
                }
                override fun onStarChange(newValue: Int) {
                    diaryEntry.value = diaryEntry.value.copy(star = newValue)
                }
                override fun onDateTimeChange(newValue: LocalDateTime) {
                    val newDueDate = newValue.toString()
                    diaryEntry.value = diaryEntry.value.copy(dateTime = newDueDate)
                }
                override fun onDoneClick(popUpScreen: () -> Unit) {

                }
            }, test = false)
        }
    }

    @Test
    fun testMoodDisplayed() {
        composeTestRule.onNodeWithText("Mood").assertIsDisplayed()
        composeTestRule.onNodeWithText("Happy").assertIsDisplayed()
    }

    @Test
    fun testStarDisplayed() {
        composeTestRule.onNodeWithText("Star").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun testTitleDisplayed() {
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lexi").assertIsDisplayed()
    }

    @Test
    fun testContentDisplayed() {
        composeTestRule.onNodeWithText("Content").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test...Test...Test...").assertIsDisplayed()
    }

    @Test
    fun testDateTimeDisplayed() {
        composeTestRule.onNodeWithText("Jan 1, 26\n7:30 AM").assertIsDisplayed()
    }

    @Test
    fun testTitleCanBeEdited() {
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title", useUnmergedTree = true).onParent()
            .performTextReplacement("New title")
        //composeTestRule.waitUntilDoesNotExist(hasText("Lexi"))
        composeTestRule.onNodeWithText("New title").assertIsDisplayed()
    }

    @Test
    fun testContentCanBeEdited() {
        composeTestRule.onNodeWithText("Content").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content", useUnmergedTree = true).onParent()
            .performTextReplacement("New content")
        //composeTestRule.waitUntilDoesNotExist(hasText("Test...Test...Test..."))
        composeTestRule.onNodeWithText("New content").assertIsDisplayed()
    }

    @Test
    fun testMoodCanBeChanged() {
        composeTestRule.onNodeWithText("Mood").performClick()
        composeTestRule.onNodeWithText("Angry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Love").performClick()
        composeTestRule.onNodeWithText("Love").assertIsDisplayed()
    }
}