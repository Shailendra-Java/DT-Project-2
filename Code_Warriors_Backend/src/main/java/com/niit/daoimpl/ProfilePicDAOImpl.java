package com.niit.daoimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.ProfilePicDAO;
import com.niit.model.ProfilePicture;

@Repository("profileDAO")
public class ProfilePicDAOImpl implements ProfilePicDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public void save(ProfilePicture profilePicture)
	{
		System.out.println("Profile Loginname : "+profilePicture.getLoginname());
		Session session=sessionFactory.openSession();
		session.save(profilePicture);
		session.close();
	}
	
	public ProfilePicture getProfilePicture(String loginname)
	{
		Session session=sessionFactory.openSession();
		ProfilePicture profilePicture=(ProfilePicture)session.get(ProfilePicture.class,loginname);
		session.close();
		return profilePicture;
	}

}
