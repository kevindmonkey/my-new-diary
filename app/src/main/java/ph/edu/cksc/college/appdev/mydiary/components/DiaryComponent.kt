package ph.edu.cksc.college.appdev.mydiary.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.DIARY_ENTRY_SCREEN
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DiaryEntryCard(
    entry: DiaryEntry,
    navController: NavHostController
) {
    var isExpanded by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a")
    val date = try { LocalDateTime.parse(entry.dateTime) } catch(e: Exception) { LocalDateTime.now() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("$DIARY_ENTRY_SCREEN/${entry.id}") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = moodList[entry.mood].icon,
                        tint = moodList[entry.mood].color,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = entry.title.ifBlank { "Untitled" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = formatter.format(date),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(entry.star) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB400),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = parseHtmlToAnnotatedString(entry.content),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            )
        }
    }
}

fun parseHtmlToAnnotatedString(content: String): AnnotatedString {
    val cleanContent = content.replace("<br/?>".toRegex(), "\n")
        .replace("<p>".toRegex(), "")
        .replace("</p>".toRegex(), "\n\n")
        .trim()

    return buildAnnotatedString {
        val tagRegex = "<(/?[bius]|a[^>]*)>".toRegex()
        var lastIndex = 0
        val styleStack = mutableListOf<SpanStyle>()

        tagRegex.findAll(cleanContent).forEach { match ->
            append(cleanContent.substring(lastIndex, match.range.first))
            val fullTag = match.groupValues[1]
            
            if (fullTag.startsWith("/")) {
                if (styleStack.isNotEmpty()) styleStack.removeAt(styleStack.size - 1)
            } else {
                val style = when {
                    fullTag == "b" -> SpanStyle(fontWeight = FontWeight.Bold)
                    fullTag == "i" -> SpanStyle(fontStyle = FontStyle.Italic)
                    fullTag == "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
                    fullTag == "s" -> SpanStyle(textDecoration = TextDecoration.LineThrough)
                    fullTag.startsWith("a") -> SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
                    else -> SpanStyle()
                }
                styleStack.add(style)
            }
            
            lastIndex = match.range.last + 1
            if (styleStack.isNotEmpty()) {
                val currentStyle = styleStack.reduce { acc, spanStyle -> acc.merge(spanStyle) }
                pushStyle(currentStyle)
            }
        }
        append(cleanContent.substring(lastIndex))
    }
}

@Composable
fun DiaryList(
    entries: List<DiaryEntry>,
    navController: NavHostController
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(entries) { entry ->
            DiaryEntryCard(entry, navController)
        }
    }
}
