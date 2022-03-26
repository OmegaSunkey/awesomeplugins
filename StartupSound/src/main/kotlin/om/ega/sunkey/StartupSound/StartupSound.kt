package om.ega.sunkey.StartupSound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.SettingsAPI
import com.aliucord.patcher.*

@AliucordPlugin(requiresRestart = false)
class StartupSound : Plugin() {
	init {
		settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
	}
    override fun start(context: Context) {
        startupdiscord()
    }

val sonido = settings.getString("sonido", "https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true")

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
                    setDataSource(sonido)
                    prepare()
                    start()
                }
            }
        } catch (e: Throwable) {
            logger.error("UNABLE to play audio", e)
        }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
} // literally c+p from animal's repo lmao how do i build on github help
