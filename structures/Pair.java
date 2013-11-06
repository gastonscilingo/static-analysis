package structures;

public class Pair<E,T>{

	private E e;
	private T t;
	
	public Pair(E e, T t){
		this.e = e;
		this.t = t;
	}
	
	
	public E getFst() {
		return e;
	}
	public void setFst(E e) {
		this.e = e;
	}
	public T getSnd() {
		return t;
	}
	public void setSnd(T t) {
		this.t = t;
	}


	@Override
	public String toString() {
		return "("+ e + "," +t+ ")";
	}
	
	
	
	
	
	
}
