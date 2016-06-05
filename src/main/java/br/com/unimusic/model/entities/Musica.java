package br.com.unimusic.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Musica {
	
	@Id
	private String id;
	private String titulo;
	private String artista;
	private String letra;
	private String traducao;
	
	public Musica() {
	}
	
	public Musica(String id, String titulo, String artista, String letra, String traducao) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.artista = artista;
		this.letra = letra;
		this.traducao = traducao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getTraducao() {
		return traducao;
	}

	public void setTraducao(String traducao) {
		this.traducao = traducao;
	}
	
}
