package com.jtschwartz.scratchpadpwa

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.material.Material

@Route("")
@PageTitle("Scratchpad")
@CssImport("styles.css")
@CssImport("button.css", themeFor = "vaadin-button")
@CssImport("text-area.css", themeFor = "vaadin-text-area")
@CssImport("text-field.css", themeFor = "vaadin-text-field")
@Theme(Material::class, variant = Material.DARK)
@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PWA(name = "Scratchpad by Jacob Schwartz", shortName = "Scratchpad", iconPath = "icons/icon-512.png", themeColor = "#333333", backgroundColor = "#333333")
class Scratchpad: KComposite() {
	init {
		ui {
			appLayout {
				navbar {
					h3("Scratchpad")
					button(icon = Icon(VaadinIcon.LIGHTBULB)) {
						addThemeVariants(ButtonVariant.MATERIAL_OUTLINED)
						classNames.add("toggle-theme")
					}
				}
				
				content {
					div {
						classNames.add("content")
						setSizeFull()
						textArea {
							isAutofocus = true
						}
						hr {}
						div {
							classNames.add("controls")
							div {
								classNames.add("controls--inputs")
								textField {
									label = "Search"
								}
								textField {
									label = "Replace"
								}
							}
							div {
								classNames.add("controls--options")
								checkBox {
									label = "Use Regex"
								}
								br {}
								checkBox {
									label = "Case Sensitivity"
								}
								br {}
								checkBox {
									label = "Limit to Selection"
								}
							}
							div {
								classNames.add("controls--submit")
								button("Search & Replace") {
									addThemeVariants(ButtonVariant.MATERIAL_CONTAINED)
								}
								br {}
								br {}
								button("Format As JSON") {
									addThemeVariants(ButtonVariant.MATERIAL_CONTAINED)
								}
							}
						}
					}
				}
			}
		}
	}
}
