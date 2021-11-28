package com.jtschwartz.jotitdown

import eu.vaadinonkotlin.VaadinOnKotlin
import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class Bootstrap : ServletContextListener {
	override fun contextInitialized(sce: ServletContextEvent?) {
		log.info("Starting up")
		VaadinOnKotlin.init()
	}
	
	override fun contextDestroyed(sce: ServletContextEvent?) {
		log.info("Shutting down")
		log.info("Destroying VaadinOnKotlin")
		VaadinOnKotlin.destroy()
		log.info("Shutdown complete")
	}
	
	companion object {
		@JvmStatic
		private val log = LoggerFactory.getLogger(Bootstrap::class.java)
	}
}
