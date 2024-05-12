package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.model.User
import net.gze1206.noruNexus.utils.component
import net.gze1206.noruNexus.utils.not
import net.gze1206.noruNexus.utils.plus
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class UserGetTitleEvent(val sender: User, val title: Title, isAsync: Boolean) : Event(isAsync) {
    companion object : Listener {
        private val handlerList = HandlerList()

        @Suppress("unused")
        @JvmStatic
        fun getHandlerList() = handlerList

        @EventHandler
        fun onUserGetTitleEvent(e: UserGetTitleEvent) {
            val player = Main.getInstance().server.getPlayer(e.sender.uuid) ?: return
            val titleComponent = "[${e.title.displayName}]".component(e.title.rarity.color)

            if (e.title.globalBroadcast) {
                Main.getInstance().server.sendMessage(
                    Constants.SYSTEM_MESSAGE_COMPONENT
                    + e.sender.getDisplayName()
                    + !"님이 "
                    + titleComponent
                    + !" 칭호를 획득하셨습니다!"
                )
                player.sendMessage("/title 명령어를 사용해 칭호를 확인하고 착용할 수 있습니다.")
            }
            else {
                player.sendMessage(
                    titleComponent
                    + !" 칭호를 획득하셨습니다!"
                )
                player.sendMessage("/title 명령어를 사용해 칭호를 확인하고 착용할 수 있습니다.")
            }
        }

    }

    override fun getHandlers() : HandlerList = handlerList
}