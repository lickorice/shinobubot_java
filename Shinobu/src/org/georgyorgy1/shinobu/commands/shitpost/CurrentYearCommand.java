package org.georgyorgy1.shinobu.commands.shitpost;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class CurrentYearCommand extends Command
{
    public CurrentYearCommand()
    {
        this.name = "currentyear";
        this.help = "I can't believe you're doing that in the current year";
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        String args = ce.getArgs();
        ce.reply(">" + args + " in the current year.");
    }
}
