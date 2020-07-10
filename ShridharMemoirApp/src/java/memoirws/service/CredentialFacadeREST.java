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
import memoirws.Credential;

/**
 *
 * @author shrid
 */
@Stateless
@Path("memoirws.credential")
public class CredentialFacadeREST extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "ShridharMemoirAppPU")
    private EntityManager em;

    public CredentialFacadeREST() {
        super(Credential.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credential entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Credential entity) {
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
    public Credential find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByUsername/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findByUsername(@PathParam("username") String username) 
    {
        Query query = em.createNamedQuery("Credential.findByUsername");
        query.setParameter("username", username);
        return query.getResultList();
    }
         
    @GET
    @Path("findByPasswordhash/{passwordhash}")
    @Produces({"application/json"})
    public List<Credential> findByPasswordhash(@PathParam("passwordhash") String passwordhash) 
    {
        Query query = em.createNamedQuery("Credential.findByPasswordhash");
        query.setParameter("passwordhash", passwordhash);
        return query.getResultList();
    }

    @GET
    @Path("findBySignupdate/{signupdate}")
    @Produces({"application/json"})
    public List<Credential> findBySignupdate(@PathParam("signupdate") String signupdate) 
    {
        Query query = em.createNamedQuery("Credential.findBySignupdate");
        query.setParameter("signupdate", signupdate);
        return query.getResultList();
    }
    
    @GET
    @Path("getHighestCredentialId")
    @Produces({"application/json"})
    public Object getHighestCredentialId() 
    {
        Query query = em.createQuery("SELECT c.credentialid FROM Credential c order by c.credentialid desc", Credential.class);
        Object queryList = query.setMaxResults(1).getSingleResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject movieObject = Json.createObjectBuilder().
                   add("CredentialId", queryList.toString())
                    .build();
            arrayBuilder.add(movieObject);
        
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    @GET
    @Path("findByUsernameAndPasswordHash/{username}/{passwordhash}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findByUsernameAndPasswordHash(@PathParam("username") String username,@PathParam("passwordhash") String passwordhash) 
    {
        Query query = em.createQuery("SELECT c FROM Credential c WHERE lower(c.username) = lower(:username) and c.passwordhash = :passwordhash",Credential.class);
        query.setParameter("passwordhash", passwordhash);
        query.setParameter("username", username);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
