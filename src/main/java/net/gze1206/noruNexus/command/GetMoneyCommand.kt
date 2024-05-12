package net.gze1206.noruNexus.command

import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object GetMoneyCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) : Boolean {
        if (sender !is Player){
            return false
        }

        if (args.size != 1) {
            sender.sendMessage("올바른 명령어 사용법이 아닙니다.")
            return false
        }

        val amount = args[0].toLongOrNull()
        if (amount == null) {
            sender.sendMessage("올바른 값이 아닙니다.")
            return false
        }

        val player = sender.player!!
        val succeed = UserManager.getUser(player).transaction {
            if (money < amount) {
                sender.sendMessage("소지금보다 큰 금액을 출금할 수 없습니다.")
                return@transaction
            }

            money -= amount
        }

        if (!succeed) {
            sender.sendMessage("출금에 실패했습니다.")
            return false
        }

        UserManager.getUser(player).run {
            player.updateScoreboard(this)

            val item = ItemManager.createMoney(amount)
            player.inventory.addItem(item)
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>) : MutableList<String> {
        return mutableListOf()
    }
}