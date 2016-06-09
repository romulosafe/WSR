package br.com.unimusic.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import br.com.unimusic.model.entities.Musica;
import br.com.unimusic.model.repository.MusicaRepository;

@Path("/service")
public class ServiceController {
	
	private final MusicaRepository repository = new MusicaRepository();
	private final Gson gson = new Gson();
	
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cadastrar")
	public String Cadastrar(String musica){
		Musica entity = gson.fromJson(musica, Musica.class);
 
		try {
 
			repository.salvar(entity);
 
			return "OK";
 
		} catch (Exception e) {
 
			return "ERRO";
		}
 
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/todasMusicas")
	public String TodasMusicas(){
 
		List<Musica> todas = this.repository.todasMusicas();
		String musicas = gson.toJson(todas);
		
		return musicas;
	}
	
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/getMusica/{id}")
	public Musica GetPessoa(@PathParam("id") String id){
 
		Musica entity = repository.getMusica(id);
		
		if(entity != null)
			return entity;
 
		return null;
	}
	
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/buscar/{q}")
	public String buscaAvancada(@PathParam("q") String q){
		List<Musica> relevantes = repository.buscaAvancada(q);
		String jsonRelevantes = gson.toJson(relevantes,Musica.class);
		
		return jsonRelevantes;
	}
}