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
        private val handlerList = HandlerList()

        @Suppress("unused")
        @JvmStatic
        fun getHandlerList() = handlerList

        @EventHandler
        fun onUserMoneyUpdateEvent(e: UserMoneyUpdateEvent) {
            val config = ConfigManager.config

            if (e.money >= config.getLong("title-trigger.${Title.TitleId.RICH}"))
                Title.give(e.sender, Title.TitleId.RICH)
            if (e.money >= config.getLong("title-trigger.${Title.TitleId.RIICH}"))
                Title.give(e.sender, Title.TitleId.RIICH)
            if (e.money >= config.getLong("title-trigger.${Title.TitleId.RIIICH}"))
                Title.give(e.sender, Title.TitleId.RIIICH)
        }

    }

    override fun getHandlers() : HandlerList = handlerList
}