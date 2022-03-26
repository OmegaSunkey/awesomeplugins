package om.ega.sunkey

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.utils.RxUtils.onBackpressureBuffer
import com.aliucord.utils.RxUtils.subscribe
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*

@AliucordPlugin(requiresRestart = false)
class StartupSound2 : Plugin() {
    override fun start(context: Context) {
        startupdiscord()
    }

public fun startupdiscord() {
	sonido = "https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true"
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
} 
