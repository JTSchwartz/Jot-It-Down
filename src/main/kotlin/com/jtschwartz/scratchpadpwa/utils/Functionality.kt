package com.jtschwartz.scratchpadpwa.utils

import com.vaadin.flow.component.UI
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.theme.material.Material


object Functionality {
	
	fun toggleTheme() {
		val themeList: ThemeList = UI.getCurrent().element.themeList
		
		if (themeList.contains(Material.DARK)) {
			themeList.remove(Material.DARK)
		} else {
			themeList.add(Material.DARK)
		}
	}
}
