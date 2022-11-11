package com.jtschwartz.jotitdown.utils

import com.jtschwartz.jotitdown.JotItDown
import eu.vaadinonkotlin.vaadin10.Cookies
import eu.vaadinonkotlin.vaadin10.plusAssign
import javax.servlet.http.Cookie

object Utils {
	private const val yearInSeconds = 31556952
	
	fun getCookieByName(name: String): Cookie? = Cookies[name]
	
	fun setCookie(key: String, value: String, expirationInSeconds: Int = yearInSeconds) {
		Cookies += Cookie(key, value).apply { maxAge = expirationInSeconds }
	}
	
	fun JotItDown.search(i: Int): IntRange? {
		return if (isRegexEnabled.value) {
			search.value.toRegex().find(origin.value, i)?.range
		} else {
			val index = origin.value.indexOf(search.value, i)
			index until index + search.value.length
		}
	}
	
	fun JotItDown.replaceRange(range: IntRange) {
		origin.value = if (isRegexEnabled.value) {
			val replacementSlice = origin.value.slice(range).replace(search.value.toRegex(), replace.value)
			origin.value.replaceRange(range, replacementSlice)
		} else {
			origin.value.replaceRange(range, replace.value)
		}
	}
	
	fun JotItDown.replaceAll() {
		origin.value = if (isRegexEnabled.value) {
			origin.value.replace(search.value.toRegex(), replace.value)
		} else {
			origin.value.replace(search.value, replace.value, !isCaseSensitive.value)
		}
	}
	
	fun JotItDown.processOriginContents() {
		if (origin.value.isBlank()) {
			formatAsButton.isEnabled = false
			return
		}
		
		try {
			formatted = when (dataFormat) {
				FormatTypes.JSON -> JotItDown.formatAsJSON(origin.value)
				FormatTypes.YAML -> JotItDown.formatAsYAML(origin.value)
			}
			formatAsButton.isEnabled = true
		} catch (e: Exception) {
			formatAsButton.isEnabled = false
		}
	}
	
	fun JotItDown.replaceOriginWithFormatted() {
		origin.value = formatted
	}
}

