package ph.edu.cksc.college.appdev.mydiary.service

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import java.util.UUID

@Serializable
data class NewEntry(
    val user_id: String,
    val created_at: Instant,
    val title: String,
    val content: String,
    val mood: Int,
    val star: Int,
    val location: String? = "",
    val photo_urls: List<String> = emptyList(),
    val voice_memo_urls: List<String> = emptyList(),
    val view_count: Int = 0
)

@Serializable
data class EntryUpdate(
    val id: String,
    val user_id: String,
    val created_at: Instant,
    val title: String,
    val content: String,
    val mood: Int,
    val star: Int,
    val location: String? = "",
    val photo_urls: List<String> = emptyList(),
    val voice_memo_urls: List<String> = emptyList(),
    val view_count: Int = 0
)

class StorageService(val supabase: SupabaseClient) {

    private val photoBucket by lazy { supabase.storage.from("photos") }
    private val audioBucket by lazy { supabase.storage.from("audio") }

    private fun String.toInstantSafe(): Instant {
        return try {
            Instant.parse(this)
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(this).toInstant(TimeZone.UTC)
            } catch (e2: Exception) {
                kotlinx.datetime.Clock.System.now()
            }
        }
    }

    fun getFilteredEntries(
        filter: String, 
        mood: Int? = null, 
        star: Int? = null,
        ascending: Boolean = false
    ): Flow<List<DiaryEntry>> {
        return flow {
            try {
                val items = supabase.from("entries")
                    .select() {
                        filter {
                            if (filter.isNotBlank()) {
                                or(filter = {
                                    ilike("title", "%$filter%")
                                    ilike("content", "%$filter%")
                                })
                            }
                            if (mood != null) {
                                eq("mood", mood)
                            }
                            if (star != null) {
                                eq("star", star)
                            }
                        }
                        order(column = "created_at", order = if (ascending) Order.ASCENDING else Order.DESCENDING)
                        limit(50)
                    }.decodeList<EntryUpdate>()
                val list = items.map { entry ->
                    DiaryEntry(
                        id = entry.id,
                        mood = entry.mood,
                        star = entry.star,
                        title = entry.title,
                        content = entry.content,
                        location = entry.location ?: "",
                        dateTime = entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                        photoUrls = entry.photo_urls,
                        voiceMemoUrls = entry.voice_memo_urls,
                        viewCount = entry.view_count
                    )
                }
                emit(list)
            } catch (e: Exception) {
                Log.e("StorageService", "Error filtering entries", e)
                emit(emptyList())
            }
        }
    }

    suspend fun getDiaryEntry(diaryEntryId: String): DiaryEntry? {
        try {
            val entry = supabase.from("entries").select() {
                filter {
                    eq("id", diaryEntryId)
                }
            }.decodeSingle<EntryUpdate>()
            return DiaryEntry(
                id = entry.id,
                mood = entry.mood,
                star = entry.star,
                title = entry.title,
                content = entry.content,
                location = entry.location ?: "",
                dateTime = entry.created_at.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                photoUrls = entry.photo_urls,
                voiceMemoUrls = entry.voice_memo_urls,
                viewCount = entry.view_count
            )
        } catch (e: Exception) {
            Log.e("StorageService", "Error getting entry", e)
            return null
        }
    }

    suspend fun incrementViewCount(id: String, currentCount: Int) {
        try {
            supabase.from("entries").update(mapOf("view_count" to currentCount + 1)) {
                filter { eq("id", id) }
            }
        } catch (e: Exception) {
            Log.e("StorageService", "Error incrementing view count", e)
        }
    }

    suspend fun save(diaryEntry: DiaryEntry): String {
        val userId = supabase.auth.currentSessionOrNull()?.user?.id ?: ""
        val serialEntry = NewEntry(
            user_id = userId,
            created_at = diaryEntry.dateTime.toInstantSafe(),
            title = diaryEntry.title,
            content =  diaryEntry.content,
            mood = diaryEntry.mood,
            star = diaryEntry.star,
            location = diaryEntry.location,
            photo_urls = diaryEntry.photoUrls,
            voice_memo_urls = diaryEntry.voiceMemoUrls,
            view_count = 0
        )
        val response = supabase.from("entries").insert(serialEntry) {
            select()
        }.decodeSingle<EntryUpdate>()
        return response.id
    }

    suspend fun update(diaryEntry: DiaryEntry) {
        val userId = supabase.auth.currentSessionOrNull()?.user?.id ?: ""
        val serialEntry = EntryUpdate(
            id = diaryEntry.id,
            user_id = userId,
            created_at = diaryEntry.dateTime.toInstantSafe(),
            title = diaryEntry.title,
            content =  diaryEntry.content,
            mood = diaryEntry.mood,
            star = diaryEntry.star,
            location = diaryEntry.location,
            photo_urls = diaryEntry.photoUrls,
            voice_memo_urls = diaryEntry.voiceMemoUrls,
            view_count = diaryEntry.viewCount
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

    suspend fun uploadPhoto(bytes: ByteArray): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        photoBucket.upload(fileName, bytes)
        return photoBucket.publicUrl(fileName)
    }

    suspend fun uploadAudio(bytes: ByteArray): String {
        val fileName = "${UUID.randomUUID()}.m4a"
        audioBucket.upload(fileName, bytes)
        return audioBucket.publicUrl(fileName)
    }
}
