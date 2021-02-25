package com.techelevator.NewChallengerBot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.NewChallengerBot.model.ChannelDAO;
import com.techelevator.NewChallengerBot.model.Match;
import com.techelevator.NewChallengerBot.model.MatchDAO;
import com.techelevator.NewChallengerBot.model.Player;
import com.techelevator.NewChallengerBot.model.PlayerDAO;
import com.techelevator.NewChallengerBot.model.Team;
import com.techelevator.NewChallengerBot.model.jdbc.JDBCChannelDAO;
import com.techelevator.NewChallengerBot.model.jdbc.JDBCMatchDAO;
import com.techelevator.NewChallengerBot.model.jdbc.JDBCPlayerDAO;
import com.techelevator.NewChallengerBot.utils.DuplicatePlayerException;
import com.techelevator.NewChallengerBot.utils.ImageUtils;
import com.techelevator.NewChallengerBot.utils.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class NewChallenger extends ListenerAdapter {
	public static PlayerDAO playerdao;
	public static MatchDAO matchdao;
	public static ChannelDAO channeldao;
	// Font for generated images
	private Font textFont = new Font("Felix Titling", Font.BOLD, 120);
	private static JDA jda;
	private List<Match> matches = new ArrayList<>();

	public static void main(String[] args) {
		String AUTH = null;
		File key = new File("AUTH.key");
		if(!key.exists()) {
			Scanner userInput = new Scanner(System.in);
			System.out.print("Please input your Discord Bot Token! ");
			AUTH = userInput.next();
			try (PrintWriter authWriter = new PrintWriter(key)){
				authWriter.println(AUTH);
			}catch(IOException ioEx) {
				System.err.println("Failure to write to AUTH.key uh oh");
			}
		}
		try{
			Scanner keyscan = new Scanner(key);
			if(keyscan.hasNext()) {
				AUTH = keyscan.nextLine();
			}else {
				System.err.println("Your AUTH.key file appears to be empty! please put your Discord Token!\n"
						+ "This can be found on the developer portal under bot");
			}
			keyscan.close();
		}catch(FileNotFoundException fnfEX) {
			System.err.println(fnfEX.getMessage());
			System.err.println("You don't have an AUTH.key file please create one!");
		}
		try {
			jda = JDABuilder.createDefault(AUTH)
					.addEventListeners(new NewChallenger()).setChunkingFilter(ChunkingFilter.ALL)
					.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS)
					.enableIntents(GatewayIntent.DIRECT_MESSAGES).setActivity(Activity.watching("Fight Club")).build();
			jda.awaitReady();
			System.out.println("Ready to fight!");
		} catch (LoginException e) {
			System.err.println("Invalid Discord Token!");
		} catch (InterruptedException e) {
			System.err.println("Connection Error?");
		}

	}

	public NewChallenger() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/NewChallenger");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		try {
			playerdao = new JDBCPlayerDAO(dataSource);
			matchdao = new JDBCMatchDAO(dataSource);
			channeldao = new JDBCChannelDAO(dataSource);
		} catch (Exception ex) {
			
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {


		if (event.getAuthor().isBot())
			return;
		
		MessageChannel channel = event.getChannel();
		
		Message message = event.getMessage();
		String content = message.getContentRaw();
		
		if(content.matches("!summon\\b.*")) {
			if (event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)
					|| event.getAuthor().getIdLong() == 78484810649051136l) {
				if(channeldao.isValid(channel.getIdLong())) {
					channel.sendMessage("The Tournament has already started here").queue();
				}else {
					channeldao.create(channel.getIdLong());
					channel.sendMessage("Welcome to my Tournament!").queue();
				}
			}
			return;
		}
		if(!channeldao.isValid(channel.getIdLong())) return;
		if(content.matches("!\\b.*")) {
			System.out.println(event.getAuthor().getName() +" : "+ content);
		}else {
			return;
		}
		if(content.matches("!banish\\b.*")) {
			if (event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)
					|| event.getAuthor().getIdLong() == 78484810649051136l) {
				channel.sendMessage("Granted").queue();
				channeldao.delete(channel.getIdLong());
			}else {
				channel.sendMessage("No").queue();
			}
		}else if (content.matches("!ping\\b.*")) {

			channel.sendMessage("Pong!").queue();

		} else if (content.matches("!match\\b.*")) {

			generateMatch(event);

		} else if (content.matches("!setImage\\b.*")) {

			if (event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)
					|| event.getAuthor().getIdLong() == 78484810649051136l) {
				LoadImage(event);
			} else {
				System.out.println("Illegal Command");
				channel.sendMessage("You are not allowed to use this command!").queue();
			}

		}else if (content.matches("!pic\\b.*")) {

			Player ME = playerdao.findPlayerById(event.getAuthor().getIdLong());
			channel.sendMessage("Hey you!").addFile(ME.getLogo(), "img.png").queue();

		} else if (content.matches("!winner\\b.*")) {
			String[] messageArgs = util.splitNoBlanks(content, " ");
			if (messageArgs.length == 1) {
				Player player = playerdao.findPlayerById(event.getAuthor().getIdLong());
				int match = findMyMatch(player);
				if (match >= 0 && matches.get(match).getWinner() == -1) {
					channel.sendMessage(matches.get(match).finishMatch(player)).queue();
					;
				}
			} else if (messageArgs.length == 2) {
				User user = getUserFromIDString(messageArgs[1]);
				if (user != null) {
					Player player = playerdao.findPlayerById(user.getIdLong());
					int match = findMyMatch(player);
					if (match >= 0 && matches.get(match).getWinner() == -1) {
						channel.sendMessage(matches.get(match).finishMatch(player)).queue();
					}
				} else if (messageArgs[1].matches("[0-9]+")) {
					int winner = Integer.valueOf(messageArgs[1])-1;
					Player player = playerdao.findPlayerById(event.getAuthor().getIdLong());
					int match = findMyMatch(player);
					if (winner < matches.get(match).getTeamAmount() && winner >= 0) {
						channel.sendMessage(matches.get(match).finishMatch(winner)).queue();
					} else {
						channel.sendMessage("Please Input a valid team number!").queue();
					}
				} else {
					int match = findMyMatch(messageArgs[1]);
					if (match >= 0 && matches.get(match).getWinner() == -1) {
						channel.sendMessage(matches.get(match).finishMatch(messageArgs[1])).queue();
					} else {
						channel.sendMessage(
								"Malformed !winner, please resubmit as !winner [teamnumber] or !winner [@playername]")
								.queue();
					}
				}
			} else {
				channel.sendMessage(
						"Malformed !winner, please resubmit as !winner [teamnumber] or !winner [@playername]").queue();
			}
		} else if (content.matches("!refresh\\b.*")) {
			int match = findMyMatch(playerdao.findPlayerById(event.getAuthor().getIdLong()));
			if (match >= 0) {
				channel.sendMessage("Pairing!").addFile(matches.get(match).getPairingImage(), "pairing.png").queue();
			} else {
				channel.sendMessage("You are not currently in a match").queue();
			}
		} else if (content.matches("!runback\\b.*")) {
			int match = findMyMatch(playerdao.findPlayerById(event.getAuthor().getIdLong()));
			if (match >= 0) {
				Match oldMatch = matches.get(match);
				if (oldMatch.getWinner() >= 0) {
					oldMatch.setWinner(-1);
					oldMatch.setMatchID(-1);
					channel.sendMessage("Pairing!").addFile(matches.get(match).getPairingImage(), "pairing.png")
							.queue();
				} else {
					channel.sendMessage("Your last match is still in progress please submit the results with !winner")
							.queue();
				}

			} else {
				channel.sendMessage(
						"Your last match could not be found, this can be caused by a fellow player already having played a new match")
						.queue();
			}
		} else if (content.matches("!allstats?\\b.*")) {
			allStats(event);
		} else if(content.matches("!optin")) {
			Player player = playerdao.findPlayerById(event.getAuthor().getIdLong());
			player.setOptin(!player.isOptin());
			if(player.isOptin()) {
				channel.sendMessage("You are now opted in to recieving DM's about matches you are int").queue();
			}else {
				channel.sendMessage("You are now opted out of recieving DM's about matches you are int").queue();
			}
			playerdao.update(player);
		}else if (content.matches("!stats?\\b.*")) {
		
	
			displayStats(event);
		}else if(content.matches("!addgame\\b.*")) {
		
		}else if(content.matches("!help\\b.*")) {
			channel.sendMessage("==============Commands==============\n"+
								"!match [name] vs [name] -[game] : creates a match with the players listed on either side of vs\n"+
								"!refresh : reprints the match image for the latest match you are in\n"+
								"!runback : Makes a new match with the same players as the last match you are in\n"+
								"!winner [name] or [number] declares the winner of the match [name] is in as that player's team or declares the team number of the game you are in as the winner"+
								"!setImage [@player] : -ADMIN ONLY- if you send this with a picture attached it will set the players match image\n"+
								"!pic : displays your match image\n"+
								"!stat : displays your current Win Loss data\n"+
								"!about : credits stuff"+
								"!summon : -ADMIN ONLY- allows me to respond to messages in the channel"+
								"!banish : -ADMIN ONLY- stops me from responding to messages in the channel"+
								"====================================").queue();;
		}else if(content.matches("!about\\b.*")) {
			channel.sendMessage("===============About===============\n"+
					"NewChallenger Bot by Skizzix\n"+
					"Version : 0.1.3 (Borderline Untested Edition)\n\n"+
					"NewChallenger generates pairings and keeps track of win loss\n"+
					"When generating matches NewChallenger uses player logos if available\n"+
					"Some upcoming features are splitting the win loss by game as well as\n"+
					"including a ranking system and per game logos\n")
					.queue();
		}else if (content.matches("!\\b.*")) {

			channel.sendMessage("Invalid Command try using !help").queue();
		}

	}

	public void allStats(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		String content = message.getContentRaw();
		List<long[]> WinLossPlayers = playerdao.getAllWinLoss();
		String stats = "_STATS_\n";
		for (long[] WinLoss : WinLossPlayers) {
			User user = jda.getUserById(WinLoss[3]);
			Player player = playerdao.findPlayerById(WinLoss[3]);
			String ratio = "" + ((WinLoss[1] != 0) ? ((float) WinLoss[0] / (float) WinLoss[1]) : "Inf");
			stats += ((player.getAlias() != null) ? player.getAlias() : user.getName()) + " Matches: " + WinLoss[2]
					+ " Wins: " + WinLoss[0] + " Losses: " + WinLoss[1] + " Ratio: " + ratio + "\n";
		}
		channel.sendMessage(stats).queue();
	}
	
	public void displayStats(MessageReceivedEvent event) {

		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		String content = message.getContentRaw();
		String[] messageArgs = util.splitNoBlanks(content, " ");
		if (messageArgs.length == 1) {
			Player player = playerdao.findPlayerById(event.getAuthor().getIdLong());
			int[] WinLoss = playerdao.getWinLoss(event.getAuthor().getIdLong());
			String ratio = "" + ((WinLoss[1] != 0) ? ((float) WinLoss[0] / (float) WinLoss[1]) : "Inf");
			channel.sendMessage(
					((player.getAlias() != null) ? player.getAlias() : event.getAuthor().getName()) + " Matches: "
							+ WinLoss[2] + " Wins: " + WinLoss[0] + " Losses: " + WinLoss[1] + " Ratio: " + ratio)
					.queue();
		} else if (messageArgs.length == 2) {
			User user = getUserFromIDString(messageArgs[1]);
			if (user != null) {
				Player player = playerdao.findPlayerById(user.getIdLong());
				int[] WinLoss = playerdao.getWinLoss(user.getIdLong());
				String ratio = "" + ((WinLoss[1] != 0) ? ((float) WinLoss[0] / (float) WinLoss[1]) : "Inf");
				channel.sendMessage(((player.getAlias() != null) ? player.getAlias() : user.getName())
						+ " Matches: " + WinLoss[2] + " Wins: " + WinLoss[0] + " Losses: " + WinLoss[1] + " Ratio: "
						+ ratio).queue();
			}
		}
	}
	
	public void generateMatch(MessageReceivedEvent event) {
		Match match = new Match();
		match.setNow();
		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		String content = message.getContentRaw();
		String[] messageArgs = util.splitNoBlanks(content, " ");

		List<Team> teams = new ArrayList<>();
		Team tempTeam = new Team();

		List<User> usersToPing = new ArrayList<>();
		int currentTeam = 0;
		for (int i = 1; i < messageArgs.length; i++) {
			if (messageArgs[i].matches("vs")) {
				// add the team to the list of teams
				teams.add(tempTeam);
				currentTeam++;
				// blank out the old teams
				tempTeam = new Team();
			} else {

				try {
					User user = getUserFromIDString(messageArgs[i]);
					Player player = null;
					if (user != null) {
						try {

							player = playerdao.findPlayerById(user.getIdLong());
							if (player != null) {
								RemovedMyFinishedMatch(playerdao.findPlayerById(user.getIdLong()));
								int currentmatch = findMyMatch(playerdao.findPlayerById(user.getIdLong()));
								if (currentmatch > -1 && matches.get(currentmatch).getWinner() == -1) {
									channel.sendMessage((getAlias(event, user))
											+ " is still in a match please submit the results with !winner").queue();
									return;
								}

								player.setName(user.getName());
								match.addPlayer(player, currentTeam);
							} else {
								player = new Player();
								player.setDiscord_id(user.getIdLong());

								player.setName(getAlias(event, user));
								player.setLogo( ImageUtils.createLogoFromText(player.getName(),textFont));
								try {
									playerdao.create(player);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								match.addPlayer(player, currentTeam);
							}
						} catch (DuplicatePlayerException ex) {
							System.out.println("Some Idiot tried adding a dupe");
						}
						usersToPing.add(user);
					} else {
						player = new Player();
						player.setName(messageArgs[i]);
						player.setLogo( ImageUtils.createLogoFromText(messageArgs[i],textFont));
						match.addPlayer(player, currentTeam);
					}
					if (player != null)
						tempTeam.addPlayer(player);
				} catch (DuplicatePlayerException ex) {
					System.out.println("Some Idiot tried adding a dupe");
				}

			}
		}
		teams.add(tempTeam);

		byte[] TeamImage = generateMatchImage(teams);
		match.setPairingImage(TeamImage);
		matches.add(match);
		channel.sendMessage("Pairings!").addFile(TeamImage, "img.png").queue();
		for (User user : usersToPing) {
			

			if (!user.isBot() && playerdao.canPing(user.getIdLong())) {
				System.out.println("dming " + user.getName());
				user.openPrivateChannel()
						.flatMap((privChan) -> privChan.sendMessage("Pairings!").addFile(TeamImage, "img.png")).queue();
			}
		}
	}

	public byte[] generateMatchImage(List<Team> teams) {
		final int HORIZONTAL_SPACING = 200;
		final int VERTICLE_OFFSET = 35;
		final int VERTICLE_SPACING = 15;
		// Get the largest Height and Width of the largest Name in all Teams
		int[] textSize = Team.getLargestTeamImage(teams);
		// Verticle space between lines based on height of Largest Name plus spacer
		int lineSpace = (textSize[1] + VERTICLE_SPACING);
		int height = lineSpace * (Team.getLargestTeamSize(teams)) + VERTICLE_OFFSET * 2;

		int biggestHeight = textSize[1];
		int width = 0;
		for (int i = 0; i < teams.size(); i++) {
			int[] TextSize2 = teams.get(i).getLargestImage();
			width += TextSize2[0] + HORIZONTAL_SPACING * ((i < teams.size() - 1 && i != 0) ? 2 : 1);
		}
		// width+=HORIZONTAL_SPACING/2;

		BufferedImage generatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = generatedImage.createGraphics();
		// g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2d.setFont(textFont);
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.WHITE);
		int xOffset = 0, lineNumber = 0;

		for (int i = 0; i < teams.size(); i++) {
			int biggestWidth = teams.get(i).getLargestImage()[0];
			for (Player player : teams.get(i).getPlayers()) {
				int x = HORIZONTAL_SPACING / 2 + xOffset + biggestWidth / 2;
				int y = VERTICLE_OFFSET + (lineSpace * lineNumber) + biggestHeight / 2;
				try {
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(player.getLogo()));
					// ImageIO.write(bi,"png", new File("test.png"));

					g2d.drawImage(bi, x - bi.getWidth() / 2, y - bi.getHeight() / 2, null);

				} catch (Exception Ex) {
					Ex.printStackTrace();
				}
				lineNumber++;
			}
			lineNumber = 0;
			int xspace = (biggestWidth + HORIZONTAL_SPACING);

			xOffset += (xspace);
			if (i < teams.size() - 1) {
				BufferedImage vs = ImageUtils.createImageFromText("vs",textFont);

				g2d.drawImage(vs, xOffset - HORIZONTAL_SPACING + vs.getWidth() / 2, height / 2 - vs.getHeight() / 2,
						null);
			}
		}

		// Create Output stream to store image
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			// Write image to stream
			ImageIO.write(generatedImage, "png", bytes);
		} catch (IOException ex) {
			System.out.println("We have failed to write to a variable in our program \n this should never happen");
		}
		// returns the Output stream as an array of bytes (discord wants this)
		return bytes.toByteArray();
	}

	public void LoadImage(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		String content = message.getContentRaw();
		System.out.println("Setting Image");
		List<Attachment> attachments = message.getAttachments();
		System.out.println(attachments.isEmpty());
		System.out.println(attachments.get(0).isImage());
		if (attachments.isEmpty())
			return;
		if (!attachments.get(0).isImage())
			return;
		String[] messageArgs = util.splitNoBlanks(content, " ");
		System.out.println(messageArgs[1]);
		if (messageArgs.length > 1) {
			System.out.println(messageArgs[1]);
			User user = getUserFromIDString(messageArgs[1]);
			if (user != null) {
				try {
					InputStream is = attachments.get(0).retrieveInputStream().get();
					BufferedImage buff = ImageIO.read(new ByteArrayInputStream(is.readAllBytes()));
					buff = ImageUtils.imageResize(buff, 600, 300);
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					try {
						ImageIO.write(buff, "png", bytes);
					} catch (IOException ex) {
						System.out.println(
								"We have failed to write to a variable in our program \n this should never happen");
					}
					byte[] imageBytes = bytes.toByteArray();
					Player newPlayer = new Player();
					newPlayer.setDiscord_id(user.getIdLong());
					newPlayer.setAlias(event.getGuild().getMember(user).getNickname());
					newPlayer.setLogo(imageBytes);
					if (playerdao.playerExists(newPlayer.getDiscord_id())) {
						playerdao.update(newPlayer);
					} else {
						playerdao.create(newPlayer);
					}
					channel.sendMessage("PONG!").addFile(imageBytes, "img.png").queue();
				} catch (IOException ex) {
					System.out.println("oh no! well anyways");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				channel.sendMessage("Invalid User Please @ them").queue();
			}
		} else {
			channel.sendMessage("You must specify a user").queue();
		}
	}

	public String getAlias(MessageReceivedEvent event, User user) {
		String nickname = event.getGuild().getMember(user).getNickname();
		return (nickname != null) ? nickname : user.getName();
	}
	
	public User getUserFromIDString(String id) {
		if (id.contains("@!")) {
			long userID = Long.valueOf(id.substring(3, id.length() - 1));
			return jda.getUserById(userID);
		}
		return null;
	}

	public void RemovedMyFinishedMatch(Player player) {
		if (player != null) {
			for (int i = 0; i < matches.size(); i++) {
				if (matches.get(i).matchContainsPlayer(player) >= 0 && matches.get(i).getWinner() >= 0) {
					matches.remove(i);
					return;
				}
			}
		}

	}

	public int findMyMatch(String player) {
		if (player != null) {
			for (int i = 0; i < matches.size(); i++) {
				if (matches.get(i).matchContainsName(player) >= 0 && matches.get(i).getWinner() == -1) {
					return i;
				}
			}
		}
		return -1;
	}

	public int findMyMatch(Player player) {
		if (player != null) {
			for (int i = 0; i < matches.size(); i++) {
				if (matches.get(i).matchContainsPlayer(player) >= 0) {
					return i;
				}
			}
		}
		return -1;
	}
}
