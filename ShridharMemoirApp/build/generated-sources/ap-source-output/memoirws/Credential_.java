package memoirws;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import memoirws.Person;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-05-30T15:07:58")
@StaticMetamodel(Credential.class)
public class Credential_ { 

    public static volatile SingularAttribute<Credential, String> signupdate;
    public static volatile SingularAttribute<Credential, Integer> credentialid;
    public static volatile SingularAttribute<Credential, Person> personid;
    public static volatile SingularAttribute<Credential, String> passwordhash;
    public static volatile SingularAttribute<Credential, String> username;

}