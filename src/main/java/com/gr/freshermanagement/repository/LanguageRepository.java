package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("SELECT l FROM Language l WHERE LOWER(l.languageName) = LOWER(:languageName)")
    Optional<Language> findByLanguageName(@Param("languageName") String languageName);
}
