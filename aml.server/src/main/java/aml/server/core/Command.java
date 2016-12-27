package aml.server.core;

public final class Command {
	
	public final CommandKind kind;	
	public final String targetEntityId;
	public final String targetPropertyId;
	public final Object targetValue;
	
	public Command(CommandKind kind, String targetEntityId, String targetPropertyId, Object targetValue) {
		super();
		this.kind = kind;
		this.targetEntityId = targetEntityId;
		this.targetPropertyId = targetPropertyId;
		this.targetValue = targetValue;
	}
}
