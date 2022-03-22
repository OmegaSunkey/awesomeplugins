package om.ega.sunkey.userbg

import android.content.Context
import com.aliucord.Logger
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import om.ega.sunkey.userbg.model.APFP
import om.ega.sunkey.userbg.model.AbstractDatabase
import om.ega.sunkey.userbg.model.USRBG
import kotlin.Throws

@AliucordPlugin
class UserBG : Plugin() {
    init {
        USRBG = om.ega.sunkey.userbg.model.USRBG
        APFP = om.ega.sunkey.userbg.model.APFP
    }
    @Throws(NoSuchMethodException::class)
    override fun start(ctx: Context){ 
	USRBG.init(ctx, settings, patcher)
        APFP.init(ctx, settings, patcher)
    }

    override fun stop(ctx: Context) {
        patcher.unpatchAll()
    }

    companion object {
        lateinit var USRBG: USRBG
        lateinit var APFP: APFP

        val log: Logger = Logger("UserBG")
        const val REFRESH_CACHE_TIME = (6 * 60).toLong()
    }
    
    init {
     settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }
}
