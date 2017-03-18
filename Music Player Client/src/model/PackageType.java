package model;

public enum PackageType {
	SONG_LIST(Byte.valueOf("1")), BUFFER_SIZE(Byte.valueOf("2")), AUDIO(Byte.valueOf("3"));

	private byte type;

	PackageType(Byte type) {
		this.type = type;
	}

	public byte getByte() {
		return type;
	}
}
