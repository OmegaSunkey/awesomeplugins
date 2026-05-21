package om.ega.sunkey.StartupSound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.View

import java.io.File

import com.aliucord.Utils
import com.aliucord.Http
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*

import com.discord.widgets.chat.list.WidgetChatList

@AliucordPlugin(requiresRestart = false)
class StartupSound : Plugin() {
    init {
        settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }
    var started = false
    override fun start(context: Context) {
        //val sound = File(settings.getString("sonido", "/sdcard/Aliucord/startup.mp3"))
        val sound = settings.getString("sonido", "/sdcard/Aliucord/startup.mp3")
        if(sound == "/sdcard/Aliucord/startup.mp3" && !File("/sdcard/Aliucord/startup.mp3").exists()) {
            Utils.threadPool.execute { 
                Http.simpleDownload(
                    settings.getString("sonido", "https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true"), 
                    File("/sdcard/Aliucord/startup.mp3")
                )
            }
            
        } else if(sound.contains("https://")) {
            Utils.threadPool.execute { 
                Http.simpleDownload(
                    settings.getString("sonido", "https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true"), 
                    File("/sdcard/Aliucord/userstartup.mp3")
                )
            }
            settings.setString("sonido", "/sdcard/Aliucord/userstartup.mp3")
        }
        patcher.patch(
            WidgetChatList::class.java.getDeclaredMethod(
                "onViewBound",
                View::class.java
            ), Hook {
                startupdiscord(settings.getString("sonido", "/sdcard/Aliucord/startup.mp3"))
            }
        )
    }

    private fun startupdiscord(startup: String) {
        if(!started) {
            started = true
            try {
                Utils.threadPool.execute {
                    Thread.sleep(1000)
                    MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(startup)
                        prepare()
                        start()
                    }
                }
            } catch (e: Throwable) {
                logger.error("UNABLE to play audio", e)
            }
        }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
} // literally c+p from animal's repo lmao how do i build on github help
