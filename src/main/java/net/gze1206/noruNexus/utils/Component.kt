package net.gze1206.noruNexus.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

fun String.component() = Component.text(this, NamedTextColor.WHITE)
val String.component : Component
    get() = component()
operator fun String.not() = component

operator fun Component.plus(component: Component) = this.append(component)