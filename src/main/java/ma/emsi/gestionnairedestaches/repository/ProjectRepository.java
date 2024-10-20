package ma.emsi.gestionnairedestaches.repository;

import ma.emsi.gestionnairedestaches.model.*;
import jakarta.transaction.Transactional;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Query("SELECT p FROM Project p WHERE p.ProjectOwner.id = :id")
    List<Project> findByProjectOwner(@Param("id") Integer id);

    @Query("SELECT p FROM Project p JOIN Team t ON p.ProjectTeam = t JOIN User u ON t.Leader = u OR t IN ( SELECT tm.Teams  FROM t.Members tm WHERE u = tm ) WHERE u.id = :id")
    List<Project> findProjectTeamByUserId(int id); // il affiche les projets ou le user est membre ou leader de la team qui est associer aux projets avec et meme si il est le projectOwner

    @Query("SELECT p FROM Project p JOIN Team t ON p.ProjectOwner.id != :id AND p.ProjectTeam = t JOIN User u ON t.Leader = u OR t IN ( SELECT tm.Teams  FROM t.Members tm WHERE u = tm ) WHERE u.id = :id")
    List<Project> findOtherProjectByUserId(int id);// il affiche les projets ou le user est membre ou leader de la team qui est associer aux projets avec mais il n'est pas le projectOwner

    @Query("SELECT p FROM Project p JOIN Team t ON p.ProjectTeam = t Or p.ProjectOwner.id = :id JOIN User u ON  t.Leader = u OR t IN ( SELECT tm.Teams  FROM t.Members tm WHERE u = tm ) WHERE u.id = :id  ")
    List<Project> findAllProjectByUserId(int id);

    @Query("SELECT u.Projects FROM User u")
    List<Project> findProjectByUser();


    @Query("SELECT p FROM Project p where p.ProjectTeam is null ")
    List<Project> findProjectWithoutTeam();

//    SELECT p.*
//    FROM Project p
//    JOIN Team t ON p.project_team_id = t.id
//    JOIN User u ON t.leader_id = u.id OR t.id IN (SELECT y.teams_id FROM team_members y WHERE u.id = y.members_id)
//    WHERE u.id = 1;

    Project findProjectById(Integer id);


}
