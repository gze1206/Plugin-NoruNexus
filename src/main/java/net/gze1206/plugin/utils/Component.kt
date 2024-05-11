package net.gze1206.plugin.utils

import net.kyori.adventure.text.Component

fun String.component() = Component.text(this)
val String.component : Component
    get() = component()
operator fun String.not() = component

operator fun Component.plus(component: Component) = this.append(component)