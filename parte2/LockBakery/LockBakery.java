package parte2.LockBakery;

import java.util.ArrayList;
import java.util.List;

public class LockBakery {
    private List<MiEntero> turns;

	public LockBakery() {
		this.turns = new ArrayList<MiEntero>();
	}

    public void reservarSitio(MiEntero n){
        turns.add(n);
    }

	public MiEntero getMax(List<MiEntero> turns) {
			
			MiEntero max = new MiEntero(0);
			
			for (int i = 0; i < turns.size(); i++) {
				if (turns.get(i).getValue() > max.getValue()) {
					max.setValue(turns.get(i).getValue());
				}
			}
			return max;
	}
	
	
	public boolean cmp(int turn1, int n1, int turn2, int n2) {
		return turn1 > turn2 || (turn1 == turn2 && n1 > n2);
	}
	
	public void bakeryEnter(int i) {
        turns.set(i,new MiEntero(1));
		turns.set(i,new MiEntero(getMax(turns).getValue() + 1));
		for (int j = 0; j < turns.size(); ++j) {
			if (j != i) 
				while (turns.get(j).getValue() != 0 && 
				cmp(turns.get(i).getValue(),i,turns.get(j).getValue(),j));
		}
	}
	public void bakeryRelease(int i) {
        turns.set(i,new MiEntero(0));
	}
}
