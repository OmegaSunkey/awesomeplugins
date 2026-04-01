package om.ega.sunkey.userpfp

import android.content.Context
import com.aliucord.Logger
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import om.ega.sunkey.userpfp.model.APFP
import om.ega.sunkey.userpfp.model.AbstractDatabase
import kotlin.Throws

@AliucordPlugin
class UserPFP : Plugin() {
    init {
        APFP = om.ega.sunkey.userpfp.model.APFP
    }
    @Throws(NoSuchMethodException::class)
    override fun start(ctx: Context) {
        APFP.init(settings, patcher)
    }

    override fun stop(ctx: Context) {
        patcher.unpatchAll()
    }

    companion object {
        lateinit var APFP: APFP

        val log: Logger = Logger("UserPFP")
        const val REFRESH_CACHE_TIME = (1 * 60).toLong()
    }

    init {
        settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }
}
