package com.jtschwartz.jotitdown.utils

import com.jtschwartz.jotitdown.utils.Utils.getCookieByName
import com.jtschwartz.jotitdown.utils.Utils.setCookie
import com.vaadin.flow.component.UI
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.theme.material.Material


object ThemeUtils {
	const val THEME_KEY = "theme"
	private const val ENABLED = "enabled"
	private const val DISABLED = "disabled"
	
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
