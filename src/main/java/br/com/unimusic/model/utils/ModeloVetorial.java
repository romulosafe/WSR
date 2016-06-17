package br.com.unimusic.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
			
			for (int i = 0; i < wij.size(); i++) {
				Double wi = wij.get(i);
				Double wq = wiq.get(i);
				
				if (wi == null || wq == null) {
					continue;
				}
				
				musicas.get(i).setPeso(
										(wi * wq)
										/
										(Math.sqrt(Math.pow(wi, 2)) * Math.sqrt(Math.pow(wq, 2)))
										);
				
				System.out.println(musicas.get(i));
			}
			List<Musica> filtro = new ArrayList<>();
			
			for (Musica m : musicas) {
				if (m.getPeso() != null) {
					filtro.add(m);
				}
			}
			
			this.musicas = this.reordena(filtro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Musica> reordena(List<Musica> filtro) {
		
		Collections.sort(filtro,new Comparator<Musica>() {

			@Override
			public int compare(Musica m1, Musica m2) {
				
				return m1.getPeso() < m2.getPeso() ? -1 : (m1.getPeso() > m2.getPeso() ? +1 : 0);
			}
		});
		return filtro.subList(0, 11);
	}

	public List<Musica> reclassifica(){
	
		return musicas;
	}
}
