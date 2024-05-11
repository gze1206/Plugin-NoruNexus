package net.gze1206.plugin.command

import net.gze1206.plugin.Main
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
        if (args?.size != 2) {
            sender.sendMessage("올바른 명령어 사용법이 아닙니다.")
            return false
        }

        val target = Main.instance!!.server.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("대상 유저를 찾지 못했습니다.")
            return false
        }

        val amount = args[1].toLongOrNull()
        if (amount == null) {
            sender.sendMessage("올바른 값이 아닙니다.")
            return false
        }

        val succeed = true == UserManager.getUser(target)?.transaction {
            it.money = amount
        }

        if (!succeed) {
            sender.sendMessage("수정 내용을 반영하지 못했습니다.")
            return false
        }

        UserManager.getUser(target)?.let {
            sender.sendMessage("소지 금액이 ${it.money}로 변경되었습니다.")
            it.updateScoreboard(target)
        }

        return true
    }
}