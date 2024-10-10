package com.tenco.blog_v2.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository // IoC
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;


    /**
     * 사용자 저장 메서드 (JPA API 사용)
     * @param user
     * @return 저장된 사용자 엔티티
     */
    @Transactional
    public User save(User user) {
        // JPQL 은 INSERT 구문을 직접 지원하지 않습니다.
        em.persist(user); // 영속화
        return  user;
    }


    /**
     * 사용자 이름과 비밀번호로 사용자 조회
     * @param username
     * @param password
     * @return 조회된 User 엔티티, null
     */

    public User findByUsernameAndPassword(String username, String password) {

       TypedQuery<User> jpql =
               em.createQuery("select u from User u where u.username = :username and u.password = :password",User.class);
        jpql.setParameter("username", username);
        jpql.setParameter("password", password);
        return jpql.getSingleResult();
    }

    @Transactional // 알아서 비교해서 더티 체킹 반영
    // 사용자 정보 수정 기능 만들기
    public User updateById(int id, String password, String email) {
       
       // id 값으로 영속성 컨텍스트 정보가 있는지 확인
       User userEntity = findById(id);
       userEntity.setPassword(password);
       userEntity.setEmail(email);
       return  userEntity;

//        Query query = em.createQuery(" UPDATE User u SET u.password = :password, u.email = :email where u.id = :id ");
//        query.setParameter("password", password);
//        query.setParameter("email", email);
//        query.setParameter("id", id);
//        query.executeUpdate();
//        return em.find(User.class, id);
    }

    public User findById(int id) {
        return em.find(User.class, id);
    }


}
