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
}
