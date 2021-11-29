package com.jtschwartz.jotitdown.utils

import com.vaadin.flow.component.UI
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.server.VaadinService
import com.vaadin.flow.theme.material.Material
import eu.vaadinonkotlin.vaadin10.Cookies
import eu.vaadinonkotlin.vaadin10.plusAssign
import javax.servlet.http.Cookie


object Functionality {
	private const val yearInSeconds = 31556952
	private const val THEME_KEY = "theme"
	private const val ENABLED = "enabled"
	private const val DISABLED = "disabled"
	
	private fun getCookieByName(name: String): Cookie? = Cookies[name]
	
	private fun setCookie(key: String, value: String, expirationInSeconds: Int = yearInSeconds) {
		Cookies += Cookie(key, value).apply { maxAge = expirationInSeconds }
	}
	
	fun preferredTheme() {
		val cookie = getCookieByName(THEME_KEY) ?: return
		
		if (cookie.value == ENABLED) toggleTheme()
	}
	
	fun toggleTheme() {
		val themeList: ThemeList = UI.getCurrent().element.themeList
		
		if (themeList.contains(Material.DARK)) {
			themeList.remove(Material.DARK)
			setCookie(THEME_KEY, DISABLED)
		} else {
			themeList.add(Material.DARK)
			setCookie(THEME_KEY, ENABLED)
		}
	}
}
