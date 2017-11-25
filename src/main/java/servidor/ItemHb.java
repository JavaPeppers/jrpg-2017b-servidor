package servidor;

import java.io.IOException;

import dominio.Item;

/**
 * The Class ItemHb.
 */
public class ItemHb {

	/**
	 * The id item.
	 */
	private int idItem;

	/**
	 * The nombre.
	 */
	private String nombre;

	/**
	 * The wereable.
	 */
	private int wereable;

	/**
	 * The bonus salud.
	 */
	private int bonusSalud;

	/**
	 * The bonus energia.
	 */
	private int bonusEnergia;

	/**
	 * The bonus fuerza.
	 */
	private int bonusFuerza;

	/**
	 * The bonus destreza.
	 */
	private int bonusDestreza;

	/**
	 * The bonus inteligencia.
	 */
	private int bonusInteligencia;

	/**
	 * The foto.
	 */
	private String foto;

	/**
	 * The foto equipado.
	 */
	private String fotoEquipado;

	/**
	 * The fuerza requerida.
	 */
	private int fuerzaRequerida;

	/**
	 * The destreza requerida.
	 */
	private int destrezaRequerida;

	/**
	 * The inteligencia requerida.
	 */
	private int inteligenciaRequerida;

	/**
	 * Gets the id item.
	 *
	 * @return the id item
	 */
	public int getIdItem() {
		return idItem;
	}

	/**
	 * Sets the id item.
	 *
	 * @param idItem
	 *            the new id item
	 */
	public void setIdItem(final int idItem) {
		this.idItem = idItem;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre
	 *            the new nombre
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the wereable.
	 *
	 * @return the wereable
	 */
	public int getWereable() {
		return wereable;
	}

	/**
	 * Sets the wereable.
	 *
	 * @param wereable
	 *            the new wereable
	 */
	public void setWereable(final int wereable) {
		this.wereable = wereable;
	}

	/**
	 * Gets the bonus salud.
	 *
	 * @return the bonus salud
	 */
	public int getBonusSalud() {
		return bonusSalud;
	}

	/**
	 * Sets the bonus salud.
	 *
	 * @param bonusSalud
	 *            the new bonus salud
	 */
	public void setBonusSalud(final int bonusSalud) {
		this.bonusSalud = bonusSalud;
	}

	/**
	 * Gets the bonus energia.
	 *
	 * @return the bonus energia
	 */
	public int getBonusEnergia() {
		return bonusEnergia;
	}

	/**
	 * Sets the bonus energia.
	 *
	 * @param bonusEnergia
	 *            the new bonus energia
	 */
	public void setBonusEnergia(final int bonusEnergia) {
		this.bonusEnergia = bonusEnergia;
	}

	/**
	 * Gets the bonus fuerza.
	 *
	 * @return the bonus fuerza
	 */
	public int getBonusFuerza() {
		return bonusFuerza;
	}

	/**
	 * Sets the bonus fuerza.
	 *
	 * @param bonusFuerza
	 *            the new bonus fuerza
	 */
	public void setBonusFuerza(final int bonusFuerza) {
		this.bonusFuerza = bonusFuerza;
	}

	/**
	 * Gets the bonus destreza.
	 *
	 * @return the bonus destreza
	 */
	public int getBonusDestreza() {
		return bonusDestreza;
	}

	/**
	 * Sets the bonus destreza.
	 *
	 * @param bonusDestreza
	 *            the new bonus destreza
	 */
	public void setBonusDestreza(final int bonusDestreza) {
		this.bonusDestreza = bonusDestreza;
	}

	/**
	 * Gets the bonus inteligencia.
	 *
	 * @return the bonus inteligencia
	 */
	public int getBonusInteligencia() {
		return bonusInteligencia;
	}

	/**
	 * Sets the bonus inteligencia.
	 *
	 * @param bonusInteligencia
	 *            the new bonus inteligencia
	 */
	public void setBonusInteligencia(final int bonusInteligencia) {
		this.bonusInteligencia = bonusInteligencia;
	}

	/**
	 * Gets the foto.
	 *
	 * @return the foto
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * Sets the foto.
	 *
	 * @param foto
	 *            the new foto
	 */
	public void setFoto(final String foto) {
		this.foto = foto;
	}

	/**
	 * Gets the foto equipado.
	 *
	 * @return the foto equipado
	 */
	public String getFotoEquipado() {
		return fotoEquipado;
	}

	/**
	 * Sets the foto equipado.
	 *
	 * @param fotoEquipado
	 *            the new foto equipado
	 */
	public void setFotoEquipado(final String fotoEquipado) {
		this.fotoEquipado = fotoEquipado;
	}

	/**
	 * Gets the fuerza requerida.
	 *
	 * @return the fuerza requerida
	 */
	public int getFuerzaRequerida() {
		return fuerzaRequerida;
	}

	/**
	 * Sets the fuerza requerida.
	 *
	 * @param fuerzaRequerida
	 *            the new fuerza requerida
	 */
	public void setFuerzaRequerida(final int fuerzaRequerida) {
		this.fuerzaRequerida = fuerzaRequerida;
	}

	/**
	 * Gets the destreza requerida.
	 *
	 * @return the destreza requerida
	 */
	public int getDestrezaRequerida() {
		return destrezaRequerida;
	}

	/**
	 * Sets the destreza requerida.
	 *
	 * @param destrezaRequerida
	 *            the new destreza requerida
	 */
	public void setDestrezaRequerida(final int destrezaRequerida) {
		this.destrezaRequerida = destrezaRequerida;
	}

	/**
	 * Gets the inteligencia requerida.
	 *
	 * @return the inteligencia requerida
	 */
	public int getInteligenciaRequerida() {
		return inteligenciaRequerida;
	}

	/**
	 * Sets the inteligencia requerida.
	 *
	 * @param inteligenciaRequerida
	 *            the new inteligencia requerida
	 */
	public void setInteligenciaRequerida(final int inteligenciaRequerida) {
		this.inteligenciaRequerida = inteligenciaRequerida;
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem() {
		try {
			return new Item(this.idItem, this.nombre, this.wereable, this.bonusSalud, this.bonusEnergia,
					this.bonusFuerza, this.bonusDestreza, this.bonusInteligencia, this.foto, this.fotoEquipado);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
