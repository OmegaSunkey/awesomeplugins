package om.ega.sunkey.DDGIS

import android.content.Context
import com.aliucord.Utils
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.CommandContext
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.aliucord.patcher.*

@AliucordPlugin(requiresRestart = false)
class DDGIS : Plugin() {
    override fun start(context: Context) {
	commands.registerCommand("DDGIS", "Image Search", commandoptions) {
		CommandsAPI.CommandResult(ddg)
	}
    }

val commandoptions = listOf(
	Utils.createCommandOption(
		ApplicationCommandType.STRING, "search", "you can search", null,
		required = true,
		default = true,
		channelTypes = emptyList(),
		choices = emptyList(),
		subCommandOptions = emptyList(),
		autocomplete = false
	)
)

val ddg = "https://cdn.discordapp.com/attachments/781652296710881311/907492395623518248/Finance_Kabedon_-_Nijisanji_EN_Animation1080P_HD.mp4"

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
} 
