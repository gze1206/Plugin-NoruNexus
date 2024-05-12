package net.gze1206.noruNexus.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

fun String.component(color: TextColor? = NamedTextColor.WHITE) = Component.text()
    .content(this)
    .decoration(TextDecoration.ITALIC, false)
    .color(color)
    .build()
fun String.component(color: String) = this.component(TextColor.fromHexString(color))
val String.component : Component
    get() = component()
operator fun String.not() = component

operator fun Component.plus(component: Component) = this.append(component)