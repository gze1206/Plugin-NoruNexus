package net.gze1206.noruNexus.command

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object UserMoneyCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) : Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.size != 2) {
            sender.sendMessage("올바른 명령어 사용법이 아닙니다.")
            return false
        }

        val target = Main.getInstance().server.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("대상 유저를 찾지 못했습니다.")
            return false
        }

        val amount = args[1].toLongOrNull()
        if (amount == null) {
            sender.sendMessage("올바른 값이 아닙니다.")
            return false
        }

        val succeed = UserManager.getUser(target).transaction {
            money = amount
        }

        if (!succeed) {
            sender.sendMessage("수정 내용을 반영하지 못했습니다.")
            return false
        }

        UserManager.getUser(target).run {
            sender.sendMessage("소지 금액이 ${money}로 변경되었습니다.")
            target.updateScoreboard(this)
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>) : MutableList<String>? {
        // 첫 번째 매개 변수는 유저 이름
        if (args.size < 2) return null

        // 두 번째는 숫자니까 자동 완성 없음
        return mutableListOf()
    }
}