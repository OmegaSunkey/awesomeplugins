package com.github.yournamehere

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*

// Aliucord Plugin annotation. Must be present on the main class of your plugin
@AliucordPlugin(requiresRestart = false /* Whether your plugin requires a restart after being installed/updated */)
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
class MyFirstPatch : Plugin() {
    override fun start(context: Context) {
        startupdiscord()
    }

private fun startupdiscord() {
        try {
            Utils.threadPool.execute {
                MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource("https://github.com/ItzOnlyAnimal/AliuPlugins/raw/main/boom.ogg")
                    prepare()
                    start()
                }
            }
        } catch (ignored: Throwable) {
            // ill leave this empty
        }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
} //literally c+p from animal's repo lmao how do i build on github help
