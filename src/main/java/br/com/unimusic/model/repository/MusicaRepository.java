package br.com.unimusic.model.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.unimusic.model.entities.Musica;
import br.com.unimusic.model.utils.ModeloVetorial;

public class MusicaRepository {
	
	private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("WSR");
	 
	private final EntityManager entityManager;

	public MusicaRepository() {
		super();

		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
	
	public void salvar(Musica musica){
		
		if (null != getMusica(musica.getId())) return;
 
		try {
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(musica);
			
		} catch (Exception e) {
			this.entityManager.getTransaction().rollback();
		}finally {
			this.entityManager.getTransaction().commit();
		}
	}
 
	public void alterar(Musica musica){
		
		try {
			this.entityManager.getTransaction().begin();
			this.entityManager.merge(musica);
			
		} catch (Exception e) {
			this.entityManager.getTransaction().rollback();
		}finally {
			this.entityManager.getTransaction().commit();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Musica> todasMusicas(){
 
		return this.entityManager.createQuery("SELECT p FROM Musica p ORDER BY p.id").getResultList();
	}
 
	public Musica getMusica(String id){
 
		return this.entityManager.find(Musica.class, id);
	}
	/* 
	 * Busca utilizando o modelo vetorial
	 * */
	@SuppressWarnings("unchecked")
	public List<Musica> buscaAvancada(String q){
		
		ModeloVetorial modelo = new ModeloVetorial(this.todasMusicas(),q);
		
		if (modelo.reclassifica() != null) {
			return modelo.reclassifica();
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT p FROM Musica p");
		stringBuilder.append(" WHERE p.letra LIKE :cLetra ");
		stringBuilder.append(" OR p.titulo LIKE :cLetra ");
		stringBuilder.append(" OR p.artista LIKE :cLetra ");
		stringBuilder.append(" ORDER BY p.id");
		
		Query query = this.entityManager.createQuery(stringBuilder.toString());
		
		query.setMaxResults(12);
		query.setParameter("cLetra", "%"+q+"%");
		
		List<Musica> musicas = query.getResultList();
		
		return musicas.subList(0, musicas.size() > 11? 11 : musicas.size());
	}
 
	public void excluir(String id){
 
		Musica musica = this.getMusica(id);
		
		try {
			this.entityManager.remove(musica);
		} catch (Exception e) {
			this.entityManager.getTransaction().rollback();
		}finally {
			this.entityManager.getTransaction().commit();
		}
	}
	
	public Long count(){
		
		Query q = entityManager.createQuery("select count(*) from Musica");
		return (Long) q.getSingleResult();
	}
}