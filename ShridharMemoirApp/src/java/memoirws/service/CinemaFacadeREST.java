/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoirws.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import memoirws.Cinema;
import memoirws.Person;

/**
 *
 * @author shrid
 */
@Stateless
@Path("memoirws.cinema")
public class CinemaFacadeREST extends AbstractFacade<Cinema> {

    @PersistenceContext(unitName = "ShridharMemoirAppPU")
    private EntityManager em;

    public CinemaFacadeREST() {
        super(Cinema.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Cinema entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Cinema entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Cinema find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cinema> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cinema> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByCinemaname/{cinemaname}")
    @Produces({"application/json"})
    public List<Cinema> findByCinemaname(@PathParam("cinemaname") String cinemaname) 
    {
        Query query = em.createNamedQuery("Cinema.findByCinemaname");
        query.setParameter("cinemaname", cinemaname);
        return query.getResultList();
    }

    @GET
    @Path("findByLocation/{location}")
    @Produces({"application/json"})
    public List<Cinema> findByLocation(@PathParam("location") String location) 
    {
        Query query = em.createNamedQuery("Cinema.findByLocation");
        query.setParameter("location", location);
        return query.getResultList();
    }
    
    @GET
    @Path("getHighestCinemaId")
    @Produces({"application/json"})
    public Object getHighestCinemaId() 
    {
        Query query = em.createQuery("SELECT c.cinemaid FROM Cinema c order by c.cinemaid desc", Cinema.class);
        Object queryList = query.setMaxResults(1).getSingleResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject cinemaObject = Json.createObjectBuilder().
                   add("CinemaId", queryList.toString())
                    .build();
            arrayBuilder.add(cinemaObject);
        
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    @GET
    @Path("getIdByNameAndPost/{cinemaname}/{location}")
    @Produces({"application/json"})
    public Object getIdByNameAndPost(@PathParam("cinemaname") String cinemaname,@PathParam("location") String location) 
    {
        Query query = em.createQuery("SELECT c.cinemaid FROM Cinema c where lower(c.cinemaname)= lower(:cinemaname) and c.location= :location", Cinema.class);
        query.setParameter("location", location);
        query.setParameter("cinemaname", cinemaname);
        Object queryList = query.setMaxResults(1).getSingleResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject cinemaObject = Json.createObjectBuilder().
                   add("CinemaId", queryList.toString())
                    .build();
            arrayBuilder.add(cinemaObject);
        
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    @GET
    @Path("findByCinemaAndLocation/{cinemaname}/{location}")
    @Produces({"application/json"})
    public List<Cinema> findByCinemaNameAndPostCode(@PathParam("cinemaname") String cinemaname,@PathParam("location") String location) 
    {
        Query query = em.createQuery("SELECT c FROM Cinema c where lower(c.cinemaname)= lower(:cinemaname) and c.location= :location",Cinema.class);
        query.setParameter("cinemaname", cinemaname);
        query.setParameter("location", location);
        return query.getResultList();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
