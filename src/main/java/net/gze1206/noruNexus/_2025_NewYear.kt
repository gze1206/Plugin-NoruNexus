package net.gze1206.noruNexus

import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.model.Title
import org.bukkit.Bukkit
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Timer
import java.util.TimerTask

object _2025_NewYear {
    fun schedule() {
        val nowUTC = Instant.now()

        val kstZone = ZoneId.of("Asia/Seoul")
        val newYearKST = LocalDateTime.of(2025, 1, 1, 0, 0).atZone(kstZone)
        val newYearUTC = newYearKST.withZoneSameInstant(ZoneOffset.UTC).toInstant()

        val delayMillis = newYearUTC.toEpochMilli() - nowUTC.toEpochMilli()

        if (delayMillis > 0) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    Bukkit.getScheduler().runTask(Main.getInstance()) { _ ->
                        giveTitle()
                    }
                }
            }, delayMillis)
            Main.getLog().info("New Year task scheduled for KST Timezone (UTC : $newYearUTC)")
        } else {
            Main.getLog().warning("Failed to schedule New Year task. Time may have passed.")
        }
    }

    private fun giveTitle() {
        Main.getInstance().server.onlinePlayers.forEach { player ->
            player.sendMessage("§62025년 새해 복 많이 받으세요!")
            Title.give(UserManager.getUser(player), Title.TitleId._2025_NewYear)
        }
        Main.getLog().info("Titles granted to all online players!")
    }
}