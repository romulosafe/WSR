package br.com.unimusic.model.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.unimusic.model.entities.Musica;

public class ModeloVetorial {
	
	private List<Musica> musicas;
	private ArrayList<Double> tf;
	private ArrayList<Double> idf;
	private Double N;//Tamanho total da coleção
	private Double Nt = (double) 0;//Quantidade de registros com o termo

	public ModeloVetorial(List<Musica> musicas,String termo) {
		this.musicas = musicas; 
		
		for (Musica musica : musicas) {
			Nt += musica.getLetra().split(termo).length;
			N  += musica.getLetra().split(" ").length;
			
			tf.add(Nt);
			idf.add(Math.log(N/Nt));
		}
	}
	
	public List<Musica> reclassifica(){
		
		return musicas;
	}
}
