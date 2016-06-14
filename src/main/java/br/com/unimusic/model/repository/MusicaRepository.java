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
	public List<Musica> buscaAvancada(String q){
		
		/*StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT p FROM Musica p");
		stringBuilder.append(" WHERE p.letra LIKE %p:cLetra% ");
		stringBuilder.append(" OR p.titulo LIKE %p:cLetra% ");
		stringBuilder.append(" OR p.artista LIKE %p:cLetra% ");
		stringBuilder.append(" ORDER BY p.id");*/
		/*
		Query query = this.entityManager.createQuery(stringBuilder.toString());
		query.setParameter("cLetra", q);*/
		
		List<Musica> musicas = new ModeloVetorial(this.todasMusicas(),q).reclassifica();
		
		return null;
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