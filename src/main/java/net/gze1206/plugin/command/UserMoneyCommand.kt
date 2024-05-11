package net.gze1206.plugin.command

import net.gze1206.plugin.core.UserManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object UserMoneyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("플레이어만 사용 가능한 명령어입니다.")
            return false
        }

        val value = args?.get(0)?.toLongOrNull()
        if (value == null) {
            sender.sendMessage("올바른 값이 아닙니다.")
            return false
        }

        val player = sender.player!!
        UserManager.getUser(player)?.let {
            it.money = value
            it.update()
        }

        UserManager.getUser(player)?.let {
            player.sendMessage("소지 금액이 ${it.money}로 변경되었습니다.")
        }

        return true
    }
}