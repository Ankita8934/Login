package com.service.login.auth.domain;

import lombok.Data;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
public class FreeEmailProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Boolean isActive;
    String fullExtension;
    String domainName;

    public static List<String> fetchAllRestrictedDomains() {

        Criteria criteria = getSession().createCriteria("mail");

        criteria.add(Restrictions.eq("isActive", true));
        criteria.setProjection(Projections.property("domainName"));

        return criteria.list();
    }

    private static final SessionFactory sessionFactory = null;
    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
