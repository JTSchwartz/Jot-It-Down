package com.jtschwartz.jotitdown.utils

import com.jtschwartz.jotitdown.JotItDown
import com.jtschwartz.jotitdown.utils.Utils.replaceAll
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UtilsTest {
	
	private lateinit var app: JotItDown
	
	@BeforeAll
	internal fun beforeAll() {
		mockkObject(Utils)
		every { Utils.getCookieByName(ThemeUtils.THEME_KEY) } returns null
		app = JotItDown()
	}
	
	@BeforeEach
	internal fun setUp() {
		app.apply {
			origin.value = """
			The quick
			brown fox jumps
			over
			the lazy dog
		""".trimIndent()
			
			search.value = ""
			replace.value = ""
			
			isRegexEnabled.value = false
			isCaseSensitive.value = false
			dataFormat = FormatTypes.JSON
		}
	}
	
	companion object {
		@JvmStatic
		fun noOptionsTests() = listOf(
			Arguments.of(
			" ",
			"",
			"""
			Thequick
			brownfoxjumps
			over
			thelazydog
		""".trimIndent()))
		
		@JvmStatic
		fun regexTests() = listOf(
			Arguments.of(
				"\\n",
				" ",
				"The quick brown fox jumps over the lazy dog"))
	}
	
	@ParameterizedTest
	@MethodSource("noOptionsTests")
	fun `Test search and replace without any options enabled`(searchFor: String, replaceWith: String, expected: String) {
		app.apply{
			search.value = searchFor
			replace.value = replaceWith
			replaceAll()
			assertEquals(expected, app.origin.value)
		}
	}
	
	@ParameterizedTest
	@MethodSource("regexTests")
	fun `Test search and replace with regex enabled`(searchFor: String, replaceWith: String, expected: String) {
		app.apply {
			search.value = searchFor
			replace.value = replaceWith
			isRegexEnabled.value = true
			replaceAll()
			assertEquals(expected, app.origin.value)
		}
	}
}
