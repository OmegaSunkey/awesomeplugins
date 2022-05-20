package om.ega.sunkey.SafeBooru

import android.content.Context
import com.aliucord.Utils
import com.aliucord.Logger
import com.aliucord.api.CommandsAPI
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.Http

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*

import java.util.regex.Pattern

import com.discord.api.commands.ApplicationCommandType

@AliucordPlugin(requiresRestart = false)
class SafeBooru : Plugin() {
    override fun start(context: Context) {
        commands.registerCommand("SafeBooru", "Search images in safebooru", commandoptions) {
		val keyw = it.getString("tag") 
		var number = it.getString("number")?.toInt()
		//val LOG: Logger = Logger("FC")
		val search = Http.simpleGet("https://safebooru.org/index.php?page=dapi&s=post&q=index&limit=1&pid=${number}&tags=${keyw}")
		
		var end = ""
		
		val result = search.toString()
		val matcher = Pattern.compile("file_url=\"(https:\\/\\/[\\w.\\/-]*)\"").matcher(result)
		if (matcher.find()) { 
		matcher.group(1)?.let { res -> end = res }
		} else { end = "no result" }
		return@registerCommand CommandResult(end)
	}
   }

val commandoptions = listOf(
	Utils.createCommandOption(
		ApplicationCommandType.STRING, "tag", "PLEASE insert the tag you want to search.", null,
		required = true,
		default = true,
		channelTypes = emptyList(),
		choices = emptyList(),
		subCommandOptions = emptyList(),
		autocomplete = false
	),
	Utils.createCommandOption(
		ApplicationCommandType.STRING, "number", "Insert the number of the image you want to send.", null,
		required = true,
		default = false,
		channelTypes = emptyList(),
		choices = emptyList(),
		subCommandOptions = emptyList(),
		autocomplete = false
	)
)

	/* fun booru(keywt: String) {
		val search = Http.simpleGet("https://safebooru.org/index.php?page=dapi&s=post&q=index&limit=1&tags=${keywt}")
		//LOG.debug(search)
		val r = search.toString()
	} */

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
} 
