package om.ega.sunkey.FanCopypasta

import android.content.Context
import com.aliucord.Utils
import com.aliucord.api.CommandsAPI
import com.aliucord.api.CommandsAPI.CommandResult

import com.aliucord.entities.CommandContext
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.discord.api.commands.ApplicationCommandType

@AliucordPlugin(requiresRestart = false)
class FanCopypasta : Plugin() {
    override fun start(context: Context) {
        commands.registerCommand("FanCopypasta", "Funny copypasta with your own word", commandoptions) {
		val keyw = it.getRequiredSubCommandArgs("word")?.toString()
		val copypasta = "if `${keyw}` has a million fans im one of them \nif `${keyw}` has 0 fans then I am no more \nif `${keyw}` has 1 fan that fan is me \nif the world is against `${keyw}` then I'm against the world"
		return@registerCommand CommandResult(copypasta)
	}
   }

val commandoptions = listOf(
	Utils.createCommandOption(
		ApplicationCommandType.STRING, "word", "PLEASE insert the word you want to use in", null,
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
