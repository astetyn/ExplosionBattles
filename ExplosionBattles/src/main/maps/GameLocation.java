package main.maps;

public enum GameLocation {

	LOBBY ("lobby"),
	SPECTATOR ("spec");
	
	private final String str;
	
	private GameLocation(String s) {
        str = s;
    }

	public String getStr() {
		return str;
	}
}
