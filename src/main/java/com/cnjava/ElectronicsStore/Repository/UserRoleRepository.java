package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cnjava.ElectronicsStore.Model.User;
import com.cnjava.ElectronicsStore.Model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	@Query(   "SELECT ur.appRole.name "
			+ "FROM UserRole ur "
			+ "where ur.user.id = :id")
	List<String> getRoleNames(@Param("id") int id);
	
	@Query(value = 
			"Select * "
			+ "from UserRole ur "
			+ "where ur.appRole.id = 2 "
			+ "limit :page, :number",
			nativeQuery = true)
	List<UserRole> getLimitUser(@Param("page") int page, @Param("number") int n);
	
	@Query(value = 
			"Select * "
			+ "from UserRole ur "
			+ "where ur.appRole.id = 1 "
			+ "limit :page,:number",
			nativeQuery = true)
	List<UserRole> getLimitAdmin(@Param("page") int page, @Param("number") int n);
	
	@Query(   "SELECT ur "
			+ "FROM UserRole ur "
			+ "where ur.user.id = :id")
	List<UserRole> getRole(@Param("id") int id);
	
	@Query(   "SELECT ur "
			+ "FROM UserRole ur "
			+ "WHERE ur.user.id = :userid and ur.appRole.id = :roleid")
	UserRole check(@Param("userid") int userid, @Param("roleid") int roleid );
}
