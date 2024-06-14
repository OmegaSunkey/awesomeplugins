package om.ega.sunkey.NoticeSound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.File
import com.aliucord.Utils
import com.aliucord.Http
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.discord.widgets.notice.NoticePopup

@AliucordPlugin(requiresRestart = false)
class NoticeSound : Plugin() {
	init {
		settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
	}
	override fun start(context: Context) {
	  val ping = File(settings.getString("sonido", "/sdcard/Aliucord/ping.mp3"))
	  if(!ping.exists()) Http.simpleDownload(settings.getString("sonido", "https://github.com/OmegaSunkey/awesomeplugins/raw/main/ping.mp3"), File("/sdcard/Aliucord/ping.mp3"))
	  patcher.after<NoticePopup>("getAutoDismissAnimator", Integer::class.java, Function0::class.java){
            sound(settings.getString("sonido", "/sdcard/Aliucord/ping.mp3"))
	  }
	}

	private fun sound(pinger: String) {
          try {
            Utils.threadPool.execute {
              MediaPlayer().apply {
                setAudioAttributes(
                  AudioAttributes.Builder()
                  .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
	          .setUsage(AudioAttributes.USAGE_MEDIA)
                  .build()
                )
                setDataSource(pinger)
                prepare()
                start()
              }
            }
          } catch (e: Throwable) {
            logger.error("UNABLE to play audio", e)
          }
	}

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
} 
