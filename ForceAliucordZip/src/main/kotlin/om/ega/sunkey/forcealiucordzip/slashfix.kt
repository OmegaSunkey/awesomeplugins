package om.ega.sunkey.forcealiucordzip

import android.content.Context
import java.io.File
import java.nio.file.Files
import java.security.MessageDigest
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.Constants
import com.aliucord.Logger

@AliucordPlugin(requiresRestart = true)
class spanishbutton : Plugin() {
    override fun start(c: Context) {
    	val customzip = File(BASE_PATH, "Aliucord.zip")
	val currentzip = File(c.getCodeCacheDir(), "Aliucord.zip")
	if(customzip.md5() == currentzip.md5()) {
		Logger.debug("You are already using a custom build!")
	} else {
		Files.copy(customzip, currentzip, REPLACE_EXISTING)
		Logger.debug("Replaced Aliucord build with custom build!")
	}
    }
    override fun stop(c: Context) {
    	File(c.getCodeCacheDir(), "Aliucord.zip").delete()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun File.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.readBytes())
        return digest.toHexString()
    }
}
