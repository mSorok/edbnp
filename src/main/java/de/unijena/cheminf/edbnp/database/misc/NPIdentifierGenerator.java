package de.unijena.cheminf.edbnp.database.misc;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowire;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;




public class NPIdentifierGenerator implements IdentifierGenerator, Configurable {


    private String prefix;



    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {


        String query = String.format("select %s from %s", session.getEntityPersister(object.getClass().getName(), object).getIdentifierPropertyName(),
                object.getClass().getSimpleName());


        List ids = session.createQuery(query).list() ;

        int max = 0;
        for(Object o : ids){
            int numid = Integer.parseInt(o.toString().replace("^"+prefix+"0+", "")   );
            if(numid>max){
                max=numid;
            }
        }

        max = max+1;
        int length = String.valueOf(max).length();



        return prefix+"0".repeat(7-length)+max;
    }


    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }
}
