package org.aml.typesystem;

import java.util.ArrayList;

public class Status {

	public static final int CODE_CONFLICTING_TYPE_KIND = 4;

	public static final int ERROR = 3;

	public static final int INFO = 1;

	public static final int INSTANCEOF_FAILED = 5;
	public static final int NOTHING_CAN_PASS = 6;

	public static final int OK = 0;
	public static final Status OK_STATUS = new Status(OK, 0, "");
	public static final int RESTRICTIONS_CONFLICT = 55343;

	public static final int WARNING = 2;

	protected int code;
	protected String message;
	protected int severity;
	protected Object source;

	protected ArrayList<Status> subStatus = new ArrayList<>();

	public Status(int severity, int code, String message) {
		super();
		this.severity = severity;
		this.code = code;
		this.message = message;
	}

	public void addSubStatus(Status st) {
		this.subStatus.add(st);
		if (this.severity < st.severity) {
			this.severity = st.severity;
			this.message = st.message;
		}
	}

	public String getMessage() {
		return this.message;
	}

	public Object getSource() {
		return source;
	}

	public boolean isOk() {
		return this.severity == OK;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public String toString() {
		if (this.isOk()) {
			return "OK";
		}
		return this.message;
	}
}