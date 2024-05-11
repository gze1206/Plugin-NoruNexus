package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.model.User
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class UserMoneyUpdateEvent(val sender: User, val money: Long, isAsync: Boolean) : Event(isAsync) {
    companion object : Listener {
        const val RICH = "rich"
        const val RIICH = "riich"
        const val RIIICH = "riiich"

        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList

        @EventHandler
        fun onUserMoneyUpdateEvent(e: UserMoneyUpdateEvent) {
            val config = ConfigManager.config

            if (e.money >= config.getLong("title-trigger.$RICH"))
                Title.give(e.sender, RICH)
            if (e.money >= config.getLong("title-trigger.$RIICH"))
                Title.give(e.sender, RIICH)
            if (e.money >= config.getLong("title-trigger.$RIIICH"))
                Title.give(e.sender, RIIICH)
        }
    }

    override fun getHandlers() : HandlerList = handlerList
}