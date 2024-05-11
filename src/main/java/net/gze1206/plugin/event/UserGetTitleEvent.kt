package net.gze1206.plugin.event

import net.gze1206.plugin.Main
import net.gze1206.plugin.core.Constants
import net.gze1206.plugin.model.Title
import net.gze1206.plugin.model.User
import net.gze1206.plugin.utils.not
import net.gze1206.plugin.utils.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class UserGetTitleEvent(val sender: User, val title: Title, isAsync: Boolean) : Event(isAsync) {
    companion object : Listener {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList

        @EventHandler
        fun onUserGetTitleEvent(e: UserGetTitleEvent) {
            val player = Main.instance!!.server.getPlayer(e.sender.uuid) ?: return
            val titleComponent = Component.text("[${e.title.displayName}]", TextColor.fromHexString(e.title.color))

            if (e.title.globalBroadcast) {
                Main.instance!!.server.sendMessage(
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

    override fun getHandlers(): HandlerList = handlerList
}