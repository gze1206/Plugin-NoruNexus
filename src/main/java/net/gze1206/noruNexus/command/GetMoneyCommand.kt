package net.gze1206.noruNexus.command

import net.gze1206.noruNexus.core.Constants.MONEY_UNIT_KEY
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.not
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object GetMoneyCommand : TabExecutor {
    private const val UNIT = 100

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
        if (0 < amount % UNIT) {
            sender.sendMessage("출금은 $UNIT 단위로만 할 수 있습니다.")
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

            val item = ItemStack(Material.GOLD_NUGGET, (amount / UNIT).toInt())
            val itemMeta = item.itemMeta
            itemMeta.setCustomModelData(1)
            itemMeta.displayName(!"${UNIT}원")
            itemMeta.persistentDataContainer.set(MONEY_UNIT_KEY, PersistentDataType.INTEGER, UNIT)
            item.itemMeta = itemMeta
            player.inventory.addItem(item)
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>) : MutableList<String> {
        return mutableListOf()
    }
}