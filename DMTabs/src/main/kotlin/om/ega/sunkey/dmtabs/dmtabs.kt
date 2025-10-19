package om.ega.sunkey.dmtabs

import android.content.Context
import android.view.View

import com.discord.widgets.tabs.`TabsHostBottomNavigationView$updateView$5`
import com.discord.stores.StoreStream

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Logger
import com.aliucord.api.PatcherAPI
import com.aliucord.patcher.*
import com.aliucord.wrappers.ChannelWrapper

@AliucordPlugin
class DMTabs : Plugin() {
    val Logger = Logger("DMTabs")
    override fun start(c: Context) {
    	patcher.before<`TabsHostBottomNavigationView$updateView$5`>("onClick", View::class.java) {
		StoreStream.getGuildSelected().set(0L)
		it.result = null
	}
    }
    override fun stop(c: Context) {
	patcher.unpatchAll() 
    }
}
