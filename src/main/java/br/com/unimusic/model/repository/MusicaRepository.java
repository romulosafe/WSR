package br.com.unimusic.model.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.unimusic.model.entities.Musica;

public class MusicaRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	 
	private final EntityManager entityManager;

	public MusicaRepository() {
		super();
		/*CRIANDO O NOSSO EntityManagerFactory COM AS PORPRIEDADOS DO ARQUIVO persistence.xml */
		this.entityManagerFactory = Persistence.createEntityManagerFactory("WSR");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
	
	/**
	 * CRIA UM NOVO REGISTRO NO BANCO DE DADOS
	 * */
	public void salvar(Musica musica){
 
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(musica);
		this.entityManager.getTransaction().commit();
	}
 
	/**
	 * ALTERA UM REGISTRO CADASTRADO
	 * */
	public void alterar(Musica musica){
 
		this.entityManager.getTransaction().begin();
		this.entityManager.merge(musica);
		this.entityManager.getTransaction().commit();
	}
 
	/**
	 * RETORNA TODAS AS PESSOAS CADASTRADAS NO BANCO DE DADOS 
	 * */
	@SuppressWarnings("unchecked")
	public List<Musica> todasMusicas(){
 
		return this.entityManager.createQuery("SELECT p FROM Musica p ORDER BY p.id").getResultList();
	}
 
	/**
	 * CONSULTA UMA PESSOA CADASTRA PELO CÓDIGO
	 * */
	public Musica getMusica(String id){
 
		return this.entityManager.find(Musica.class, id);
	}
 
	/**
	 * EXCLUINDO UM REGISTRO PELO CÓDIGO
	**/
	public void excluir(String id){
 
		Musica musica = this.getMusica(id);
		
		try {
			this.entityManager.getTransaction().begin();
			this.entityManager.remove(musica);
		} catch (Exception e) {
			this.entityManager.getTransaction().rollback();
		}finally {
			this.entityManager.getTransaction().commit();
		}
 
	}
}
