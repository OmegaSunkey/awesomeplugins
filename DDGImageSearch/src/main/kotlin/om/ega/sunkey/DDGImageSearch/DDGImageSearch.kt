package om.ega.sunkey.DDGImageSearch

import android.content.Context
import com.aliucord.Utils
import com.aliucord.Http
import com.aliucord.Logger
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.CommandContext
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.aliucord.patcher.*
import java.util.regex.Pattern
import java.util.concurrent.TimeUnit

@AliucordPlugin(requiresRestart = false)
class DDGImageSearch : Plugin() {
    override fun start(context: Context) {
	commands.registerCommand("DDGImageSearch", "Image Search", commandoptions) {
		val keyw = it.getCommandArgs("DDGImageSearch")?.get("search").toString()
		val ddg = searchi(keyw)
		return@registerCommand CommandResult(ddg)
	}
    }

val logger = Logger("DDG")

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

fun searchi(keyword: String) {
	val url = "https://duckduckgo.com"
	val params = {
		"q" = keyword
	}
	DDG.logger.debug("hitting ddg for token")
	//make a req for token then grab it for results
	val res = Http.simplePost(url, params)
	val urlPattern = Pattern.compile("r'vqd=([\\d-]+)\\&'")
	val searchObj = urlPattern.matcher(res)

	if (searchObj == null) return

	DDG.logger.debug("token!!!")

/*	val headers = {
		'authority' = 'duckduckgo.com',
		'accept' = 'application/json, text/javascript; q=0.01',
		'sec-fetch-dest' = 'empty',
		'x-requested-with' = 'XMLHttpRequest',
		'user-agent' = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36',
		'sec-fetch-site' = 'same-origin',
		'sec-fetch-mode' = 'cors',
		'referer' = 'https://duckduckgo.com',
		'accept-language' = 'en-US,en;q=0.9',
	}*/
	
	val params = {
		('l', 'us-en');
		('o', 'json');
		('q', keyword);
		('vqd', searchObj.group(1));
		('f', ',,,');
		('p', '1');
		('v7exp', 'a');
	}
	val requestUrl = url + "i.js"

	DDG.logger.debug("Hitting url : %s", requestUrl)

	while(True) {
		while(True) {
			try {
				val res = Http.simpleJsonGet(requestUrl, params)
				//val data = "" i need to get res.text i think ill use gson
				DDG.logger.debug(res)
				break
			}
			
			catch(Throwable) {
				DDG.logger.debug("Fail")
				TimeUnit.SECONDS.sleep(5)
				continue //i need to add a timer
			}
		}
		DDG.logger.debug("Success")
		val result = res["results"]

		/* if(/* next not in data */ false) {
			logger.debug("No pages, finish")
			//exit(0)
		}*/

		val requestUrl = url + res["next"]
	}
	return result
}

/*	fun printres(/*objs*/) {
		for /* obj in objs */ {
			/* wait why im porting print lmao */
		}
	}*/


    override fun stop(context: Context) {
        commands.unregisterAll()
    }
} 
