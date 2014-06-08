package at.tuwien.sentimentanalyzer.entities.reddit;


public class Children {
	private Data data;
   	private String kind;
  
	public Data getData(){
		return this.data;
	}
	public void setData(Data data){
		this.data = data;
	}
 	public String getKind(){
		return this.kind;
	}
	public void setKind(String kind){
		this.kind = kind;
	}
	
}
