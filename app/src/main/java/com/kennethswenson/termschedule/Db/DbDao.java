package com.kennethswenson.termschedule.Db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.Mentor;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.Models.TermClass;

import java.util.List;

@Dao
public interface DbDao {
    @Query("SELECT * FROM Term")
    LiveData<List<Term>> loadTerms();

    @Query("SELECT * FROM termclass WHERE termclass.termId == :id")
    LiveData<List<TermClass>> getClassesByTermId(Integer id);

    @Query("SELECT * FROM assessment WHERE assessment.classId == :id")
    LiveData<List<Assessment>> getAssessmentsByClassId(Integer id);

    @Query("SELECT * FROM mentor WHERE mentor.classId == :id")
    LiveData<List<Mentor>> getMentorsByClassId(Integer id);

    @Query("SELECT * FROM termclass WHERE termclass.id == :id")
    TermClass getClassById(Integer id);

    @Query("SELECT * FROM term WHERE term.id == :id")
    Term getTermById(Integer id);

    @Query("SELECT * FROM assessment WHERE assessment.id == :id")
    Assessment getAssessmentById(Integer id);

    @Insert
    void insertTerm(Term term);

    @Insert
    void insertClass(TermClass termClass);

    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertMentor(Mentor mentor);

    @Update
    void updateTerm(Term term);

    @Update
    void updateClass(TermClass termClass);

    @Update
    void updateAssessment(Assessment assessment);

    @Update
    void updateMentor(Mentor mentor);

    @Delete
    void deleteTerms(Term... terms);

    @Delete
    void deleteClasses(TermClass... termClasses);

    @Delete
    void deleteAssessments(Assessment... assessments);

    @Delete void deleteMentors(Mentor... mentors);
}
