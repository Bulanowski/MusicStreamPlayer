package model;

import java.util.EventObject;

public class PackageReceivedEvent extends EventObject {

	private static final long serialVersionUID = 5645758205320464197L;
	private byte packageType;
	private Object information;

	public PackageReceivedEvent(Object source) {
		super(source);
	}

	public PackageReceivedEvent(Object source, byte packageType, Object information) {
		super(source);
		this.packageType = packageType;
		this.information = information;
	}

	public byte getPackageType() {
		return packageType;
	}

	public Object getInformation() {
		return information;
	}

}
