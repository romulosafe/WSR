package br.com.unimusic.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.unimusic.model.entities.Musica;

public class ModeloVetorial {
	
	private List<Musica> musicas;
	private ArrayList<Double> wij = new ArrayList<>();
	private ArrayList<Double> wiq = new ArrayList<>();
	private Double N;//Tamanho total da coleção ou tf
	private Double Nt = (double) 0;//Quantidade de registros com o termo ou Idf
	private int TF;
	private int DF = 0;
	private Double IDF;

	public ModeloVetorial(List<Musica> musicas,String termo) {
		this.musicas = musicas; 
		
		try {
			for (Musica musica : musicas) {
				TF = musica.getLetra().split(termo).length -1;//Frequencia do termo na coleção - tf
				if (TF > 0) {
					
					for (int i = 0; i < musicas.size(); i++) {
						if (	musicas.get(i).getLetra().contains(termo) || 
								musicas.get(i).getTitulo().contains(termo)||
								musicas.get(i).getArtista().contains(termo)
							) {
							
							DF++;
						}
					}
					//DF  = musica.getLetra().length();//Quantidade te termos da coleção
					N  = (double) musica.getLetra().split(" ").length;//Quantidade te termos da coleção
					
					IDF = Math.log(N/TF);//termo de discriminação
					wij.add(TF * IDF);
					wiq.add(DF * IDF);
					
				}
			}
			
			for (int i = 0; i < musicas.size(); i++) {
				Double wi = wij.get(i);
				Double wq = wiq.get(i);
				
				musicas.get(i).setPeso(
										(wi * wq)
										/
										(Math.sqrt(Math.pow(wi, 2)) * Math.sqrt(Math.pow(wq, 2)))
										);
				
				System.out.println(musicas.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Musica> reclassifica(){
		
/*		List<Musica> mus = Collections.sort(musicas, null);
		
		return Collections.somusicas;*/
		return null;
	}
}
