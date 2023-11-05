package parte2.LockBakery;

public class MiEntero {
    private volatile int valor;
	
	public MiEntero(int n) {
		this.valor = n;
	}
	
	public void sum(int n) {
		this.valor += n;
	}
	
	public int getValue() {
		return valor;
	}
	
	public void setValue(int i) {
		this.valor = i;
	}
}
