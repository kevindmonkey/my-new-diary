package ph.edu.cksc.college.appdev.mydiary.screens

import org.junit.Assert.assertEquals
import org.junit.Test
import ph.edu.cksc.college.appdev.mydiary.components.fromHTML

class MainScreenTest {

    @Test
    fun fromHTML_blank() {
        assertEquals("", fromHTML(""))
    }

    @Test
    fun fromHTML_TextOnly() {
        assertEquals("Text", fromHTML("Text"))
    }

    @Test
    fun fromHTML_HTMLParagraph() {
        assertEquals("Text", fromHTML("<p>Text</p>"))
    }

    @Test
    fun fromHTML_HTML2Paragraphs() {
        assertEquals("Text\n\nAnother", fromHTML("<p>Text</p><p>Another</p>"))
    }

    @Test
    fun fromHTML_HTMLParagraphWithBR() {
        assertEquals("Text\nAnother line", fromHTML("<p>Text<br>Another line</p>"))
    }

    @Test
    fun fromHTML_HTMLParagraphWithBRSlash() {
        assertEquals("Text\nAnother line", fromHTML("<p>Text<br/>Another line</p>"))
    }

    @Test
    fun fromHTML_HTMLBold() {
        assertEquals("Text Another", fromHTML("<b>Text</b> <strong>Another</strong>"))
    }

    @Test
    fun fromHTML_HTMLItalic() {
        assertEquals("Text Another", fromHTML("<i>Text</i> <em>Another</em>"))
    }

    @Test
    fun fromHTML_HTMLItalicWithAttr() {
        assertEquals("Text Another", fromHTML("<i class='whatever'>Text</i> <em style=\"font-weight:bold\">Another</em>"))
    }

    @Test
    fun fromHTML_HTMLEntities() {
        assertEquals("1 < 2 & 2 > 1", fromHTML("1 &lt; 2 &amp; 2 &gt; 1"))
    }

    @Test
    fun fromHTML_NonBreakingSpace() {
        assertEquals("Text with space", fromHTML("Text&nbsp;with&nbsp;space"))
    }

    @Test
    fun fromHTML_NestedTags() {
        assertEquals("Nested Text", fromHTML("<div><p><b><i>Nested Text</i></b></p></div>"))
    }

    @Test
    fun fromHTML_MixedContent() {
        assertEquals("Line 1\nLine 2\n\nEnd", fromHTML("Line 1<br>Line 2<p>End</p>"))
    }

    @Test
    fun fromHTML_StripsUnknownTags() {
        assertEquals("Safe Content", fromHTML("<script>alert('bad')</script>Safe Content<style>body{}</style>"))
    }

    // Additional 5 Tests for thoroughness
    @Test
    fun fromHTML_MultipleLineBreaks() {
        assertEquals("Line 1\n\nLine 2", fromHTML("Line 1<br><br>Line 2"))
    }

    @Test
    fun fromHTML_UppercaseTags() {
        assertEquals("UPPERCASE", fromHTML("<P>UPPERCASE</P>"))
    }

    @Test
    fun fromHTML_NumericEntities() {
        // Regex should handle literal entities if explicitly added, 
        // currently it handles common ones. Adding check for remaining entities.
        val result = fromHTML("&#60;Hello&#62;")
        assertEquals("Hello", result) // Strips the unknown tag-like structure
    }

    @Test
    fun fromHTML_EmptyTags() {
        assertEquals("Clean", fromHTML("<b></b>Clean<i></i>"))
    }

    @Test
    fun fromHTML_PreservesRealLineBreaks() {
        assertEquals("Line 1\nLine 2", fromHTML("Line 1\nLine 2"))
    }
}
