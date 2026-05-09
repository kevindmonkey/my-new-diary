package ph.edu.cksc.college.appdev.mydiary.service

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.screens.Entry

@Serializable
data class NewEntry(
    val user_id: String,
    val created_at: Instant,
    val title: String,
    val content: String,
    val mood: Int,
    val star: Int
)

class StorageService(val supabase: SupabaseClient) {

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
                    id = entry.id,
                    mood = entry.mood,
                    star = entry.star,
                    title = entry.title,
                    content = entry.content,
                    dateTime = entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
                )
                list.add(item)
            }
            emit(list)
        }
    }

    suspend fun getDiaryEntry(diaryEntryId: String): DiaryEntry? {
        try {
            val entry = supabase.from("entries").select() {
                filter {
                    eq("id", diaryEntryId)
                }
            }.decodeSingle<Entry>()
            return DiaryEntry(
                id = entry.id,
                mood = entry.mood,
                star = entry.star,
                title = entry.title,
                content = entry.content,
                dateTime = entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
            )
        } catch (e: Exception) {
            Log.e("StorageService", "Error getting entry", e)
            return null
        }
    }

    suspend fun save(diaryEntry: DiaryEntry): String {
        val serialEntry = NewEntry(
            user_id = userSession?.user?.id ?: "",
            created_at = Instant.parse(diaryEntry.dateTime + "z"),
            title = diaryEntry.title,
            content =  diaryEntry.content,
            mood = diaryEntry.mood,
            star = diaryEntry.star
        )
        val result = supabase.from("entries").insert(serialEntry)
        Log.d("Result", result.data)
        return ""
    }

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
        supabase.from("entries").update(serialEntry) {
            filter {
                eq("id", diaryEntry.id)
            }
        }
    }

    suspend fun delete(diaryEntryId: String) {
        supabase.from("entries").delete {
            filter {
                eq("id", diaryEntryId)
            }
        }
    }
}
