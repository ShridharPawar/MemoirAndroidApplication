package memoirws;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import memoirws.Memoir;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-30T15:07:58")
@StaticMetamodel(Cinema.class)
public class Cinema_ { 

    public static volatile SingularAttribute<Cinema, String> cinemaname;
    public static volatile SingularAttribute<Cinema, Integer> cinemaid;
    public static volatile CollectionAttribute<Cinema, Memoir> memoirCollection;
    public static volatile SingularAttribute<Cinema, String> location;

}