package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryComponent
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.service.StorageService
import ph.edu.cksc.college.appdev.mydiary.supabase
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme
import java.time.LocalDateTime

/**
 * UI Tests for the Diary Entry screen.
 * Verifies that entry details are displayed correctly and can be edited.
 */
class DiaryEntryTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val storageService = StorageService(supabase)
            MyDiaryTheme {
                DiaryEntryComponent(
                    id = "test-id",
                    viewModel = object : DiaryEntryViewModel {
                        @SuppressLint("UnrememberedMutableState")
                        override var diaryEntry = mutableStateOf(
                            DiaryEntry(
                                id = "test-id",
                                mood = 0, // Happy
                                star = 1,
                                title = "Lexi",
                                content = "Test Content",
                                dateTime = LocalDateTime.of(2026, 1, 1, 7, 30).toString()
                            )
                        )
                        override var modified: Boolean = false

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

                        override fun onLocationChange(newValue: String) {
                            diaryEntry.value = diaryEntry.value.copy(location = newValue)
                        }

                        override fun onDateTimeChange(newValue: LocalDateTime) {
                            diaryEntry.value = diaryEntry.value.copy(dateTime = newValue.toString())
                        }

                        override fun onAddPhoto(url: String) {}
                        override fun onRemovePhoto(url: String) {}
                        override fun onAddVoiceMemo(url: String) {}
                        override fun onRemoveVoiceMemo(url: String) {}
                        override fun onDoneClick(popUpScreen: () -> Unit) {}
                    },
                    storageService = storageService,
                    isEditing = true,
                    onDateClick = {},
                    onCancel = {},
                    onSave = {}
                )
            }
        }
    }

    @Test
    fun testMoodAndStarsDisplayed() {
        // Assert initial mood and star rating chips are shown
        composeTestRule.onNodeWithText("Happy").assertIsDisplayed()
        composeTestRule.onNodeWithText("1 Stars").assertIsDisplayed()
    }

    @Test
    fun testTitleAndContentDisplayed() {
        // Assert title and content text fields show the correct values
        composeTestRule.onNodeWithText("Lexi").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Content").assertIsDisplayed()
    }

    @Test
    fun testDateTimeDisplayed() {
        // Assert date and time are formatted correctly
        // Jan 1, 2026 is a Thursday
        composeTestRule.onNodeWithText("THURSDAY, JANUARY 1, 2026").assertIsDisplayed()
        composeTestRule.onNodeWithText("7:30 AM").assertIsDisplayed()
    }

    @Test
    fun testTitleEditing() {
        // Test replacing text in the title field
        composeTestRule.onNodeWithText("Lexi").performTextReplacement("Updated Title")
        composeTestRule.onNodeWithText("Updated Title").assertIsDisplayed()
    }

    @Test
    fun testContentEditing() {
        // Test replacing text in the content field
        composeTestRule.onNodeWithText("Test Content").performTextReplacement("Updated Content Body")
        composeTestRule.onNodeWithText("Updated Content Body").assertIsDisplayed()
    }

    @Test
    fun testMoodChanging() {
        // Open mood dropdown
        composeTestRule.onNodeWithText("Happy").performClick()
        
        // Select "Angry"
        composeTestRule.onNodeWithText("Angry").performClick()
        
        // Verify UI updated
        composeTestRule.onNodeWithText("Angry").assertIsDisplayed()
    }

    @Test
    fun testStarRatingChanging() {
        // Open stars dropdown
        composeTestRule.onNodeWithText("1 Stars").performClick()
        
        // Select "5 Stars"
        composeTestRule.onNodeWithText("5 Stars").performClick()
        
        // Verify UI updated
        composeTestRule.onNodeWithText("5 Stars").assertIsDisplayed()
    }
}
