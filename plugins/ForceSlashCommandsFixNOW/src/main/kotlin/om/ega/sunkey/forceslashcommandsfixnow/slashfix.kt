package om.ega.sunkey.forceslashcommandsfixnow

import android.content.Context
import java.io.File
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Http
import com.aliucord.Utils

@AliucordPlugin(requiresRestart = true)
class spanishbutton : Plugin() {
    override fun start(c: Context) {
    	Utils.threadPool.execute { 
		Http.simpleDownload("https://github.com/OmegaSunkey/awesomeplugins/raw/main/Aliucord.zip", File(c.getCodeCacheDir(), "Aliucord.zip"))
	}
    }
    override fun stop(c: Context) {
    	File(c.getCodeCacheDir(), "Aliucord.zip").delete()
    }
}
