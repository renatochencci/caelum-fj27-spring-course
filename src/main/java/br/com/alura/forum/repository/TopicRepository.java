package br.com.alura.forum.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.OpenTopicByCategory;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
	
	@Query("SELECT count(topic) FROM Topic topic "
			+ "JOIN topic.course course "
			+ "JOIN course.subcategory subcategory "
			+ "JOIN subcategory.category category "
			+ "WHERE category = :category")
	int countTopicsByCategory(@Param("category") Category category);

		
	@Query("SELECT count(topic) FROM Topic topic "
			+ "JOIN topic.course course "
			+ "JOIN course.subcategory subcategory "
			+ "JOIN subcategory.category category "
			+ "WHERE category = :category AND topic.creationInstant > :lastWeek")
	int countLastWeekTopicsByCategory(@Param("category") Category category, 
				@Param("lastWeek") Instant lastWeek);

		
	@Query("SELECT count(topic) FROM Topic topic "
			+ "JOIN topic.course course "
			+ "JOIN course.subcategory subcategory "
			+ "JOIN subcategory.category category "
			+ "WHERE category = :category AND topic.status = 'NOT_ANSWERED'")
	int countUnansweredTopicsByCategory(@Param("category") Category category);


	List<Topic> findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(User owner, Instant instant);
	
	@Query("select new br.com.alura.forum.model.OpenTopicByCategory("
			+ "t.course.subcategory.category.name as categoryName, "
			+ "count(t) as topicCount, "
			+ "now() as instant) from Topic t " 
			+ "group by t.course.subcategory.category")
	List<OpenTopicByCategory> findOpenTopicsByCategory();
}