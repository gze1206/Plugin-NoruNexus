package net.gze1206.plugin.model

import java.util.UUID

data class User (
    val uuid : UUID,
    var displayName: String,
    var prefix : String?,
    var money: ULong,
)