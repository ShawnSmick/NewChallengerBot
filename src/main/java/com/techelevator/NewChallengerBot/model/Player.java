package com.techelevator.NewChallengerBot.model;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Player {
	private long id = -1;
	private long discord_id=-1;
	private String name;
	private String alias;
	private byte[] logo;
	private boolean optin = false;
	List<Object> matches; // TODO make Match object;

	@Override
	public String toString() {
		return "#" + id + " DiscordID: " + discord_id + " " + alias;
	}

	@Override
	public boolean equals(Object obj) {
		Player other = (Player) obj;
		if (this.discord_id == other.discord_id) {
			return true;
		}
		return false;

	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDiscord_id() {
		return discord_id;
	}

	public void setDiscord_id(long discord_id) {
		this.discord_id = discord_id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public boolean isOptin() {
		return optin;
	}

	public void setOptin(boolean optin) {
		this.optin = optin;
	}
	public int[] getLogoSize() {
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(this.getLogo()));
			Rectangle2D bounds = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
			int tempWidth = bi.getWidth();
			int tempHeight = bi.getHeight();
			return new int[] { tempWidth, tempHeight };
		} catch (IOException ex) {
			System.err.println("Recreate Image from Bytestream for player "+this.getDiscord_id());
			return null;
		}
	}
}
