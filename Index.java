import java.util.ArrayList;

public class Index {

	private int count;
	private ArrayList<Integer> lines = new ArrayList<>();
	
	public Index(){
	}
	
	public Index(int count, ArrayList<Integer> lines) {
		this.lines = lines;
		this.count = count;
	}
	
	public int getCount() {
	 return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public ArrayList<Integer> getLines(){
		
		return this.lines;
	}

	
	public void setLines(ArrayList<Integer> lines) {
		this.lines = lines;
	}
	
	public String toString() {
		String dataIndex = new String();
		dataIndex = count + " ";
		for(int line: lines) {
			dataIndex += line + " "; 
		}
		dataIndex += "\n";
		return dataIndex;
	}
}
