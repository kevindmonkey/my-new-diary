package ph.edu.cksc.college.appdev.mydiary.components

import androidx.compose.runtime.MutableState
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import java.time.LocalDateTime

interface DiaryEntryViewModel {

    var diaryEntry: MutableState<DiaryEntry>

    var modified: Boolean

    fun onTitleChange(newValue: String)

    fun onContentChange(newValue: String)

    fun onMoodChange(newValue: Int)

    fun onStarChange(newValue: Int)

    fun onDateTimeChange(newValue: LocalDateTime)

    fun onDoneClick(popUpScreen: () -> Unit)

}