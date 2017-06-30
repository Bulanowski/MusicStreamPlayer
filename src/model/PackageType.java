package model;

public enum PackageType {
	COMMAND(Byte.valueOf("0")), SONG_LIST(Byte.valueOf("1")), SONG(Byte.valueOf("2")), CHAT(Byte.valueOf("3"));

	private final byte type;

	PackageType(Byte type) {
		this.type = type;
	}

	public byte getByte() {
		return type;
	}
}
