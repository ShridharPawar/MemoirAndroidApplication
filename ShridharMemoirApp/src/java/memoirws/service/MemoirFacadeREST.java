/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoirws.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import memoirws.Cinema;
import memoirws.Memoir;

/**
 *
 * @author shrid
 */
@Stateless
@Path("memoirws.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "ShridharMemoirAppPU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memoir entity) {
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
    public Memoir find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("findByMoviename/{moviename}")
    @Produces({"application/json"})
    public List<Memoir> findByPasswordhash(@PathParam("moviename") String moviename) {
        Query query = em.createNamedQuery("Memoir.findByMoviename");
        query.setParameter("moviename", moviename);
        return query.getResultList();
    }

    @GET
    @Path("findByMoviereleasedate/{moviereleasedate}")
    @Produces({"application/json"})
    public List<Memoir> findByMoviereleasedate(@PathParam("moviereleasedate") String moviereleasedate) throws ParseException 
    {
        Query query = em.createNamedQuery("Memoir.findByMoviereleasedate");
        query.setParameter("moviereleasedate", moviereleasedate);
        return query.getResultList();
    }

    @GET
    @Path("findByDatetimewatched/{datetimewatched}")
    @Produces({"application/json"})
    public List<Memoir> findByDatetimewatched(@PathParam("datetimewatched") String datetimewatched) throws ParseException {
        Query query = em.createNamedQuery("Memoir.findByDatetimewatched");
        query.setParameter("datetimewatched", datetimewatched);
        return query.getResultList();
    }

    @GET
    @Path("findByComment/{comment}")
    @Produces({"application/json"})
    public List<Memoir> findByComment(@PathParam("comment") String comment) {
        Query query = em.createNamedQuery("Memoir.findByComment");
        query.setParameter("comment", comment);
        return query.getResultList();
    }

    @GET
    @Path("findByUserrating/{userrating}")
    @Produces({"application/json"})
    public List<Memoir> findByUserrating(@PathParam("userrating") double userrating) {
        Query query = em.createNamedQuery("Memoir.findByUserrating");
        query.setParameter("userrating", userrating);
        return query.getResultList();
    }

    @GET
    @Path("Task3cfindByCinemaNameAndComment/{cinemaname}/{comment}")
    @Produces({"application/json"})
    public List<Memoir> findByCinemaNameAndComment(@PathParam("cinemaname") String cinemaname, @PathParam("comment") String comment) {
        TypedQuery<Memoir> query = em.createQuery("SELECT m FROM Memoir m WHERE lower(m.comment) = lower(:comment) AND lower(m.cinemaid.cinemaname) = lower(:cinemaname)", Memoir.class);
        query.setParameter("cinemaname", cinemaname);
        query.setParameter("comment", comment);
        return query.getResultList();
    }

    @GET
    @Path("Task3dfindByCinemaNameAndCommentStatic/{cinemaname}/{comment}")
    @Produces({"application/json"})
    public List<Memoir> findByCinemaNameAndCommentStatic(@PathParam("cinemaname") String cinemaname, @PathParam("comment") String comment) {
        Query query = em.createNamedQuery("Memoir.findByCinemaNameAndCommentStatic");
        query.setParameter("cinemaname", cinemaname);
        query.setParameter("comment", comment);
        return query.getResultList();
    }

    @GET
    @Path("Task4aListCinemaPostcodesAndCount/{personid}/{startdate}/{enddate}")
    @Produces({"application/json"})
    public Object ListCinemaPostcodesAndCount(@PathParam("personid") Integer personid, @PathParam("startdate") String startdate, @PathParam("enddate") String enddate) throws ParseException 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startdateobj = formatter.parse(startdate);
        Date enddateobj = formatter.parse(enddate);
        Query q = em.createQuery("SELECT m.cinemaid.location FROM Memoir as m WHERE m.personid.personid = :personid and extract(date from m.datetimewatched) > :startdateobj and extract(date from m.datetimewatched) < :enddateobj", Memoir.class);
        q.setParameter("personid", personid);
        q.setParameter("startdateobj", startdateobj);
        q.setParameter("enddateobj", enddateobj);
        List<Object[]> queryList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<String> postcodes = new ArrayList<>();
        for (Object row : queryList) {
            if (!postcodes.contains((String) row)) {
                int counter = 0;
                for (int i = 0; i < queryList.size(); i++) {
                    if (row.equals(queryList.get(i))) {
                        counter++;
                    }
                }
                JsonObject personObject = Json.createObjectBuilder().
                        add("postcode", (String) row).add("count", counter)
                        .build();
                arrayBuilder.add(personObject);
                postcodes.add((String) row);
            }
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    @GET
    @Path("Task4bListMonthsAndMoviesWatched/{personid}/{year}")
    @Produces({"application/json"})
    public Object ListMonthsAndMoviesWatched(@PathParam("personid") Integer personid, @PathParam("year") String year) throws ParseException 
    {
        Query q = em.createQuery("SELECT extract(month from m.datetimewatched) FROM Memoir as m WHERE m.personid.personid = :personid and extract(year from m.datetimewatched) =:year", Memoir.class);
        q.setParameter("personid", personid);
        q.setParameter("year", year);
        List<Object[]> queryList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<Integer> donemonths = new ArrayList<>();
        String monthname = "";
        for (Object row : queryList) {
            int counter = 0;
            if (!donemonths.contains((int) row)) {
                for (int i = 0; i < queryList.size(); i++) {
                    if (row == queryList.get(i)) {
                        counter++;
                    }
                }
                switch ((int) row) {
                    case 1:
                        monthname = "January";
                        break;
                    case 2:
                        monthname = "February";
                        break;
                    case 3:
                        monthname = "March";
                        break;
                    case 4:
                        monthname = "April";
                        break;
                    case 5:
                        monthname = "May";
                        break;
                    case 6:
                        monthname = "June";
                        break;
                    case 7:
                        monthname = "July";
                        break;
                    case 8:
                        monthname = "August";
                        break;
                    case 9:
                        monthname = "September";
                        break;
                    case 10:
                        monthname = "October";
                        break;
                    case 11:
                        monthname = "November";
                        break;
                    case 12:
                        monthname = "December";
                        break;
                }
                JsonObject monthObject = Json.createObjectBuilder().
                        add(monthname, counter).build();
                arrayBuilder.add(monthObject);
                donemonths.add((int) row);
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (!donemonths.contains(i)) {
                switch (i) {
                    case 1:
                        monthname = "January";
                        break;
                    case 2:
                        monthname = "February";
                        break;
                    case 3:
                        monthname = "March";
                        break;
                    case 4:
                        monthname = "April";
                        break;
                    case 5:
                        monthname = "May";
                        break;
                    case 6:
                        monthname = "June";
                        break;
                    case 7:
                        monthname = "July";
                        break;
                    case 8:
                        monthname = "August";
                        break;
                    case 9:
                        monthname = "September";
                        break;
                    case 10:
                        monthname = "October";
                        break;
                    case 11:
                        monthname = "November";
                        break;
                    case 12:
                        monthname = "December";
                        break;
                }
                JsonObject monthObject = Json.createObjectBuilder().
                        add(monthname, 0).build();
                arrayBuilder.add(monthObject);
                donemonths.add(i);
            }
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    @GET
    @Path("Task4cListMovieNameRatingReleaseDate/{personid}")
    @Produces({"application/json"})
    public Object ListMovieNameAndRatingAndReleaseDate(@PathParam("personid") Integer personid) throws ParseException {
        
        Query q = em.createQuery("SELECT m.moviename,m.userrating,m.moviereleasedate FROM Memoir as m WHERE m.personid.personid = :personid and m.userrating=(SELECT max(mem.userrating) FROM Memoir as mem WHERE mem.personid.personid = :personid)", Memoir.class);
        q.setParameter("personid", personid);
        List<Object[]> queryList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject movieObject = Json.createObjectBuilder().
                    add("Name", (String) row[0])
                    .add("RatingScore", (double) (row[1]))
                    .add("ReleaseDate", (String) row[2]).build();
            arrayBuilder.add(movieObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    @GET
    @Path("Task4dListMovieNamesAndReleaseYears/{personid}")
    @Produces({"application/json"})
    public Object ListMovieNamesAndReleaseYears(@PathParam("personid") Integer personid) throws ParseException 
    {
        Query q = em.createQuery("SELECT m.moviename,extract(YEAR from m.moviereleasedate) FROM Memoir as m WHERE m.personid.personid = :personid and extract(YEAR from m.datetimewatched)= extract(YEAR from m.moviereleasedate)", Memoir.class);
        q.setParameter("personid", personid);
        List<Object[]> queryList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject movieObject = Json.createObjectBuilder().
                    add("MovieName", (String) row[0])
                    .add("ReleaseYear", (Integer) (row[1])).build();
            arrayBuilder.add(movieObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    
    @GET
    @Path("Task4eRemake/{personid}")
    @Produces({"application/json"})
    public Object Task4eRemake(@PathParam("personid") Integer personid) 
    {
        Query firstQuery = em.createQuery("SELECT m.moviename,Count(m.moviename) FROM Memoir as m WHERE m.personid.personid = :personid group by m.moviename having Count(distinct m.moviereleasedate)>1", Memoir.class);
        firstQuery.setParameter("personid", personid);
        List<Object[]> queryList1 = firstQuery.getResultList();
        ArrayList<String> remakeMovies = new ArrayList<String>();
        for (Object[] row : queryList1) {
            remakeMovies.add((String) row[0]);
        }
        Query q = em.createQuery("SELECT m.moviename,extract(YEAR from m.moviereleasedate) FROM Memoir as m WHERE m.personid.personid = :personid", Memoir.class);
        q.setParameter("personid", personid);
        List<Object[]> queryList = q.getResultList();
        List<String> doneMovies = new ArrayList<String>();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            if (remakeMovies.contains(row[0]) && !doneMovies.contains((String) row[0])) {
                ArrayList<Integer> years = new ArrayList<Integer>();
                for (Object[] row1 : queryList) {
                    if (((String) row1[0]).equals((String) row[0])) {
                        years.add((Integer) row1[1]);
                    }
                }
                String ReleaseYears = "";
                for (int i = 0; i < years.size(); i++) {
                    ReleaseYears = ReleaseYears + years.get(i);
                    if (i != years.size() - 1) {
                        ReleaseYears = ReleaseYears + ",";
                    }
                }

                JsonObject movieObject = Json.createObjectBuilder().
                        add("MovieName", (String) row[0])
                        .add("ReleaseYears", ReleaseYears).build();
                arrayBuilder.add(movieObject);
                doneMovies.add((String) row[0]);
            }

        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    @GET
    @Path("Task4fTopMovies/{personid}")
    @Produces({"application/json"})
    public Object Task4fTopMovies(@PathParam("personid") Integer personid) throws ParseException 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String currentYear = formatter.format(date);
        Query q = em.createQuery("SELECT m.moviename,m.moviereleasedate,m.userrating FROM Memoir as m WHERE m.personid.personid = :personid and extract(YEAR from m.moviereleasedate)=:currentYear order by m.userrating desc", Memoir.class);
        q.setParameter("personid", personid);
        q.setParameter("currentYear", currentYear);
        List<Object[]> queryList = q.setMaxResults(5).getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject movieObject = Json.createObjectBuilder().
                    add("MovieName", (String) row[0])
                    .add("ReleaseDate", (String)row[1]).add("RatingScore", (double) row[2]).build();
            arrayBuilder.add(movieObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    @GET
    @Path("findByPersonid/{personid}")
    @Produces({"application/json"})
    public List<Memoir> findByPersonid(@PathParam("personid") int personid) {
        TypedQuery<Memoir> query = em.createQuery("SELECT m FROM Memoir m WHERE m.personid.personid = :personid", Memoir.class);
        query.setParameter("personid", personid);
        return query.getResultList();
    }
    
     @GET
    @Path("getHighestMemoirId")
    @Produces({"application/json"})
    public Object getHighestMemoirId() 
    {
        Query query = em.createQuery("SELECT m.memoirid FROM Memoir m order by m.memoirid desc", Memoir.class);
        Object queryList = query.setMaxResults(1).getSingleResult();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject cinemaObject = Json.createObjectBuilder().
                   add("MemoirId", queryList.toString())
                    .build();
            arrayBuilder.add(cinemaObject);
        
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
}
