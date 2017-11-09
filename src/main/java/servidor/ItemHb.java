package servidor;

import java.io.IOException;

import dominio.Item;

public class ItemHb {
    private int idItem;
    private String nombre;
    private int wereable;
    private int bonusSalud;
    private int bonusEnergia;
    private int bonusFuerza;
    private int bonusDestreza;
    private int bonusInteligencia;
    private String foto;
    private String fotoEquipado;
    private int fuerzaRequerida;
    private int destrezaRequerida;
    private int inteligenciaRequerida;

 
    public int getIdItem() {
	return idItem;
    }

    public void setIdItem(final int idItem) {
	this.idItem = idItem;
    }

    public String getNombre() {
	return nombre;
    }


    public void setNombre(final String nombre) {
	this.nombre = nombre;
    }


    public int getWereable() {
	return wereable;
    }


    public void setWereable(final int wereable) {
	this.wereable = wereable;
    }


    public int getBonusSalud() {
	return bonusSalud;
    }


    public void setBonusSalud(final int bonusSalud) {
	this.bonusSalud = bonusSalud;
    }


    public int getBonusEnergia() {
	return bonusEnergia;
    }


    public void setBonusEnergia(final int bonusEnergia) {
	this.bonusEnergia = bonusEnergia;
    }

    public int getBonusFuerza() {
	return bonusFuerza;
    }


    public void setBonusFuerza(final int bonusFuerza) {
	this.bonusFuerza = bonusFuerza;
    }


    public int getBonusDestreza() {
	return bonusDestreza;
    }


    public void setBonusDestreza(final int bonusDestreza) {
	this.bonusDestreza = bonusDestreza;
    }


    public int getBonusInteligencia() {
	return bonusInteligencia;
    }

  
    public void setBonusInteligencia(final int bonusInteligencia) {
	this.bonusInteligencia = bonusInteligencia;
    }


    public String getFoto() {
	return foto;
    }

 
    public void setFoto(final String foto) {
	this.foto = foto;
    }


    public String getFotoEquipado() {
	return fotoEquipado;
    }


    public void setFotoEquipado(final String fotoEquipado) {
	this.fotoEquipado = fotoEquipado;
    }


    public int getFuerzaRequerida() {
	return fuerzaRequerida;
    }


    public void setFuerzaRequerida(final int fuerzaRequerida) {
	this.fuerzaRequerida = fuerzaRequerida;
    }

    public int getDestrezaRequerida() {
	return destrezaRequerida;
    }


    public void setDestrezaRequerida(final int destrezaRequerida) {
	this.destrezaRequerida = destrezaRequerida;
    }


    public int getInteligenciaRequerida() {
	return inteligenciaRequerida;
    }


    public void setInteligenciaRequerida(final int inteligenciaRequerida) {
	this.inteligenciaRequerida = inteligenciaRequerida;
    }

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