package servidor;

/**
 * The Class Inv.
 */
public class Inv {

	/**
	 * The id inventario.
	 */
	private int idInventario;

	/**
	 * The manos 1.
	 */
	private int manos1;

	/**
	 * The manos 2.
	 */
	private int manos2;

	/**
	 * The pie.
	 */
	private int pie;

	/**
	 * The cabeza.
	 */
	private int cabeza;

	/**
	 * The pecho.
	 */
	private int pecho;

	/**
	 * The accesorio.
	 */
	private int accesorio;

	/**
	 * Instantiates a new inv.
	 */
	public Inv() {
	}

	/**
	 * Instantiates a new inv.
	 *
	 * @param idInventario
	 *            the id inventario
	 */
	public Inv(final int idInventario) {
		this.idInventario = idInventario;
		this.pie = -1;
		this.cabeza = -1;
		this.pecho = -1;
		this.accesorio = -1;
		this.manos1 = -1;
		this.manos2 = -1;
	}

	/**
	 * Instantiates a new inv.
	 *
	 * @param idInventario
	 *            the id inventario
	 * @param manos1
	 *            the manos 1
	 * @param manos2
	 *            the manos 2
	 * @param pie
	 *            the pie
	 * @param cabeza
	 *            the cabeza
	 * @param pecho
	 *            the pecho
	 * @param accesorio
	 *            the accesorio
	 */
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

	/**
	 * Gets the id inventario.
	 *
	 * @return the id inventario
	 */
	public int getidInventario() {
		return idInventario;
	}

	/**
	 * Sets the id inventario.
	 *
	 * @param idInventario
	 *            the new id inventario
	 */
	public void setidInventario(int idInventario) {
		this.idInventario = idInventario;
	}

	/**
	 * Gets the manos 1.
	 *
	 * @return the manos 1
	 */
	public int getmanos1() {
		return manos1;
	}

	/**
	 * Sets the manos 1.
	 *
	 * @param manos1
	 *            the new manos 1
	 */
	public void setmanos1(int manos1) {
		this.manos1 = manos1;
	}

	/**
	 * Gets the manos 2.
	 *
	 * @return the manos 2
	 */
	public int getmanos2() {
		return manos2;
	}

	/**
	 * Sets the manos 2.
	 *
	 * @param manos2
	 *            the new manos 2
	 */
	public void setmanos2(int manos2) {
		this.manos2 = manos2;
	}

	/**
	 * Gets the pie.
	 *
	 * @return the pie
	 */
	public int getpie() {
		return pie;
	}

	/**
	 * Sets the pie.
	 *
	 * @param pie
	 *            the new pie
	 */
	public void setpie(int pie) {
		this.pie = pie;
	}

	/**
	 * Gets the cabeza.
	 *
	 * @return the cabeza
	 */
	public int getcabeza() {
		return cabeza;
	}

	/**
	 * Sets the cabeza.
	 *
	 * @param cabeza
	 *            the new cabeza
	 */
	public void setcabeza(int cabeza) {
		this.cabeza = cabeza;
	}

	/**
	 * Gets the pecho.
	 *
	 * @return the pecho
	 */
	public int getpecho() {
		return pecho;
	}

	/**
	 * Sets the pecho.
	 *
	 * @param pecho
	 *            the new pecho
	 */
	public void setpecho(int pecho) {
		this.pecho = pecho;
	}

	/**
	 * Gets the accesorio.
	 *
	 * @return the accesorio
	 */
	public int getaccesorio() {
		return accesorio;
	}

	/**
	 * Sets the accesorio.
	 *
	 * @param accesorio
	 *            the new accesorio
	 */
	public void setaccesorio(int accesorio) {
		this.accesorio = accesorio;
	}

}
