package om.ega.sunkey.dmtabs

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager

import com.discord.widgets.guilds.list.GuildListItem
import com.discord.widgets.guilds.list.WidgetGuildsListViewModel
import com.discord.widgets.tabs.`TabsHostBottomNavigationView$updateView$5`
import com.discord.widgets.tabs.NavigationTab
import com.discord.stores.StoreStream
import com.discord.stores.StoreTabsNavigation

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Logger
import com.aliucord.api.PatcherAPI
import com.aliucord.patcher.*

@AliucordPlugin
class DMTabs : Plugin() {
    val Logger = Logger("DMTabs")
    override fun start(c: Context) {
	patcher.before<`TabsHostBottomNavigationView$updateView$5`>("onClick", View::class.java) {
		if(StoreStream.Companion!!.getTabsNavigation().getSelectedTab() == NavigationTab.FRIENDS) StoreStream.Companion!!.getTabsNavigation().selectTab(NavigationTab.HOME, false)
		StoreStream.getGuildSelected().set(0L)
		it.result = null	
	}	
	patcher.patch(
		WidgetGuildsListViewModel::class.java.getDeclaredMethod(
			"onItemClicked",
			GuildListItem::class.java,
			Context::class.java,
			FragmentManager::class.java
		), PreHook {
			if(it.args[0] is GuildListItem.FriendsItem) {	
				StoreStream.Companion!!.getTabsNavigation().selectTab(NavigationTab.FRIENDS, false)
				it.result = null
			} else return@PreHook
		}
	)	
    }
    override fun stop(c: Context) {
	patcher.unpatchAll() 
    }
}
