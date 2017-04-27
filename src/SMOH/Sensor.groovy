package SMOH

class Sensor extends HBase {
	public String caption_local;
	
	public Sensor(String caption)
	{
		super(caption);
	}
	
	public <T> void connect(Class<T> type, T obj)
	{		
		//type.cast(obj.caption);
		//println obj.caption;
		//println input.cast(caption);
		//String sen = (MainNode)input.;
		//println sen;
	}
}
