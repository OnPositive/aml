package org.aml.typesystem;

import java.util.ArrayList;

/**
 * <p>Status class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Status {

	/** Constant <code>CODE_CONFLICTING_TYPE_KIND=4</code> */
	public static final int CODE_CONFLICTING_TYPE_KIND = 4;

	/** Constant <code>ERROR=3</code> */
	public static final int ERROR = 3;

	/** Constant <code>INFO=1</code> */
	public static final int INFO = 1;

	/** Constant <code>INSTANCEOF_FAILED=5</code> */
	public static final int INSTANCEOF_FAILED = 5;
	/** Constant <code>NOTHING_CAN_PASS=6</code> */
	public static final int NOTHING_CAN_PASS = 6;

	/** Constant <code>OK=0</code> */
	public static final int OK = 0;
	/** Constant <code>OK_STATUS</code> */
	public static final Status OK_STATUS = new Status(OK, 0, "");
	/** Constant <code>RESTRICTIONS_CONFLICT=55343</code> */
	public static final int RESTRICTIONS_CONFLICT = 55343;

	/** Constant <code>WARNING=2</code> */
	public static final int WARNING = 2;

	protected int code;
	protected String message;
	protected int severity;
	protected Object source;

	protected ArrayList<Status> subStatus = new ArrayList<>();

	/**
	 * <p>Constructor for Status.</p>
	 *
	 * @param severity a int.
	 * @param code a int.
	 * @param message a {@link java.lang.String} object.
	 */
	public Status(int severity, int code, String message) {
		super();
		this.severity = severity;
		this.code = code;
		this.message = message;
	}

	/**
	 * <p>addSubStatus.</p>
	 *
	 * @param st a {@link org.aml.typesystem.Status} object.
	 */
	public void addSubStatus(Status st) {
		this.subStatus.add(st);
		if (this.severity < st.severity) {
			this.severity = st.severity;
			this.message = st.message;
		}
	}

	/**
	 * <p>Getter for the field <code>message</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * <p>Getter for the field <code>source</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * <p>isOk.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isOk() {
		return this.severity == OK;
	}

	/**
	 * <p>Setter for the field <code>source</code>.</p>
	 *
	 * @param source a {@link java.lang.Object} object.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (this.isOk()) {
			return "OK";
		}
		return this.message;
	}
}
