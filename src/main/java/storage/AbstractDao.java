package storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class AbstractDao<T> {

    private Class<T> tClass;

    public static final SessionFactory SESSION_FACTORY = new Configuration().configure("/hibernate/hibernate.cfg.xml").buildSessionFactory();

    public AbstractDao(Class tClass){
        this.tClass = tClass;
    }

    public void createDao(T object){
        Session session = openTransactSession();
        session.save(object);
        closeTransactSession(session);
    }

    public List<T> getAllDao(){
        Session session = openSession();
        List<T> result = session.createQuery("from " + tClass.getSimpleName(), tClass).list();
        session.close();
        return result;
    }

    public Session openSession(){
        return SESSION_FACTORY.openSession();
    }

    public Session openTransactSession(){
        Session session = openSession();
        session.beginTransaction();
        return session;
    }

    public void closeTransactSession(Session session){
        session.getTransaction().commit();
        session.close();
    }
}
