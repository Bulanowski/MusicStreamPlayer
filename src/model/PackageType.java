package model;

public enum PackageType {
	COMMAND(Byte.valueOf("0")), SONG_LIST(Byte.valueOf("1")), SONG_INFO(Byte.valueOf("2")), AUDIO(Byte.valueOf("3")), CHAT(Byte.valueOf("4"));

	private byte type;

	PackageType(Byte type) {
		this.type = type;
	}

	public byte getByte() {
		return type;
	}
}
