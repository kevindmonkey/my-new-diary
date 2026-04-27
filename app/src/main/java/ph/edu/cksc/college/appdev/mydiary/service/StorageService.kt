package ph.edu.cksc.college.appdev.mydiary.service

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.screens.Entry
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class NewEntry @OptIn(ExperimentalTime::class) constructor(
    val user_id: String,
    val created_at: Instant,
    val title: String,
    val content: String,
    val mood: Int,
    val star: Int,
)

class StorageService(val supabase: SupabaseClient) {

    @OptIn(ExperimentalTime::class)
    fun getFilteredEntries(filter: String): Flow<List<DiaryEntry>> {
        return flow {
            val items = supabase.from("entries")
                .select() {
                    filter {
                        or(filter = {
                            ilike("title", "%$filter%")
                            ilike("content", "%$filter%")
                        }
                        )
                    }
                    order(column = "created_at", order = Order.DESCENDING)
                    limit(20)
                }.decodeList<Entry>()
            val list: MutableList<DiaryEntry> = ArrayList()
            for (entry in items) {
                val item = DiaryEntry(
                    entry.id,
                    entry.mood,
                    entry.star,
                    entry.title,
                    entry.content,
                    entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
                )
                Log.d("Entry", "${entry.id} => ${entry.title}")
                list.add(item)
            }
            emit(list.map { it })
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getDiaryEntry(diaryEntryId: String): DiaryEntry? {
        try {
            val entry = supabase.from("entries").select() {
                filter {
                    eq("id", diaryEntryId)
                }
            }.decodeSingle<Entry>()
            val item = DiaryEntry(
                entry.id,
                entry.mood,
                entry.star,
                entry.title,
                entry.content,
                entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
            )
            Log.d("Entry", "${entry.id} => ${entry.title}")
            return item
        } catch (e: Exception) {
            return null
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun save(diaryEntry: DiaryEntry): String {
        val serialEntry = NewEntry(
            user_id = userSession?.user?.id ?: "",
            created_at = Instant.parse(diaryEntry.dateTime + "z"),
            title = diaryEntry.title,
            content =  diaryEntry.content,
            mood = diaryEntry.mood,
            star = diaryEntry.star
        )
        val result = supabase.from("entries").insert(serialEntry)//.decodeSingle<Entry>()
        Log.d("Result", result.data)
        return ""//result.id
    }

    @OptIn(ExperimentalTime::class)
    suspend fun update(diaryEntry: DiaryEntry) {
        val serialEntry = Entry(
            id = diaryEntry.id,
            user_id = userSession?.user?.id ?: "",
            created_at = Instant.parse(diaryEntry.dateTime + "z"),
            title = diaryEntry.title,
            content =  diaryEntry.content,
            mood = diaryEntry.mood,
            star = diaryEntry.star
        )
        val result = supabase.from("entries").update(serialEntry) {
            filter {
                eq("id", diaryEntry.id)
            }
        }
    }

    suspend fun delete(diaryEntryId: String) {
        //firestore.collection(DIARYENTRY_COLLECTION).document(diaryEntryId).delete().await()
    }
}