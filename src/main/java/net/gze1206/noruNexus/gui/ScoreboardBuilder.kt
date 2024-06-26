package net.gze1206.noruNexus.gui

import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.gze1206.noruNexus.core.Constants.RESET_CHARACTER
import net.gze1206.noruNexus.utils.not
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

class ScoreboardBuilder {
    companion object {
        const val MAX_LINE = 16
    }

    private val name = UUID.randomUUID().toString().replace("-", "")
    private val title : Component = !"INFO"
    private val lines = arrayListOf<String>()
    private val scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective(name, Criteria.DUMMY, title).apply {
        displaySlot = DisplaySlot.SIDEBAR
        numberFormat(NumberFormat.blank())
    }

    private fun add(line: Component) : ScoreboardBuilder {
        if (MAX_LINE < lines.size) throw IndexOutOfBoundsException("16줄을 넘을 수 없습니다.")

        val name = RESET_CHARACTER.repeat(lines.size)
        val team = scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)
        team.suffix(line)
        team.addEntry(name)
        objective.getScore(name).score = MAX_LINE - lines.size
        lines.add(name)
        return this
    }

    operator fun set(index: Int, line: Component) {
        if (MAX_LINE <= index) throw IndexOutOfBoundsException("16줄을 넘을 수 없습니다.")

        if (lines.size <= index) {
            repeat(index - lines.size + 1) {
                add(!" ")
            }
        }

        val name = lines[index]
        val team = scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)
        team.suffix(line)
        if (!team.hasEntry(name)) team.addEntry(name)
    }

    operator fun get(index: Int) : Component? {
        return lines.getOrNull(index)?.let { scoreboard.getTeam(it)?.suffix() }
    }

    fun scoreboard() = scoreboard
}