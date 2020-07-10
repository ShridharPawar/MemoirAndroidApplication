package memoirws;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import memoirws.Credential;
import memoirws.Memoir;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-30T15:07:58")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> firstname;
    public static volatile SingularAttribute<Person, String> address;
    public static volatile SingularAttribute<Person, String> gender;
    public static volatile SingularAttribute<Person, String> surname;
    public static volatile SingularAttribute<Person, String> dob;
    public static volatile CollectionAttribute<Person, Memoir> memoirCollection;
    public static volatile CollectionAttribute<Person, Credential> credentialCollection;
    public static volatile SingularAttribute<Person, String> postcode;
    public static volatile SingularAttribute<Person, Integer> personid;
    public static volatile SingularAttribute<Person, String> state;

}