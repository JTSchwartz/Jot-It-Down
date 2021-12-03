package com.jtschwartz.jotitdown

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.github.mvysny.karibudsl.v10.*
import com.google.gson.*
import com.jtschwartz.jotitdown.utils.FormatTypes
import com.jtschwartz.jotitdown.utils.ThemeUtils
import com.jtschwartz.jotitdown.utils.Utils.processOriginContents
import com.jtschwartz.jotitdown.utils.Utils.replaceOriginWithFormatted
import com.jtschwartz.jotitdown.utils.Utils.searchAndReplace
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
@PageTitle("Jot It Down")
@CssImport.Container(
	value = [
		CssImport("./styles.css"),
		CssImport("./button.css", themeFor = "vaadin-button"),
		CssImport("./select.css", themeFor = "vaadin-select"),
		CssImport("./text-area.css", themeFor = "vaadin-text-area"),
		CssImport("./text-field.css", themeFor = "vaadin-text-field")
	])
@Theme(Material::class)
@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PWA(name = "Jot It Down", shortName = "Jot It Down", iconPath = "icons/icon-512.png", themeColor = "#333333", backgroundColor = "#333333")
class JotItDown: KComposite() {
	private lateinit var toggleThemeButton: Button
	private lateinit var searchAndReplaceButton: Button
	lateinit var formatAsButton: Button
	
	lateinit var isRegexEnabled: Checkbox
	lateinit var isCaseSensitive: Checkbox
	
	lateinit var origin: TextArea
	lateinit var search: TextField
	lateinit var replace: TextField
	
	var formatted: String? = null
	var dataFormat: FormatTypes = FormatTypes.JSON
	
	companion object {
		private val gson: Gson = GsonBuilder()
			.setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
			.serializeNulls()
			.setPrettyPrinting()
			.create()
		
		private val yaml = ObjectMapper(YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES).disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
		
		val formatAsJSON = { original: String -> gson.toJson(gson.fromJson(original, Any::class.java)) }
		val formatAsYAML = { original: String -> yaml.writeValueAsString(yaml.readValue(original, Any::class.java)) }
	}
	
	init {
		ThemeUtils.preferredTheme()
		ui {
			appLayout {
				navbar {
					h3("Jot It Down")
					toggleThemeButton = button(icon = Icon(VaadinIcon.LIGHTBULB)) {
						classNames.add("toggle-theme")
						onLeftClick { ThemeUtils.toggleTheme() }
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
										searchAndReplaceButton.isEnabled = value.isNotEmpty()
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
								searchAndReplaceButton = button("Search & Replace") {
									isEnabled = false
									onLeftClick { searchAndReplace() }
								}
								br {}
								br {}
								formatAsButton = button("Format As") {
									isEnabled = false
									onLeftClick { replaceOriginWithFormatted() }
								}
								select<FormatTypes> {
									setItems(FormatTypes.JSON, FormatTypes.YAML)
									value = FormatTypes.JSON
									addValueChangeListener {
										dataFormat = value
										processOriginContents()
									}
								}
							}
							button(icon = Icon(VaadinIcon.CODE)) {
								classNames.add("source-code-link")
								onLeftClick { UI.getCurrent().page.open("https://github.com/JTSchwartz/Jot-It-Down") }
							}
						}
					}
				}
			}
		}
	}
}
