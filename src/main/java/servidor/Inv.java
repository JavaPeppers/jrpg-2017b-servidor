package servidor;

public class Inv {
	private int idInventario;
	private int manos1;
	private int manos2;
	private int pie;
	private int cabeza;
	private int pecho;
	private int accesorio;
	
	public Inv() {}
	
	public Inv(final int idInventario) {
        this.idInventario = idInventario;
        this.pie = -1;
        this.cabeza = -1;
        this.pecho = -1;
        this.accesorio = -1;
        this.manos1 = -1;
        this.manos2 = -1;       
    }
	
	public Inv(int idInventario, int manos1, int manos2, int pie, int cabeza, int pecho, int accesorio) {
		super();
		this.idInventario = idInventario;
		this.manos1 = manos1;
		this.manos2 = manos2;
		this.pie = pie;
		this.cabeza = cabeza;
		this.pecho = pecho;
		this.accesorio = accesorio;
	}

	public int getidInventario() {
		return idInventario;
	}
				   
	public void setidInventario(int idInventario) {
		this.idInventario = idInventario;
	}

	public int getmanos1() {
		return manos1;
	}

	public void setmanos1(int manos1) {
		this.manos1 = manos1;
	}

	public int getmanos2() {
		return manos2;
	}

	public void setmanos2(int manos2) {
		this.manos2 = manos2;
	}

	public int getpie() {
		return pie;
	}

	public void setpie(int pie) {
		this.pie = pie;
	}

	public int getcabeza() {
		return cabeza;
	}

	public void setcabeza(int cabeza) {
		this.cabeza = cabeza;
	}

	public int getpecho() {
		return pecho;
	}

	public void setpecho(int pecho) {
		this.pecho = pecho;
	}

	public int getaccesorio() {
		return accesorio;
	}

	public void setaccesorio(int accesorio) {
		this.accesorio = accesorio;
	}
	
	
}
