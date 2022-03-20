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
class StartupSound : Plugin() {
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
                    setDataSource("https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true")
                    prepare()
                    start()
                }
            }
        } catch (ignored: Throwable) {
            //not dealing rn
        }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
} // literally c+p from animal's repo lmao how do i build on github help
