package om.ega.sunkey.Ads

import android.content.Context
import android.app.ActivityManager

import com.aliucord.Utils
import com.aliucord.Logger
import com.aliucord.api.CommandsAPI
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.Http
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.wrappers.embeds.MessageEmbedWrapper.Companion.title
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*

import java.net.URLEncoder
import java.util.regex.Pattern
import java.util.regex.Matcher

import com.discord.api.commands.ApplicationCommandType
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import com.discord.widgets.chat.list.entries.ChatListEntry
import com.discord.widgets.chat.list.entries.MessageEntry

@AliucordPlugin(requiresRestart = false)
class Ads : Plugin() {
    init {
	settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }
    override fun start(context: Context) {	
	val LOG = Logger("Ads")
	if(!settings.getBool("registered", false)) {
		val MemInfo = ActivityManager.MemoryInfo()
		(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(MemInfo)
		val unixepoch = System.currentTimeMillis()
		val uid = MemInfo.availMem + unixepoch
		settings.setString("uid", uid.toString())
		//LOG.debug(uid.toString())
		try {
			Http.simpleGet("https://aliucord-ads.gdspikes.workers.dev/register?uid=${uid}")
			settings.setBool("registered", true)
		} catch(e) {
			LOG.error("Couldn't register !! WTF!?")
		}
	}
	
	val adlist = mutableListOf<String>()
	val AdsJSON = Http.simpleGet("https://aliucord-ads.gdspikes.workers.dev/getAd")
	val admatch = Pattern.compile("ad\":\"\\(.*?\\)\"").matcher(AdsJSON)
	while(admatch.find()) {
		adlist.add(admatch.group())
	}

	patcher.after<WidgetChatListAdapterItemMessage>(
		"onConfigure",
		Int::class.java,
		ChatListEntry::class.java
	) { p ->
		val entry = p.args[1] as MessageEntry
		val message = entry.message
		if (message.isLoading) return@after
		message.embeds.removeAll {
			it.title == "AD"
		}

		MessageEmbedBuilder().run {
			setTitle("AD")
			addField("Ad served from our customers", adlist.random(), false)
			message.embeds.add(build())
		}
	}

        commands.registerCommand("newad", "Insert a new ad for all Aliucord users to see", commandoptions) {
		val adcontent = it.getString("content")
		val encodedad = URLEncoder.encode(adcontent)
		Http.simpleGet("https://aliucord-ads.gdspikes.workers.dev/newAd?ad=${encodedad}")
		//val LOG: Logger = Logger("FC")
		//LOG.debug(keyw)
		//val copypasta = "if `${keyw}` has a million fans im one of them \nif `${keyw}` has 0 fans then I am no more \nif `${keyw}` has 1 fan that fan is me \nif the world is against `${keyw}` then I'm against the world"
		return@registerCommand CommandResult("Your ad has been submitted! Aliucord users will now start seeing this ad.")
	}
   }

val commandoptions = listOf(
	Utils.createCommandOption(
		ApplicationCommandType.STRING, "content", "Buy an ad for 1 aliutoken, and send to all Aliucord users any awesome info you have!", null,
		required = true,
		default = true,
		channelTypes = emptyList(),
		choices = emptyList(),
		subCommandOptions = emptyList(),
		autocomplete = false
	)
)

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
} 
