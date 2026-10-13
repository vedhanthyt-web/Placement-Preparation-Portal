package com.college.placementportal.repository;

import com.college.placementportal.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findByCategoryIgnoreCase(String category);
    List<QuestionEntity> findByCategoryIgnoreCaseAndSubCategoryIgnoreCase(String category, String subCategory);

    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<QuestionEntity> findRandomQuestions(@Param("count") int count);

    @Query(value = "SELECT * FROM questions WHERE UPPER(category) = UPPER(:category) ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<QuestionEntity> findRandomQuestionsByCategory(@Param("category") String category, @Param("count") int count);
}
