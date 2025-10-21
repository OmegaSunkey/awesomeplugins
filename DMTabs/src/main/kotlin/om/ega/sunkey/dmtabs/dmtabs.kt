package om.ega.sunkey.dmtabs

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager

import com.discord.widgets.guilds.list.GuildListItem
import com.discord.widgets.guilds.list.WidgetGuildsListViewModel
import com.discord.widgets.tabs.`TabsHostBottomNavigationView$updateView$5`
import com.discord.widgets.tabs.`TabsHostBottomNavigationView$updateView$4`
import com.discord.widgets.tabs.NavigationTab
import com.discord.stores.StoreStream
import com.discord.stores.StoreChannelsSelected
import com.discord.stores.StoreTabsNavigation
import com.discord.api.channel.Channel

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Logger
import com.aliucord.api.PatcherAPI
import com.aliucord.patcher.*

@AliucordPlugin
class DMTabs : Plugin() {
    val Logger = Logger("DMTabs")
    var lastchannel: Long = 0L
    var shouldchangechannels = false
    override fun start(c: Context) {
	patcher.before<`TabsHostBottomNavigationView$updateView$5`>("onClick", View::class.java) {
		lastchannel = StoreChannelsSelected.`access$getSelectedChannel$p`(StoreStream.getChannelsSelected()).getId()
		shouldchangechannels = true
		if(StoreStream.Companion!!.getTabsNavigation().getSelectedTab() != NavigationTab.HOME) StoreStream.Companion!!.getTabsNavigation().selectTab(NavigationTab.HOME, false)
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
	patcher.before<`TabsHostBottomNavigationView$updateView$4`>("onClick", View::class.java) {
		if(shouldchangechannels) {
			StoreStream.getMessagesLoader().jumpToMessage(lastchannel, 1L)
			shouldchangechannels = false
		}
	}
    }
    override fun stop(c: Context) {
	patcher.unpatchAll() 
    }
}
