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
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import memoirws.Person;

/**
 *
 * @author shrid
 */
@Stateless
@Path("memoirws.person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "ShridharMemoirAppPU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Person entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Person entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Person find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByFirstname/{firstname}")
    @Produces({"application/json"})
    public List<Person> findByFirstname(@PathParam("firstname") String firstname) 
    {
        Query query = em.createNamedQuery("Person.findByFirstname");
        query.setParameter("firstname", firstname);
        return query.getResultList();
    }
     
    @GET
    @Path("findBySurname/{surname}")
    @Produces({"application/json"})
    public List<Person> findBySurname(@PathParam("surname") String surname) 
    {
        Query query = em.createNamedQuery("Person.findBySurname");
        query.setParameter("surname", surname);
        return query.getResultList();
    }
         
    @GET
    @Path("findByGender/{gender}")
    @Produces({"application/json"})
    public List<Person> findByGender(@PathParam("gender") String gender) 
    {
        Query query = em.createNamedQuery("Person.findByGender");
        query.setParameter("gender", gender);
        return query.getResultList();
    }
         
    @GET
    @Path("findByDob/{dob}")
    @Produces({"application/json"})
    public List<Person> findByDob(@PathParam("dob") String dob) 
    {
        Query query = em.createNamedQuery("Person.findByDob");
        query.setParameter("dob",dob);
        return query.getResultList();
    }
         
    @GET
    @Path("findByAddress/{address}")
    @Produces({"application/json"})
    public List<Person> findByAddress(@PathParam("address") String address) 
    {
        Query query = em.createNamedQuery("Person.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();
    }
         
    @GET
    @Path("findByState/{state}")
    @Produces({"application/json"})
    public List<Person> findByState(@PathParam("state") String state) 
    {
        Query query = em.createNamedQuery("Person.findByState");
        query.setParameter("state", state);
        return query.getResultList();
    }
         
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({"application/json"})
    public List<Person> findByPostcode(@PathParam("postcode") String postcode) 
    {
        Query query = em.createNamedQuery("Person.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    @GET
    @Path("Task3bfindByNameAndDobAndPostcode/{firstname}/{dob}/{postcode}")
    @Produces({"application/json"})
    public List<Person> findByNameAndDobAndPostcode(@PathParam("firstname") String firstname, @PathParam("dob") String dob, @PathParam("postcode") String postcode)
    {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE lower(p.firstname) = lower(:firstname) AND p.dob = :dob AND p.postcode = :postcode", Person.class);
        query.setParameter("firstname", firstname);
        query.setParameter("dob", dob);
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    @GET
    @Path("getHighestPersonId")
    @Produces({"application/json"})
    public Object getHighestPersonId() 
    {
        Query query = em.createQuery("SELECT p.personid FROM Person p order by p.personid desc", Person.class);
        Object queryList = query.setMaxResults(1).getSingleResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject movieObject = Json.createObjectBuilder().
                   add("PersonId", queryList.toString())
                    .build();
            arrayBuilder.add(movieObject);
        
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
