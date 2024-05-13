package net.gze1206.noruNexus.core

enum class ItemType {
    MONEY,
    RECALL_SCROLL,
    RUNE,
}

enum class RuneType(val displayName: String, val inactiveModelId: Int, val activeModelId: Int) {
    NONE("룬 원석", 10, 10),

    BONE("뼈의 룬", 11, 12),
    ENDER("엔더의 룬", 13, 14),
    GUN_POWDER("화약의 룬", 15, 16),
    IRON("철의 룬", 17, 18),
    MONEY("재물의 룬", 19, 20),
    STRING("실타래의 룬", 21, 22),
}