package net.gze1206.noruNexus.core

enum class ItemType {
    MONEY,
    RECALL_SCROLL,
    RUNE,
}

enum class RuneType(val displayName: String, val lore: String, val defaultParams: Array<Int>, val inactiveModelId: Int, val activeModelId: Int) {
    EMPTY("룬 원석", "룬이 비어있습니다. /룬 명령어를 통해 적절한 아이템과 조합해 룬에 문양을 새겨주세요.", arrayOf(), 10, 10),

    BONE("뼈의 룬", "이 룬을 사용하면 %1~%2개의 뼈를 얻을 수 있습니다.", arrayOf(1, 3), 11, 12),
    ENDER("엔더의 룬", "이 룬을 사용하면 %1~%2개의 엔더 진주를 얻을 수 있습니다. 간혹 엔더의 눈이 나오는 경우도 있습니다.", arrayOf(1, 3, 10), 13, 14),
    GUNPOWDER("화약의 룬", "이 룬을 사용하면 %1~%2개의 화약을 얻을 수 있습니다.", arrayOf(1, 3), 15, 16),
    IRON("철의 룬", "이 룬을 사용하면 %1~%2개의 철 주괴를 얻을 수 있습니다.", arrayOf(1, 3), 17, 18),
    MONEY("재물의 룬", "이 룬을 사용하면 %1~%2원을 얻을 수 있습니다.", arrayOf(1, 3), 19, 20),
    STRING("실타래의 룬", "이 룬을 사용하면 %1~%2개의 실을 얻을 수 있습니다.", arrayOf(1, 3), 21, 22),
}