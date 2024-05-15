package net.gze1206.noruNexus.config

import net.gze1206.noruNexus.core.RuneType
import java.security.InvalidParameterException

data class RuneConfig(val path: String, val fileName: String) {
    data class Item(val rate: Int, val amount: Int)

    companion object {
        private const val PROGRESS_ON_KILL_MONSTER_KEY = "progress.kill-monster"
        private const val PROGRESS_ON_MINING_KEY = "progress.mining"
        private const val PARAMS_KEY = "params"
        private const val DEFAULT_KEY = "default"
        private const val EMPTY_RUNE_DROPS_KEY = "empty-rune-drops"
        private const val RATE_KEY = "rate"
        private const val AMOUNT_KEY = "amount"
    }

    private val config = ConfigFile(path, fileName)
    private val emptyRuneDropOverrides = HashMap<String, Item>()
    private val progressOnKillMonsterOverrides = HashMap<String, Double>()
    private val progressOnMiningOverrides = HashMap<String, Double>()
    private val runeParameters = HashMap<RuneType, Array<Int>>()
    private lateinit var defaultEmptyRuneDrop : Item
    private var defaultProgressOnKillMonster = 0.0
    private var defaultProgressOnMining = 0.0

    fun init() {
        config.getConfig().run {
            options().copyDefaults(true)

            addDefault("${PROGRESS_ON_KILL_MONSTER_KEY}.$DEFAULT_KEY", 0.01)
            addDefault("${PROGRESS_ON_MINING_KEY}.$DEFAULT_KEY", 0.015)
            addDefault("${EMPTY_RUNE_DROPS_KEY}.${DEFAULT_KEY}.$RATE_KEY", 50)
            addDefault("${EMPTY_RUNE_DROPS_KEY}.${DEFAULT_KEY}.$AMOUNT_KEY", 1)

            RuneType.entries.forEach {
                addDefault("${PARAMS_KEY}.${it.name}", it.defaultParams)
            }
        }

        loadConfig()
    }

    fun getDropInfo(name: String) : Item {
        return emptyRuneDropOverrides[name.lowercase()] ?: defaultEmptyRuneDrop
    }

    fun getProgressOnKillMonster(name: String) : Double {
        return progressOnKillMonsterOverrides[name.lowercase()] ?: defaultProgressOnKillMonster
    }

    fun getProgressOnMining(name: String) : Double {
        return progressOnMiningOverrides[name.lowercase()] ?: defaultProgressOnMining
    }

    fun getParameters(runeType: RuneType) : Array<Int> {
        return runeParameters[runeType]!!
    }

    fun reload() {
        config.reload()
        emptyRuneDropOverrides.clear()
        progressOnKillMonsterOverrides.clear()
        progressOnMiningOverrides.clear()
        runeParameters.clear()
        loadConfig()
    }

    fun save() {
        config.save()
    }

    private fun loadConfig() {
        config.getConfig().run {
            var section = getConfigurationSection(PROGRESS_ON_KILL_MONSTER_KEY)!!
            section.getKeys(false).forEach {
                val key = it.lowercase()
                val value = section.getDouble(it)
                if (key == DEFAULT_KEY) {
                    defaultProgressOnKillMonster = value
                } else {
                    progressOnKillMonsterOverrides[key] = value
                }
            }

            section = getConfigurationSection(PROGRESS_ON_MINING_KEY)!!
            section.getKeys(false).forEach {
                val key = it.lowercase()
                val value = section.getDouble(it)
                if (key == DEFAULT_KEY) {
                    defaultProgressOnMining = value
                } else {
                    progressOnMiningOverrides[key] = value
                }
            }

            section = getConfigurationSection(EMPTY_RUNE_DROPS_KEY)!!
            section.getKeys(false).forEach {
                val key = it.lowercase()
                val item = Item(
                    section.getInt("${it}.$RATE_KEY"),
                    section.getInt("${it}.$AMOUNT_KEY")
                )
                if (key == DEFAULT_KEY) {
                    defaultEmptyRuneDrop = item
                } else {
                    emptyRuneDropOverrides[key] = item
                }
            }

            RuneType.entries.forEach {
                val array = getIntegerList("${PARAMS_KEY}.${it.name}").toTypedArray()
                runeParameters[it] = if (array.isEmpty()) it.defaultParams else array

                if (runeParameters[it]!!.size != it.defaultParams.size) {
                    throw InvalidParameterException("${it.displayName} 룬의 매개변수 설정에 이상이 있습니다.")
                }
            }
        }
    }
}