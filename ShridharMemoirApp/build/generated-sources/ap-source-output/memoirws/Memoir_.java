package memoirws;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import memoirws.Cinema;
import memoirws.Person;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-30T15:07:58")
@StaticMetamodel(Memoir.class)
public class Memoir_ { 

    public static volatile SingularAttribute<Memoir, Cinema> cinemaid;
    public static volatile SingularAttribute<Memoir, String> datetimewatched;
    public static volatile SingularAttribute<Memoir, String> moviereleasedate;
    public static volatile SingularAttribute<Memoir, Double> userrating;
    public static volatile SingularAttribute<Memoir, String> comment;
    public static volatile SingularAttribute<Memoir, Person> personid;
    public static volatile SingularAttribute<Memoir, Integer> memoirid;
    public static volatile SingularAttribute<Memoir, String> moviename;

}