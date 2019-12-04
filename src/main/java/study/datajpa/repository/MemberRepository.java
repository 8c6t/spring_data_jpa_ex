package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    // @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("SELECT m.username FROM Member m")
    List<String> findUsernameList();

    @Query("SELECT new study.datajpa.dto.MemberDto(m.id, m.username, t.name) FROM Member m JOIN m.team t")
    List<MemberDto> findMemberDto();

    @Query("SELECT m FROM Member m WHERE m.username IN :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionByUsername(String username);

    @Query(value = "SELECT m FROM Member m LEFT JOIN m.team t",
            countQuery = "SELECT COUNT(m) FROM Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    // Slice<Member> findByAge(int age, Pageable pageable);
    // List<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.age = m.age + 1 WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = { "team" })
    List<Member> findAll();

    @EntityGraph(attributePaths = { "team" })
    @Query("SELECT m FROM Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = { "team" })
//    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
