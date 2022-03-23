package om.ega.sunkey.useerbg

import android.content.Context
import com.aliucord.Logger
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import om.ega.sunkey.useerbg.model.AbstractDatabase
import om.ega.sunkey.useerbg.model.USRBG
import kotlin.Throws

@AliucordPlugin
class usrbg : Plugin() {
    init {
        USRBG = om.ega.sunkey.useerbg.model.USRBG
    }
    @Throws(NoSuchMethodException::class)
    override fun start(ctx: Context){ 
	USRBG.init(ctx, settings, patcher)
    }

    override fun stop(ctx: Context) {
        patcher.unpatchAll()
    }

    companion object {
        lateinit var USRBG: USRBG

        val log: Logger = Logger("UsrBG")
        const val REFRESH_CACHE_TIME = (6 * 60).toLong()
    }
    
    init {
     settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }
}
