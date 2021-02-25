# NewChallengerBot
New challenger bot is a simple discord bot for creating vs matches between players and displaying a fancy image for the match! Each player can have a custom log set for themselves that will display in the match image.

New Challenger also records Wins and Losses for those matches and allow the user to look at their current stats!

Commands 
```
!summon : -ADMIN ONLY- allows the bot to respond to messages in the channel this command is used in

!banish : -ADMIN ONLY- allows the bot to respond to messages in the channel this command is used in

!setImage [@name] : -ADMIN ONLY- sets a user's logo if you have an image attached to the message

!pic : Displays your current logo

!match [@name] vs [@name] : Allows users to create a match image, there can be any number of users on either side of the vs as well as multiple vs to create several teams

!refresh : reposts the latest match that player is currently in

!runback : makes a new match with the same players as the last match you were in

!winner [@name] or [@number] or no Args : declares the winner of a match as the @name player or as the team number entered, if no arguments are used it declare the winner as you!

!stats [@name] : displays the stats of the named player or you if no arguments are used.

!allstats : displays the stats for everyone!

!about : Credits

```

# SETUP

Download the latest build here!

Setup should be reasonably simple for create a PostgreSQL database with the name NewChallenger on the machine you are running this bot on, and run the included sql file located in the SQL folder.

For Windows Run NewChallenger.bat

For Linux Run NewChallenger.sh

For Mac Run NewChallenger.command ... maybe I have no way to test this let me know if it works.

The first time you start the program the bot should prompt you for the Discord Bot Token, plop that in there and you should be off to the races.

# Future

My intention for this to slowly add more features such as an Elo system of some type as well as a distinction between different games, and maybe some tools for randomly selecting maps.
