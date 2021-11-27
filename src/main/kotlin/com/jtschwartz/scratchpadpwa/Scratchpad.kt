package com.jtschwartz.scratchpadpwa

import com.github.mvysny.karibudsl.v10.*
import com.google.gson.*
import com.jtschwartz.scratchpadpwa.utils.Functionality
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.material.Material

@Route("")
@PageTitle("Scratchpad")
@CssImport.Container(
	value = [
		CssImport("styles.css"),
		CssImport("button.css", themeFor = "vaadin-button"),
		CssImport("text-area.css", themeFor = "vaadin-text-area"),
		CssImport("text-field.css", themeFor = "vaadin-text-field")
	]
                    )
@Theme(Material::class)
@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PWA(name = "Scratchpad", shortName = "Scratchpad", iconPath = "icons/icon-512.png", themeColor = "#333333", backgroundColor = "#333333")
class Scratchpad: KComposite() {
	private lateinit var toggleTheme: Button
	private lateinit var searchAndReplace: Button
	private lateinit var formatAsJson: Button
	
	private lateinit var isRegexEnabled: Checkbox
	private lateinit var isCaseSensitive: Checkbox
	
	private lateinit var origin: TextArea
	private lateinit var search: TextField
	private lateinit var replace: TextField
	
	private var formattedJson: String? = null
	
	companion object {
		val gson: Gson = GsonBuilder()
			.setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.serializeNulls()
			.setPrettyPrinting()
			.create()
	}
	
	init {
		UI.getCurrent().element.themeList.add(Material.DARK)
		
		ui {
			appLayout {
				navbar {
					h3("Scratchpad")
					toggleTheme = button(icon = Icon(VaadinIcon.LIGHTBULB)) {
						classNames.add("toggle-theme")
						onLeftClick { Functionality.toggleTheme() }
					}
				}
				
				content {
					div {
						classNames.add("content")
						setSizeFull()
						origin = textArea {
							isAutofocus = true
							valueChangeMode = ValueChangeMode.EAGER
							addValueChangeListener {
								processOriginContents()
							}
						}
						hr {}
						div {
							classNames.add("controls")
							div {
								classNames.add("controls--inputs")
								search = textField {
									label = "Search"
									valueChangeMode = ValueChangeMode.EAGER
									addValueChangeListener {
										searchAndReplace.isEnabled = value.isNotEmpty()
									}
								}
								replace = textField {
									label = "Replace"
								}
							}
							div {
								classNames.add("controls--options")
								isRegexEnabled = checkBox {
									label = "Use Regex"
									onLeftClick { isCaseSensitive.isEnabled = !value }
								}
								br {}
								isCaseSensitive = checkBox {
									label = "Case Sensitivity"
								}
							}
							div {
								classNames.add("controls--submit")
								searchAndReplace = button("Search & Replace") {
									isEnabled = false
									onLeftClick { searchAndReplace() }
								}
								br {}
								br {}
								formatAsJson = button("Format As JSON") {
									isEnabled = false
									onLeftClick { replaceOriginWithFormattedJson() }
								}
							}
						}
					}
				}
			}
		}
	}
	
	private fun searchAndReplace() {
		origin.value = if (isRegexEnabled.value) {
			origin.value.replace(search.value.toRegex(), replace.value)
		} else {
			origin.value.replace(search.value, replace.value, !isCaseSensitive.value)
		}
	}
	
	private fun processOriginContents() {
		if (origin.value.isBlank()) {
			formatAsJson.isEnabled = false
		} else {
			try {
				formattedJson = gson.toJson(gson.fromJson(origin.value, Any::class.java))
				formatAsJson.isEnabled = true
			} catch (e: Exception) {
				formatAsJson.isEnabled = false
			}
		}
	}
	
	private fun replaceOriginWithFormattedJson() {
		origin.value = formattedJson
	}
}
